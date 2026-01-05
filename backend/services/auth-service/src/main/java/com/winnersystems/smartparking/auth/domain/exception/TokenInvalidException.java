package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando un token de verificación es inválido.
 *
 * <p>Se lanza en los siguientes casos:</p>
 * <ul>
 *   <li>Token no encontrado en la base de datos</li>
 *   <li>Token ya utilizado previamente</li>
 *   <li>Token no pertenece al usuario especificado</li>
 * </ul>
 *
 * @author Edwin Yoner - Winner Systems
 * @version 1.0
 */
public class TokenInvalidException extends RuntimeException {

   public TokenInvalidException() {
      super("El token de verificación es inválido o no existe");
   }

   public TokenInvalidException(String message) {
      super(message);
   }

   public TokenInvalidException(String message, Throwable cause) {
      super(message, cause);
   }
}