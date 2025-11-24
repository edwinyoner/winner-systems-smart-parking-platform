package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO REST para solicitar restablecimiento de contraseña.
 */
public record ForgotPasswordRequest(
      @NotBlank(message = "Email es requerido")
      @Email(message = "Email debe ser válido")
      String email,

      String captchaToken
) {
}