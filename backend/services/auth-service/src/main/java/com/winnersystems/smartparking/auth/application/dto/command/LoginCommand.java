package com.winnersystems.smartparking.auth.application.dto.command;

/**
 * Command para hacer login.
 * Los Commands son objetos inmutables que representan una INTENCIÓN del usuario.
 *
 * ¿Por qué usar Commands?
 * - Validación centralizada
 * - Registro de auditoría
 * - Separación entre capa web y aplicación
 */
public record LoginCommand(
      String email,
      String password,
      String captchaToken,
      boolean rememberMe
) {
   /**
    * Validación básica (también se puede hacer con Bean Validation)
    */
   public void validate() {
      if (email == null || email.isBlank()) {
         throw new IllegalArgumentException("Email es requerido");
      }
      if (password == null || password.isBlank()) {
         throw new IllegalArgumentException("Password es requerido");
      }
   }
}