package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando la verificación de CAPTCHA falla.
 * Esto es una REGLA DE SEGURIDAD del dominio para prevenir bots.
 *
 * Cuándo se lanza:
 * - El token de reCAPTCHA es inválido
 * - El token de reCAPTCHA ha expirado
 * - La puntuación de reCAPTCHA v3 es muy baja (posible bot)
 * - Error al comunicarse con Google reCAPTCHA
 *
 * Se usa en:
 * - Registro de usuarios
 * - Login (opcional, si hay muchos intentos fallidos)
 * - Recuperación de contraseña
 */
public class InvalidCaptchaException extends RuntimeException {

   /**
    * Constructor por defecto
    */
   public InvalidCaptchaException() {
      super("Verificación de CAPTCHA inválida. Por favor intenta nuevamente.");
   }

   /**
    * Constructor con mensaje personalizado
    */
   public InvalidCaptchaException(String message) {
      super(message);
   }

   /**
    * Constructor con mensaje y causa
    */
   public InvalidCaptchaException(String message, Throwable cause) {
      super(message, cause);
   }
}