package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO para restablecer contraseña usando token de reset.
 *
 * <p>Usado en: POST /api/auth/reset-password</p>
 *
 * <p><b>Flujo:</b></p>
 * <ol>
 *   <li>Usuario recibe email con link: /reset-password?token=abc123...</li>
 *   <li>Ingresa nueva contraseña y confirmación</li>
 *   <li>Backend valida token (no expirado, no usado)</li>
 *   <li>Backend actualiza contraseña</li>
 *   <li>Backend revoca TODOS los refresh tokens (forzar re-login)</li>
 * </ol>
 *
 * <p><b>Validaciones de contraseña:</b></p>
 * <ul>
 *   <li>Mínimo 8 caracteres</li>
 *   <li>Al menos 1 mayúscula</li>
 *   <li>Al menos 1 minúscula</li>
 *   <li>Al menos 1 número</li>
 *   <li>Al menos 1 carácter especial</li>
 * </ul>
 *
 * @param token UUID del token de reset (recibido por email)
 * @param newPassword Nueva contraseña (requerida, min 8 caracteres)
 * @param confirmPassword Confirmación de contraseña (debe coincidir)
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ResetPasswordRequest(
      @NotBlank(message = "Token es requerido")
      String token,

      @NotBlank(message = "Nueva contraseña es requerida")
      @Size(min = 8, max = 128, message = "La contraseña debe tener entre 8 y 128 caracteres")
      @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe contener al menos: 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial"
      )
      String newPassword,

      @NotBlank(message = "Confirmación de contraseña es requerida")
      String confirmPassword
) {
}