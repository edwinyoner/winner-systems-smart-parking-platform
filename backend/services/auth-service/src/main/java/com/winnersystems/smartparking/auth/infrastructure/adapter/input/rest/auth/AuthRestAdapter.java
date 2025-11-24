package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.application.port.input.auth.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ForgotPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.LoginRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.RefreshTokenRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.ResetPasswordRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.LoginResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.MessageResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.mapper.AuthRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller para autenticación.
 *
 * ESTE ES UN ADAPTADOR DE ENTRADA:
 * - Recibe peticiones HTTP (entrada)
 * - Convierte Request → Command (DTO Application)
 * - Llama a los Use Cases (Application)
 * - Convierte Response DTO → REST Response
 * - Retorna HTTP Response
 *
 * ¿Por qué NO tiene lógica de negocio?
 * - Toda la lógica está en Application Layer (Services)
 * - Este controller es DELGADO, solo transforma datos HTTP ↔ Application
 */
@RestController
@RequestMapping("/api/auth")
public class AuthRestAdapter {

   private final LoginUseCase loginUseCase;
   private final LogoutUseCase logoutUseCase;
   private final RefreshTokenUseCase refreshTokenUseCase;
   private final ForgotPasswordUseCase forgotPasswordUseCase;
   private final ResetPasswordUseCase resetPasswordUseCase;
   private final AuthRestMapper mapper;

   public AuthRestAdapter(
         LoginUseCase loginUseCase,
         LogoutUseCase logoutUseCase,
         RefreshTokenUseCase refreshTokenUseCase,
         ForgotPasswordUseCase forgotPasswordUseCase,
         ResetPasswordUseCase resetPasswordUseCase,
         AuthRestMapper mapper) {
      this.loginUseCase = loginUseCase;
      this.logoutUseCase = logoutUseCase;
      this.refreshTokenUseCase = refreshTokenUseCase;
      this.forgotPasswordUseCase = forgotPasswordUseCase;
      this.resetPasswordUseCase = resetPasswordUseCase;
      this.mapper = mapper;
   }

   /**
    * POST /api/auth/login
    * Iniciar sesión
    */
   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
      // 1. Convertir Request → Command
      LoginCommand command = mapper.toCommand(request);

      // 2. Ejecutar caso de uso
      AuthResponseDto response = loginUseCase.execute(command);

      // 3. Convertir DTO → Response
      LoginResponse loginResponse = mapper.toLoginResponse(response);

      // 4. Retornar HTTP 200
      return ResponseEntity.ok(loginResponse);
   }

   /**
    * POST /api/auth/logout
    * Cerrar sesión
    */
   @PostMapping("/logout")
   public ResponseEntity<MessageResponse> logout(@RequestParam Long userId) {
      logoutUseCase.execute(userId);
      return ResponseEntity.ok(new MessageResponse("Sesión cerrada exitosamente"));
   }

   /**
    * POST /api/auth/refresh
    * Refrescar access token
    */
   @PostMapping("/refresh")
   public ResponseEntity<LoginResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
      // Ejecutar caso de uso
      AuthResponseDto response = refreshTokenUseCase.execute(request.refreshToken());

      // Convertir y retornar
      LoginResponse loginResponse = mapper.toLoginResponse(response);
      return ResponseEntity.ok(loginResponse);
   }

   /**
    * POST /api/auth/forgot-password
    * Solicitar restablecimiento de contraseña
    */
   @PostMapping("/forgot-password")
   public ResponseEntity<MessageResponse> forgotPassword(
         @Valid @RequestBody ForgotPasswordRequest request) {

      ForgotPasswordCommand command = mapper.toCommand(request);
      forgotPasswordUseCase.execute(command);

      // SIEMPRE retornar el mismo mensaje (seguridad)
      return ResponseEntity.ok(
            new MessageResponse("Si el email existe, recibirás un enlace para restablecer tu contraseña")
      );
   }

   /**
    * POST /api/auth/reset-password
    * Restablecer contraseña con token
    */
   @PostMapping("/reset-password")
   public ResponseEntity<MessageResponse> resetPassword(
         @Valid @RequestBody ResetPasswordRequest request) {

      ResetPasswordCommand command = mapper.toCommand(request);
      resetPasswordUseCase.execute(command);

      return ResponseEntity.ok(
            new MessageResponse("Contraseña restablecida exitosamente")
      );
   }
}