package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando las credenciales (email/password) son INVÁLIDAS.
 * Esto es parte de la lógica de autenticación del dominio.
 *
 * Cuándo se lanza:
 * - Email no existe en el sistema
 * - Contraseña incorrecta
 * - Combinación email/password no coincide
 *
 * SEGURIDAD: Por seguridad, NO debes especificar si el email o la contraseña
 * es la incorrecta. Solo di "credenciales inválidas".
 */
public class InvalidCredentialsException extends RuntimeException {

   /**
    * Constructor por defecto con mensaje genérico
    */
   public InvalidCredentialsException() {
      super("Credenciales inválidas. Verifica tu email y contraseña.");
   }

   /**
    * Constructor con mensaje personalizado
    */
   public InvalidCredentialsException(String message) {
      super(message);
   }

   /**
    * Constructor con mensaje y causa
    */
   public InvalidCredentialsException(String message, Throwable cause) {
      super(message, cause);
   }
}