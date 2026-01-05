package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth;

import com.winnersystems.smartparking.auth.application.dto.command.ChangePasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ForgotPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.command.LoginCommand;
import com.winnersystems.smartparking.auth.application.dto.command.ResetPasswordCommand;
import com.winnersystems.smartparking.auth.application.dto.query.AuthResponseDto;
import com.winnersystems.smartparking.auth.application.port.input.auth.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request.*;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.LoginResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response.MessageResponse;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.mapper.AuthRestMapper;
import com.winnersystems.smartparking.auth.infrastructure.config.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * REST Controller para autenticación.
 *
 * <p><b>IMPORTANTE - Arquitectura Microservicios:</b></p>
 * <ul>
 *   <li>API Gateway reescribe: /api/auth/* → /* (sin prefijo)</li>
 *   <li>Este adaptador NO tiene /api/auth en @RequestMapping</li>
 *   <li>Paths son relativos: /login, /logout, /refresh, etc.</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping  // ✅ SIN /api/auth (API Gateway hace el rewrite)
@RequiredArgsConstructor
public class AuthRestAdapter {

   private final LoginUseCase loginUseCase;
   private final LogoutUseCase logoutUseCase;
   private final RefreshTokenUseCase refreshTokenUseCase;
   private final ForgotPasswordUseCase forgotPasswordUseCase;
   private final ResetPasswordUseCase resetPasswordUseCase;
   private final VerifyEmailUseCase verifyEmailUseCase;          // ✅ NUEVO
   private final ResendVerificationUseCase resendVerificationUseCase; // ✅ NUEVO
   private final ChangePasswordUseCase changePasswordUseCase;
   private final AuthRestMapper mapper;

   /**
    * POST /login (desde Gateway: POST /api/auth/login)
    * Iniciar sesión con email y contraseña.
    */
   @PostMapping("/login")
   public ResponseEntity<LoginResponse> login(
         @Valid @RequestBody LoginRequest request,
         HttpServletRequest httpRequest) {

      // 1. Convertir Request → Command (con info del HttpServletRequest)
      LoginCommand command = mapper.toCommand(request, httpRequest);

      // 2. Ejecutar caso de uso
      AuthResponseDto response = loginUseCase.execute(command);

      // 3. Convertir DTO → Response
      LoginResponse loginResponse = mapper.toLoginResponse(response);

      // 4. Retornar HTTP 200
      return ResponseEntity.ok(loginResponse);
   }

   /**
    * POST /logout (desde Gateway: POST /api/auth/logout)
    * Cerrar sesión revocando el refresh token.
    */
   @PostMapping("/logout")
   public ResponseEntity<MessageResponse> logout(
         @Valid @RequestBody RefreshTokenRequest request) {

      // Revocar refresh token
      logoutUseCase.executeLogout(request.refreshToken());

      return ResponseEntity.ok(
            new MessageResponse("Sesión cerrada exitosamente")
      );
   }

   /**
    * POST /refresh (desde Gateway: POST /api/auth/refresh)
    * Renovar access token usando refresh token.
    */
   @PostMapping("/refresh")
   public ResponseEntity<LoginResponse> refreshToken(
         @Valid @RequestBody RefreshTokenRequest request) {

      // Ejecutar caso de uso
      AuthResponseDto response = refreshTokenUseCase.executeRefresh(request.refreshToken());

      // Convertir y retornar
      LoginResponse loginResponse = mapper.toLoginResponse(response);

      return ResponseEntity.ok(loginResponse);
   }

   /**
    * POST /forgot-password (desde Gateway: POST /api/auth/forgot-password)
    * Solicitar restablecimiento de contraseña por email.
    */
   @PostMapping("/forgot-password")
   public ResponseEntity<MessageResponse> forgotPassword(
         @Valid @RequestBody ForgotPasswordRequest request,
         HttpServletRequest httpRequest) {

      // Convertir Request → Command (con info del HttpServletRequest)
      ForgotPasswordCommand command = mapper.toCommand(request, httpRequest);

      forgotPasswordUseCase.execute(command);

      // SIEMPRE retornar el mismo mensaje (seguridad: no revelar si email existe)
      return ResponseEntity.ok(
            new MessageResponse("Si el email existe, recibirás un enlace para restablecer tu contraseña")
      );
   }

   /**
    * POST /reset-password (desde Gateway: POST /api/auth/reset-password)
    * Restablecer contraseña usando token recibido por email.
    */
   @PostMapping("/reset-password")
   public ResponseEntity<MessageResponse> resetPassword(
         @Valid @RequestBody ResetPasswordRequest request,
         HttpServletRequest httpRequest) {

      ResetPasswordCommand command = mapper.toCommand(request, httpRequest);

      resetPasswordUseCase.execute(command);

      return ResponseEntity.ok(
            new MessageResponse("Contraseña restablecida exitosamente")
      );
   }

   /**
    * GET /verify-email?token=xxx (desde Gateway: GET /api/auth/verify-email?token=xxx)
    * Verificar email del usuario usando token recibido por email.
    *
    * <p><b>Flujo:</b></p>
    * <ol>
    *   <li>Usuario hace clic en enlace del email de bienvenida/verificación</li>
    *   <li>Frontend recibe token en query param y llama a este endpoint</li>
    *   <li>Backend valida token y marca email como verificado</li>
    *   <li>Usuario puede iniciar sesión</li>
    * </ol>
    */

   @GetMapping("/verify-email")
   public ResponseEntity<?> verifyEmail(
         @RequestParam("token") String token,
         @RequestParam(value = "redirect", required = false, defaultValue = "false") boolean redirect) {

      verifyEmailUseCase.verifyEmail(token);

      if (redirect) {
         // Redirige al login del frontend con parámetro de éxito
         return ResponseEntity.status(302)
               .location(URI.create("http://localhost:4200/login?verified=true"))
               .build();
      }

      // Si no hay redirect (llamada directa), responde JSON
      return ResponseEntity.ok(new MessageResponse("Email verificado exitosamente. Ya puedes iniciar sesión."));
   }

   /**
    * POST /resend-verification (desde Gateway: POST /api/auth/resend-verification)
    * Reenviar email de verificación a un usuario.
    *
    * <p><b>Casos de uso:</b></p>
    * <ul>
    *   <li>Usuario no recibió el email original</li>
    *   <li>Token de verificación expiró (24 horas)</li>
    *   <li>Email fue eliminado accidentalmente</li>
    * </ul>
    *
    * <p><b>Rate limiting:</b> Máximo 3 solicitudes por hora por usuario.</p>
    */
   @PostMapping("/resend-verification")
   public ResponseEntity<MessageResponse> resendVerification(
         @Valid @RequestBody ResendVerificationRequest request) {

      // Ejecutar caso de uso
      resendVerificationUseCase.resendVerification(request.email());

      return ResponseEntity.ok(
            new MessageResponse("Email de verificación enviado. Por favor, revisa tu bandeja de entrada.")
      );
   }

   /**
    * PUT /change-password (desde Gateway: PUT /api/auth/change-password)
    * Cambiar contraseña del usuario autenticado.
    *
    * <p><b>REQUIERE AUTENTICACIÓN:</b> JWT válido en header Authorization</p>
    */
   @PutMapping("/change-password")
   public ResponseEntity<MessageResponse> changePassword(
         @Valid @RequestBody ChangePasswordRequest request,
         HttpServletRequest httpRequest,
         @AuthenticationPrincipal CustomUserDetails userDetails) {

      // ✅ DEBUG - VER QUÉ ESTÁ PASANDO
      System.out.println("========== CHANGE PASSWORD DEBUG ==========");
      System.out.println("📋 Request received");
      System.out.println("🔐 Authorization Header: " + httpRequest.getHeader("Authorization"));
      System.out.println("👤 UserDetails: " + userDetails);

      if (userDetails != null) {
         System.out.println("   - UserDetails Class: " + userDetails.getClass().getName());
         System.out.println("   - UserId: " + userDetails.getUserId());
         System.out.println("   - Email: " + userDetails.getUsername());
         System.out.println("   - Authorities: " + userDetails.getAuthorities());
      } else {
         System.out.println("   ❌ UserDetails is NULL!");
      }
      System.out.println("==========================================");

      // Si userDetails es null, retornar 403
      if (userDetails == null) {
         System.err.println("❌ ERROR: No se pudo obtener el usuario autenticado");
         return ResponseEntity.status(403).body(
               new MessageResponse("No tienes permisos para realizar esta acción")
         );
      }

      Long userId = userDetails.getUserId();

      ChangePasswordCommand command = mapper.toCommand(request, httpRequest, userId);
      changePasswordUseCase.execute(command);

      return ResponseEntity.ok(
            new MessageResponse("Contraseña cambiada exitosamente. Por favor, inicia sesión nuevamente.")
      );
   }
}