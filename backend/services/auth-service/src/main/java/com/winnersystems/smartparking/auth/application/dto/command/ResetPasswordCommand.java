package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Command para restablecer contraseña con token.
 */
public record ResetPasswordCommand(
      String token,
      String newPassword,
      String confirmPassword
) {
   public void validate() {
      if (token == null || token.isBlank()) {
         throw new IllegalArgumentException("Token es requerido");
      }
      if (newPassword == null || newPassword.length() < 8) {
         throw new IllegalArgumentException("Password debe tener al menos 8 caracteres");
      }
      if (!newPassword.equals(confirmPassword)) {
         throw new IllegalArgumentException("Las contraseñas no coinciden");
      }
   }
}