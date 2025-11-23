package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Command para solicitar restablecimiento de contraseña.
 */
public record ForgotPasswordCommand(
      String email,
      String captchaToken
) {
   public void validate() {
      if (email == null || email.isBlank()) {
         throw new IllegalArgumentException("Email es requerido");
      }
   }
}