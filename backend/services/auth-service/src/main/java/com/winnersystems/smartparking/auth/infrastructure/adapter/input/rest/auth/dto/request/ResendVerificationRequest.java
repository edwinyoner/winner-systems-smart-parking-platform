package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO para reenviar email de verificación.
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
public record ResendVerificationRequest(

      @NotBlank(message = "El email es obligatorio")
      @Email(message = "El email debe ser válido")
      String email
) {
}