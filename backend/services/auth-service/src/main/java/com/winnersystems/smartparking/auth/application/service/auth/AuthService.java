package com.winnersystems.smartparking.auth.application.service.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ChangePasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.application.dto.query.CaptchaValidationResult;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.port.input.auth.*;
import com.winnersystems.smartparking.auth.application.port.output.*;
import com.winnersystems.smartparking.auth.domain.exception.InvalidCaptchaException;
import com.winnersystems.smartparking.auth.domain.exception.InvalidCredentialsException;
import com.winnersystems.smartparking.auth.domain.exception.TokenExpiredException;
import com.winnersystems.smartparking.auth.domain.exception.TokenInvalidException;
import com.winnersystems.smartparking.auth.domain.exception.UserNotFoundException;
import com.winnersystems.smartparking.auth.domain.model.PasswordResetToken;
import com.winnersystems.smartparking.auth.domain.model.RefreshToken;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.domain.model.VerificationToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para autenticación.
 * Implementa todos los casos de uso de auth/.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthService implements
      LoginUseCase,
      LogoutUseCase,
      RefreshTokenUseCase,
      ForgotPasswordUseCase,
      ResetPasswordUseCase,
      VerifyEmailUseCase,
      ResendVerificationUseCase,
      ChangePasswordUseCase{

   private final UserPersistencePort userPersistencePort;
   private final PasswordEncoderPort passwordEncoderPort;
   private final JwtPort jwtPort;
   private final TokenPersistencePort tokenPersistencePort;
   private final CaptchaPort captchaPort;
   private final EmailPort emailPort;

   // ========== LOGIN ==========

   @Override
   public AuthResponseDto execute(LoginCommand command) {
      // 1. Validar captcha
      if (captchaPort.isEnabled()) {
         CaptchaValidationResult result = captchaPort.validate(
               command.captchaToken(),
               command.ipAddress()
         );

         if (!result.success() || result.score() < 0.5) {
            throw new InvalidCaptchaException("Captcha inválido o score bajo");
         }
      }

      // 2. Buscar usuario por email
      User user = userPersistencePort.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());

      // 3. Verificar contraseña
      if (!passwordEncoderPort.matches(command.password(), user.getPassword())) {
         throw new InvalidCredentialsException();
      }

      // 4. Verificar que esté activo y verificado
      if (!user.getStatus()) {
         throw new IllegalStateException("Usuario inactivo");
      }

      if (!user.isEmailVerified()) {
         throw new IllegalStateException("Email no verificado");
      }

      // 5. Generar access token (JWT)
      String accessToken = jwtPort.generateAccessToken(
            user.getId(),
            user.getEmail(),
            getRoleNames(user),
            getPermissionNames(user)
      );

      // 6. Generar y guardar refresh token (UUID v4)
      String refreshTokenValue = UUID.randomUUID().toString();

      int validityHours = command.rememberMe() ? 720 : 168; // 30 días vs 7 días

      RefreshToken refreshToken = new RefreshToken(
            refreshTokenValue,
            user.getId(),
            validityHours,
            command.deviceInfo(),
            command.ipAddress()
      );

      tokenPersistencePort.saveRefreshToken(refreshToken);

      // 7. Retornar respuesta
      return AuthResponseDto.of(
            accessToken,
            refreshTokenValue,
            1800,                           // 30 minutos
            mapToDto(user)
      );
   }

   // ========== LOGOUT ==========

   @Override
   public void executeLogout(String refreshToken) {
      // 1. Buscar token directo (búsqueda O(1) con índice)
      RefreshToken token = tokenPersistencePort.findRefreshTokenByToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

      // 2. Validar que no esté revocado
      if (token.getRevoked()) {
         throw new IllegalStateException("Token ya fue revocado");
      }

      // 3. Revocar usando método de dominio
      token.revoke();
      tokenPersistencePort.saveRefreshToken(token);
   }

   // ========== REFRESH TOKEN ==========

   @Override
   public AuthResponseDto executeRefresh(String refreshToken) {
      // 1. Buscar token directo (búsqueda O(1) con índice)
      RefreshToken token = tokenPersistencePort.findRefreshTokenByToken(refreshToken)
            .orElseThrow(() -> new TokenExpiredException("RefreshToken", "Token inválido"));

      // 2. Validar que no esté revocado
      if (token.getRevoked()) {
         throw new IllegalStateException("Token revocado");
      }

      // 3. Validar que no haya expirado
      if (token.isExpired()) {
         throw new TokenExpiredException("RefreshToken", "Token expirado");
      }

      // 4. Obtener usuario
      User user = userPersistencePort.findById(token.getUserId())
            .orElseThrow(() -> new UserNotFoundException(token.getUserId()));

      // 5. Verificar que esté activo
      if (!user.getStatus() || !user.isEmailVerified()) {
         throw new IllegalStateException("Usuario inactivo o email no verificado");
      }

      // 6. Generar nuevo access token
      String newAccessToken = jwtPort.generateAccessToken(
            user.getId(),
            user.getEmail(),
            getRoleNames(user),
            getPermissionNames(user)
      );

      // 7. Retornar con MISMO refreshToken (NO rotación)
      return AuthResponseDto.of(
            newAccessToken,
            refreshToken,
            1800,                           // 30 minutos
            mapToDto(user)
      );
   }

   // ========== FORGOT PASSWORD ==========

   @Override
   public void execute(ForgotPasswordCommand command) {
      // 1. Validar captcha (más permisivo: 0.3)
      if (captchaPort.isEnabled()) {
         CaptchaValidationResult result = captchaPort.validate(
               command.captchaToken(),
               command.ipAddress()
         );

         if (!result.success() || result.score() < 0.3) {
            throw new InvalidCaptchaException("Captcha inválido");
         }
      }

      // 2. Buscar usuario (sin revelar si existe)
      var userOpt = userPersistencePort.findByEmail(command.email());

      if (userOpt.isEmpty()) {
         // NO revelar que email no existe - retornar silenciosamente
         return;
      }

      User user = userOpt.get();

      // 3. Verificar rate limiting (máx 3 por hora)
      long recentCount = tokenPersistencePort.countPasswordResetTokensByUserSince(
            user.getId(),
            LocalDateTime.now().minusHours(1)
      );

      if (recentCount >= 3) {
         throw new IllegalStateException("Demasiadas solicitudes. Intenta en 1 hora");
      }

      // 4. Revocar tokens previos
      tokenPersistencePort.revokePasswordResetTokensByUserId(user.getId());

      // 5. Generar token (UUID v4)
      String tokenValue = UUID.randomUUID().toString();

      PasswordResetToken token = new PasswordResetToken(
            tokenValue,
            user.getId(),
            command.ipAddress(),
            command.userAgent()
      );

      tokenPersistencePort.savePasswordResetToken(token);

      // 6. Construir link
      String resetLink = "http://localhost:4200/reset-password?token=" + tokenValue;

      // 7. Enviar email
      try {
         emailPort.sendPasswordResetEmail(
               user.getEmail(),
               user.getFullName(),
               resetLink,
               1 // 1 hora
         );
      } catch (Exception e) {
         // Log error pero no fallar
      }
   }

   // ========== RESET PASSWORD ==========

   @Override
   public void execute(ResetPasswordCommand command) {
      // 1. Buscar token directo (búsqueda O(1) con índice)
      PasswordResetToken token = tokenPersistencePort.findPasswordResetTokenByToken(command.token())
            .orElseThrow(() -> new TokenExpiredException("Token", "Token inválido"));

      // 2. Validar que sea válido (no usado, no expirado)
      if (!token.isValid()) {
         if (token.isUsed()) {
            throw new IllegalStateException("Token ya fue usado");
         }
         throw new TokenExpiredException("Token", "Token expirado");
      }

      // 3. Validar que passwords coincidan
      if (!command.newPassword().equals(command.confirmPassword())) {
         throw new IllegalArgumentException("Las contraseñas no coinciden");
      }

      // 4. Buscar usuario
      User user = userPersistencePort.findById(token.getUserId())
            .orElseThrow(() -> new UserNotFoundException(token.getUserId()));

      // 5. Hashear y actualizar password
      String hashedPassword = passwordEncoderPort.encode(command.newPassword());
      user.setPassword(hashedPassword);
      user.setUpdatedAt(LocalDateTime.now());
      userPersistencePort.save(user);

      // 6. Marcar token como usado (método de dominio)
      token.markAsUsed();
      tokenPersistencePort.savePasswordResetToken(token);

      // 7. Revocar TODOS los refresh tokens (forzar re-login)
      tokenPersistencePort.revokeRefreshTokensByUserId(user.getId());

      // 8. Enviar email de confirmación
      try {
         emailPort.sendPasswordChangedEmail(
               user.getEmail(),
               user.getFullName(),
               LocalDateTime.now().toString()
         );
      } catch (Exception e) {
         // Log error pero no fallar
      }
   }

   // ========== VERIFY EMAIL ==========

   @Override
   public void verifyEmail(String token) {
      // 1. Buscar token directo (búsqueda O(1) con índice)
      VerificationToken verificationToken = tokenPersistencePort
            .findVerificationTokenByToken(token)
            .orElseThrow(() -> new TokenInvalidException("Token de verificación inválido o no existe"));

      // 2. Validar que no haya expirado
      if (verificationToken.isExpired()) {
         throw new TokenExpiredException("VerificationToken", "El token de verificación ha expirado");
      }

      // 3. Validar que no haya sido usado
      if (verificationToken.isVerified()) {
         throw new TokenInvalidException("Este token ya fue utilizado previamente");
      }

      // 4. Buscar usuario
      User user = userPersistencePort.findById(verificationToken.getUserId())
            .orElseThrow(() -> new UserNotFoundException(verificationToken.getUserId()));

      // 5. Verificar si ya está verificado (evitar procesamiento duplicado)
      if (user.isEmailVerified()) {
         throw new IllegalStateException("Este email ya ha sido verificado");
      }

      // 6. Marcar email como verificado
      user.verifyEmail();
      userPersistencePort.save(user);

      // 7. Marcar token como usado (método de dominio)
      verificationToken.markAsVerified();
      tokenPersistencePort.saveVerificationToken(verificationToken);

      // 8. Enviar email de bienvenida/confirmación (opcional)
      try {
         emailPort.sendEmailVerifiedConfirmation(
               user.getEmail(),
               user.getFullName()
         );
      } catch (Exception e) {
         // Log error pero no fallar (verificación ya exitosa)
      }
   }

   // ========== RESEND VERIFICATION ==========

   @Override
   public void resendVerification(String email) {
      // 1. Buscar usuario por email
      User user = userPersistencePort.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));

      // 2. Verificar que NO esté ya verificado
      if (user.isEmailVerified()) {
         throw new IllegalStateException("Este email ya ha sido verificado");
      }

      // 3. Verificar rate limiting (máx 3 por hora)
      long recentCount = tokenPersistencePort.countVerificationTokensByUserSince(
            user.getId(),
            LocalDateTime.now().minusHours(1)
      );

      if (recentCount >= 3) {
         throw new IllegalStateException("Demasiadas solicitudes de verificación. Intenta en 1 hora");
      }

      // 4. Revocar tokens de verificación previos (opcional pero recomendado)
      tokenPersistencePort.deleteVerificationTokensByUserId(user.getId());

      // 5. Generar nuevo token (UUID v4)
      String tokenValue = UUID.randomUUID().toString();

      VerificationToken verificationToken = new VerificationToken(
            tokenValue,
            user.getId(),
            24L // 24 horas de validez
      );

      tokenPersistencePort.saveVerificationToken(verificationToken);

      // 6. Construir link de verificación
      String verificationLink = "http://localhost:4200/verify-email?token=" + tokenValue;

      // 7. Enviar email de verificación
      emailPort.sendVerificationEmail(
            user.getEmail(),
            user.getFullName(),
            verificationLink
      );
   }

   // ========== CHANGE PASSWORD ==========

   @Override
   public void execute(ChangePasswordCommand command) {
      // 1. Validar que passwords coincidan
      if (!command.newPassword().equals(command.confirmPassword())) {
         throw new IllegalArgumentException("Las contraseñas no coinciden");
      }

      // 2. Buscar usuario
      User user = userPersistencePort.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

      // 3. Verificar contraseña actual
      if (!passwordEncoderPort.matches(command.currentPassword(), user.getPassword())) {
         throw new IllegalStateException("La contraseña actual es incorrecta");
      }

      // 4. Validar que nueva contraseña sea diferente
      if (passwordEncoderPort.matches(command.newPassword(), user.getPassword())) {
         throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
      }

      // 5. Hashear y actualizar password
      String hashedPassword = passwordEncoderPort.encode(command.newPassword());
      user.setPassword(hashedPassword);
      user.setUpdatedAt(LocalDateTime.now());
      userPersistencePort.save(user);

      // 6. Revocar TODOS los refresh tokens (forzar re-login)
      tokenPersistencePort.revokeRefreshTokensByUserId(user.getId());

      // 7. Enviar email de confirmación
      try {
         emailPort.sendPasswordChangedEmail(
               user.getEmail(),
               user.getFullName(),
               LocalDateTime.now().toString()
         );
      } catch (Exception e) {
         // Log error pero no fallar
      }
   }

   // ========== HELPERS ==========

   private UserDto mapToDto(User user) {
      return new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getProfilePicture(),
            user.getStatus(),
            user.isEmailVerified(),
            getRoleNames(user),
            user.getCreatedAt(),
            user.getUpdatedAt()
      );
   }

   private Set<String> getRoleNames(User user) {
      return user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toSet());
   }

   private Set<String> getPermissionNames(User user) {
      return user.getRoles().stream()
            .flatMap(role -> role.getPermissions().stream())
            .map(permission -> permission.getName())
            .collect(Collectors.toSet());
   }


}