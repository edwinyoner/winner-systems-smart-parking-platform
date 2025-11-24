package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO REST para restablecer contraseña con token.
 */
public record ResetPasswordRequest(
      @NotBlank(message = "Token es requerido")
      String token,

      @NotBlank(message = "Nueva contraseña es requerida")
      @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
      String newPassword,

      @NotBlank(message = "Confirmación de contraseña es requerida")
      String confirmPassword
) {
}