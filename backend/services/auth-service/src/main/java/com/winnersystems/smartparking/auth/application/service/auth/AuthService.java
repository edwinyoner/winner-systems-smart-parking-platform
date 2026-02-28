package com.winnersystems.smartparking.auth.application.service.auth;

import com.winnersystems.smartparking.auth.application.dto.command.*;
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
      ChangePasswordUseCase,
      UpdateProfileUseCase {

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

      // 5. VALIDAR QUE EL USUARIO TENGA EL ROL SELECCIONADO
      String activeRole = command.selectedRole();
      if (activeRole != null && !activeRole.isBlank()) {
         if (!user.hasRole(activeRole)) {
            throw new InvalidCredentialsException("No tienes el rol seleccionado");
         }
      } else {
         // Si no seleccionó rol, usar el primero que tenga
         activeRole = user.getRoles().stream()
               .findFirst()
               .map(Role::getName)
               .orElse("OPERADOR");
      }

      // 6. Generar access token CON activeRole Y SUS PERMISOS
      String accessToken = jwtPort.generateAccessToken(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getProfilePicture(),
            user.getStatus(),
            user.isEmailVerified(),
            activeRole,
            getRoleNames(user),
            getPermissionNames(user, activeRole)  // ✅ PASAR activeRole
      );

      // 7. Generar y guardar refresh token
      String refreshTokenValue = UUID.randomUUID().toString();

      int validityHours = command.rememberMe() ? 720 : 168;

      RefreshToken refreshToken = new RefreshToken(
            refreshTokenValue,
            user.getId(),
            validityHours,
            command.deviceInfo(),
            command.ipAddress()
      );

      tokenPersistencePort.saveRefreshToken(refreshToken);

      // 8. Retornar respuesta
      return AuthResponseDto.of(
            accessToken,
            refreshTokenValue,
            1800,
            mapToDto(user)
      );
   }

   // ========== LOGOUT ==========

   @Override
   public void executeLogout(String refreshToken) {
      RefreshToken token = tokenPersistencePort.findRefreshTokenByToken(refreshToken)
            .orElseThrow(() -> new IllegalArgumentException("Token inválido"));

      if (token.getRevoked()) {
         throw new IllegalStateException("Token ya fue revocado");
      }

      token.revoke();
      tokenPersistencePort.saveRefreshToken(token);
   }

   // ========== REFRESH TOKEN ==========

   @Override
   public AuthResponseDto executeRefresh(String refreshToken) {
      // 1. Buscar token
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

      // 6. Obtener el rol activo del token anterior (o usar el primero)
      String activeRole = user.getRoles().stream()
            .findFirst()
            .map(Role::getName)
            .orElse("OPERADOR");

      // 7. Generar nuevo access token CON LOS PERMISOS DEL ROL ACTIVO
      String newAccessToken = jwtPort.generateAccessToken(
            user.getId(),
            user.getEmail(),
            user.getFirstName(),
            user.getLastName(),
            user.getPhoneNumber(),
            user.getProfilePicture(),
            user.getStatus(),
            user.isEmailVerified(),
            activeRole,
            getRoleNames(user),
            getPermissionNames(user, activeRole)  // ✅ PASAR activeRole
      );

      // 8. Retornar con MISMO refreshToken
      return AuthResponseDto.of(
            newAccessToken,
            refreshToken,
            1800,
            mapToDto(user)
      );
   }

   // ========== FORGOT PASSWORD ==========

   @Override
   public void execute(ForgotPasswordCommand command) {
      if (captchaPort.isEnabled()) {
         CaptchaValidationResult result = captchaPort.validate(
               command.captchaToken(),
               command.ipAddress()
         );

         if (!result.success() || result.score() < 0.3) {
            throw new InvalidCaptchaException("Captcha inválido");
         }
      }

      var userOpt = userPersistencePort.findByEmail(command.email());

      if (userOpt.isEmpty()) {
         return;
      }

      User user = userOpt.get();

      long recentCount = tokenPersistencePort.countPasswordResetTokensByUserSince(
            user.getId(),
            LocalDateTime.now().minusHours(1)
      );

      if (recentCount >= 3) {
         throw new IllegalStateException("Demasiadas solicitudes. Intenta en 1 hora");
      }

      tokenPersistencePort.revokePasswordResetTokensByUserId(user.getId());

      String tokenValue = UUID.randomUUID().toString();

      PasswordResetToken token = new PasswordResetToken(
            tokenValue,
            user.getId(),
            command.ipAddress(),
            command.userAgent()
      );

      tokenPersistencePort.savePasswordResetToken(token);

      String resetLink = "http://localhost:4200/reset-password?token=" + tokenValue;

      try {
         emailPort.sendPasswordResetEmail(
               user.getEmail(),
               user.getFullName(),
               resetLink,
               1
         );
      } catch (Exception e) {
         // Log error
      }
   }

   // ========== RESET PASSWORD ==========

   @Override
   public void execute(ResetPasswordCommand command) {
      PasswordResetToken token = tokenPersistencePort.findPasswordResetTokenByToken(command.token())
            .orElseThrow(() -> new TokenExpiredException("Token", "Token inválido"));

      if (!token.isValid()) {
         if (token.isUsed()) {
            throw new IllegalStateException("Token ya fue usado");
         }
         throw new TokenExpiredException("Token", "Token expirado");
      }

      if (!command.newPassword().equals(command.confirmPassword())) {
         throw new IllegalArgumentException("Las contraseñas no coinciden");
      }

      User user = userPersistencePort.findById(token.getUserId())
            .orElseThrow(() -> new UserNotFoundException(token.getUserId()));

      String hashedPassword = passwordEncoderPort.encode(command.newPassword());
      user.setPassword(hashedPassword);
      user.setUpdatedAt(LocalDateTime.now());
      userPersistencePort.save(user);

      token.markAsUsed();
      tokenPersistencePort.savePasswordResetToken(token);

      tokenPersistencePort.revokeRefreshTokensByUserId(user.getId());

      try {
         emailPort.sendPasswordChangedEmail(
               user.getEmail(),
               user.getFullName(),
               LocalDateTime.now().toString()
         );
      } catch (Exception e) {
         // Log error
      }
   }

   // ========== VERIFY EMAIL ==========

   @Override
   public void verifyEmail(String token) {
      VerificationToken verificationToken = tokenPersistencePort
            .findVerificationTokenByToken(token)
            .orElseThrow(() -> new TokenInvalidException("Token de verificación inválido o no existe"));

      if (verificationToken.isExpired()) {
         throw new TokenExpiredException("VerificationToken", "El token de verificación ha expirado");
      }

      if (verificationToken.isVerified()) {
         throw new TokenInvalidException("Este token ya fue utilizado previamente");
      }

      User user = userPersistencePort.findById(verificationToken.getUserId())
            .orElseThrow(() -> new UserNotFoundException(verificationToken.getUserId()));

      if (user.isEmailVerified()) {
         throw new IllegalStateException("Este email ya ha sido verificado");
      }

      user.verifyEmail();
      userPersistencePort.save(user);

      verificationToken.markAsVerified();
      tokenPersistencePort.saveVerificationToken(verificationToken);

      try {
         emailPort.sendEmailVerifiedConfirmation(
               user.getEmail(),
               user.getFullName()
         );
      } catch (Exception e) {
         // Log error
      }
   }

   // ========== RESEND VERIFICATION ==========

   @Override
   public void resendVerification(String email) {
      User user = userPersistencePort.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado con email: " + email));

      if (user.isEmailVerified()) {
         throw new IllegalStateException("Este email ya ha sido verificado");
      }

      long recentCount = tokenPersistencePort.countVerificationTokensByUserSince(
            user.getId(),
            LocalDateTime.now().minusHours(1)
      );

      if (recentCount >= 3) {
         throw new IllegalStateException("Demasiadas solicitudes de verificación. Intenta en 1 hora");
      }

      tokenPersistencePort.deleteVerificationTokensByUserId(user.getId());

      String tokenValue = UUID.randomUUID().toString();

      VerificationToken verificationToken = new VerificationToken(
            tokenValue,
            user.getId(),
            24L
      );

      tokenPersistencePort.saveVerificationToken(verificationToken);

      String verificationLink = "http://localhost:4200/verify-email?token=" + tokenValue;

      emailPort.sendVerificationEmail(
            user.getEmail(),
            user.getFullName(),
            verificationLink
      );
   }

   // ========== CHANGE PASSWORD ==========

   @Override
   public void execute(ChangePasswordCommand command) {
      if (!command.newPassword().equals(command.confirmPassword())) {
         throw new IllegalArgumentException("Las contraseñas no coinciden");
      }

      User user = userPersistencePort.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

      if (!passwordEncoderPort.matches(command.currentPassword(), user.getPassword())) {
         throw new IllegalStateException("La contraseña actual es incorrecta");
      }

      if (passwordEncoderPort.matches(command.newPassword(), user.getPassword())) {
         throw new IllegalArgumentException("La nueva contraseña debe ser diferente a la actual");
      }

      String hashedPassword = passwordEncoderPort.encode(command.newPassword());
      user.setPassword(hashedPassword);
      user.setUpdatedAt(LocalDateTime.now());
      userPersistencePort.save(user);

      tokenPersistencePort.revokeRefreshTokensByUserId(user.getId());

      try {
         emailPort.sendPasswordChangedEmail(
               user.getEmail(),
               user.getFullName(),
               LocalDateTime.now().toString()
         );
      } catch (Exception e) {
         // Log error
      }
   }

   // ========== UPDATE PROFILE ==========

   @Override
   public UserDto execute(UpdateProfileCommand command) {
      User user = userPersistencePort.findById(command.userId())
            .orElseThrow(() -> new UserNotFoundException(command.userId()));

      user.setFirstName(command.firstName());
      user.setLastName(command.lastName());
      user.setPhoneNumber(command.phoneNumber());
      user.setProfilePicture(command.profilePicture());
      user.setUpdatedAt(LocalDateTime.now());

      User updatedUser = userPersistencePort.save(user);

      return mapToDto(updatedUser);
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

   /**
    * MÉTODO - Obtiene solo los permisos del rol activo
    */
   private Set<String> getPermissionNames(User user, String activeRole) {
      return user.getRoles().stream()
            .filter(role -> role.getName().equals(activeRole))  // Filtrar por rol activo
            .flatMap(role -> role.getPermissions().stream())
            .map(permission -> permission.getName())
            .collect(Collectors.toSet());
   }
}