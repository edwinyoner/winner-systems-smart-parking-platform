package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO para cambiar contraseña.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ChangePasswordRequest(

      @NotBlank(message = "La contraseña actual es requerida")
      String currentPassword,

      @NotBlank(message = "La nueva contraseña es requerida")
      @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 12 caracteres")
      @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
      )
      String newPassword,

      @NotBlank(message = "La confirmación de contraseña es requerida")
      String confirmPassword
) {
}