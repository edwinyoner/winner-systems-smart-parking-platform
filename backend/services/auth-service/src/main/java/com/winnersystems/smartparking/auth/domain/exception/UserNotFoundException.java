package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando NO se encuentra un usuario en el sistema.
 * Esta es una excepción de DOMINIO, parte de la lógica de negocio.
 *
 * Cuándo se lanza:
 * - Al buscar un usuario por ID y no existe
 * - Al buscar un usuario por email y no existe
 * - Al intentar actualizar/eliminar un usuario inexistente
 */
public class UserNotFoundException extends RuntimeException {

   private final String identifier;

   /**
    * Constructor con identificador genérico (puede ser email, username, etc)
    */
   public UserNotFoundException(String identifier) {
      super(String.format("Usuario no encontrado con identificador: %s", identifier));
      this.identifier = identifier;
   }

   /**
    * Constructor con ID numérico
    */
   public UserNotFoundException(Long userId) {
      super(String.format("Usuario no encontrado con ID: %d", userId));
      this.identifier = userId.toString();
   }

   /**
    * Constructor con mensaje personalizado
    */
   public UserNotFoundException(String identifier, String customMessage) {
      super(customMessage);
      this.identifier = identifier;
   }

   public String getIdentifier() {
      return identifier;
   }
}