package com.winnersystems.smartparking.auth.application.service.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.application.dto.query.UserDto;
import com.winnersystems.smartparking.auth.application.port.input.auth.*;
import com.winnersystems.smartparking.auth.application.port.output.*;
import com.winnersystems.smartparking.auth.domain.exception.*;
import com.winnersystems.smartparking.auth.domain.model.*;

/**
 * Servicio de autenticación.
 * Implementa TODOS los casos de uso relacionados con autenticación.
 *
 * ARQUITECTURA HEXAGONAL:
 * - Este servicio SOLO usa interfaces (ports)
 * - NO conoce implementaciones concretas (JPA, SMTP, JWT library)
 * - Toda la lógica de negocio está aquí
 */
public class AuthService implements
      LoginUseCase,
      LogoutUseCase,
      RefreshTokenUseCase,
      ForgotPasswordUseCase,
      ResetPasswordUseCase {

   // ========== PUERTOS DE SALIDA (Dependencias) ==========
   private final UserPersistencePort userRepository;
   private final TokenPersistencePort tokenRepository;
   private final PasswordEncoderPort passwordEncoder;
   private final JwtPort jwtService;
   private final EmailPort emailService;
   private final CaptchaPort captchaService;

   // Inyección de dependencias por constructor
   public AuthService(
         UserPersistencePort userRepository,
         TokenPersistencePort tokenRepository,
         PasswordEncoderPort passwordEncoder,
         JwtPort jwtService,
         EmailPort emailService,
         CaptchaPort captchaService) {
      this.userRepository = userRepository;
      this.tokenRepository = tokenRepository;
      this.passwordEncoder = passwordEncoder;
      this.jwtService = jwtService;
      this.emailService = emailService;
      this.captchaService = captchaService;
   }

   // ========== IMPLEMENTACIÓN: LOGIN ==========

   @Override
   public AuthResponseDto execute(LoginCommand command) {
      // 1. Validar comando
      command.validate();

      // 2. Validar CAPTCHA
      if (!captchaService.validateCaptcha(command.captchaToken(), null)) {
         throw new InvalidCaptchaException();
      }

      // 3. Buscar usuario por email
      User user = userRepository.findByEmail(command.email())
            .orElseThrow(() -> new InvalidCredentialsException());

      // 4. Verificar contraseña
      if (!passwordEncoder.matches(command.password(), user.getPassword())) {
         throw new InvalidCredentialsException();
      }

      // 5. Verificar que el usuario pueda hacer login
      if (!user.canLogin()) {
         throw new InvalidCredentialsException(
               "Usuario inactivo o email no verificado");
      }

      // 6. Generar tokens
      String accessToken = jwtService.generateAccessToken(user);

      // Refresh token: 24h si NO marcó "recuérdame", 30 días si SÍ
      int refreshTokenHours = command.rememberMe() ? 720 : 24;
      RefreshToken refreshToken = new RefreshToken(user, refreshTokenHours);
      tokenRepository.saveRefreshToken(refreshToken);

      // 7. Actualizar último login
      user.updateLastLogin();
      userRepository.save(user);

      // 8. Construir respuesta
      UserDto userDto = mapToDto(user);
      return new AuthResponseDto(
            accessToken,
            refreshToken.getToken(),
            900, // 15 minutos en segundos
            userDto
      );
   }

   // ========== IMPLEMENTACIÓN: LOGOUT ==========

   @Override
   public void execute(Long userId) {
      User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

      // Revocar todos los refresh tokens del usuario
      tokenRepository.revokeAllRefreshTokensByUser(user);
   }

   @Override
   public void executeWithToken(String refreshToken) {
      RefreshToken token = tokenRepository.findRefreshTokenByToken(refreshToken)
            .orElseThrow(() -> new TokenExpiredException("Token no encontrado"));

      token.revoke();
      tokenRepository.saveRefreshToken(token);
   }

   // ========== IMPLEMENTACIÓN: REFRESH TOKEN ==========

   @Override
   public AuthResponseDto execute(String refreshTokenString) {
      // 1. Buscar refresh token
      RefreshToken refreshToken = tokenRepository
            .findRefreshTokenByToken(refreshTokenString)
            .orElseThrow(() -> new TokenExpiredException("Token inválido"));

      // 2. Validar que sea válido
      if (!refreshToken.isValid()) {
         throw new TokenExpiredException("Token expirado o revocado");
      }

      // 3. Obtener usuario
      User user = refreshToken.getUser();

      // 4. Verificar que el usuario siga activo
      if (!user.canLogin()) {
         throw new InvalidCredentialsException("Usuario inactivo");
      }

      // 5. Generar nuevo access token
      String newAccessToken = jwtService.generateAccessToken(user);

      // 6. (Opcional) Rotation: generar nuevo refresh token
      // refreshToken.revoke();
      // RefreshToken newRefreshToken = new RefreshToken(user, 720);
      // tokenRepository.saveRefreshToken(newRefreshToken);

      // 7. Construir respuesta
      UserDto userDto = mapToDto(user);
      return new AuthResponseDto(
            newAccessToken,
            refreshToken.getToken(), // Mismo refresh token (o nuevo si rotation)
            900,
            userDto
      );
   }

   // ========== IMPLEMENTACIÓN: FORGOT PASSWORD ==========

   @Override
   public void execute(ForgotPasswordCommand command) {
      // 1. Validar
      command.validate();

      // 2. Validar CAPTCHA
      if (!captchaService.validateCaptcha(command.captchaToken(), null)) {
         throw new InvalidCaptchaException();
      }

      // 3. Buscar usuario
      // IMPORTANTE: No revelar si el email existe o no (seguridad)
      User user = userRepository.findByEmail(command.email())
            .orElse(null);

      if (user != null && user.isFullyActive()) {
         // 4. Generar token de reset
         PasswordResetToken resetToken = new PasswordResetToken(user);
         tokenRepository.savePasswordResetToken(resetToken);

         // 5. Enviar email
         emailService.sendPasswordResetEmail(user, resetToken.getToken());
      }

      // SIEMPRE retornar el mismo mensaje (seguridad)
      // No revelar si el email existe o no
   }

   // ========== IMPLEMENTACIÓN: RESET PASSWORD ==========

   @Override
   public void execute(ResetPasswordCommand command) {
      // 1. Validar
      command.validate();

      // 2. Buscar token
      PasswordResetToken resetToken = tokenRepository
            .findPasswordResetTokenByToken(command.token())
            .orElseThrow(() -> new TokenExpiredException("Token inválido"));

      // 3. Validar token
      if (!resetToken.isValid()) {
         throw new TokenExpiredException("Token expirado o ya usado");
      }

      // 4. Obtener usuario
      User user = resetToken.getUser();

      // 5. Encriptar nueva contraseña
      String encodedPassword = passwordEncoder.encode(command.newPassword());

      // 6. Actualizar contraseña
      user.changePassword(encodedPassword);
      userRepository.save(user);

      // 7. Marcar token como usado
      resetToken.markAsUsed();
      tokenRepository.savePasswordResetToken(resetToken);

      // 8. Revocar todos los refresh tokens (seguridad)
      tokenRepository.revokeAllRefreshTokensByUser(user);

      // 9. Enviar email de confirmación
      emailService.sendPasswordChangedEmail(user);
   }

   // ========== HELPER: Mapeo a DTO ==========

   private UserDto mapToDto(User user) {
      // Usar MapStruct o el mapeo manual
      return new UserDto(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getFullName(),
            user.getEmail(),
            user.getPhoneNumber(),
            user.getProfilePicture(),
            user.getStatus(),
            user.isEmailVerified(),
            null, // roles (lazy loading)
            user.getLastLoginAt(),
            user.getCreatedAt(),
            user.getUpdatedAt()
      );
   }
}