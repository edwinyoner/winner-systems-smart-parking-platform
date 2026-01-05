package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando se intenta registrar un usuario con un EMAIL
 * que YA EXISTE en el sistema.
 *
 * REGLA DE NEGOCIO: Los emails deben ser ÚNICOS en el sistema.
 *
 * Cuándo se lanza:
 * - Al registrar un nuevo usuario con email duplicado
 * - Al actualizar el email de un usuario a uno que ya existe
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class EmailAlreadyExistsException extends RuntimeException {

   private final String email;

   /**
    * Constructor con email duplicado
    */
   public EmailAlreadyExistsException(String email) {
      super(String.format("El email '%s' ya está registrado en el sistema", email));
      this.email = email;
   }

   /**
    * Constructor con email y mensaje personalizado
    */
   public EmailAlreadyExistsException(String email, String customMessage) {
      super(customMessage);
      this.email = email;
   }

   public String getEmail() {
      return email;
   }
}