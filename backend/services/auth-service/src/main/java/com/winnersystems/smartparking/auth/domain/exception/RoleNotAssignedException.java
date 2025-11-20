package com.winnersystems.smartparking.auth.domain.exception;

/**
 * Excepción lanzada cuando se intenta realizar una acción con un ROL
 * que NO está asignado al usuario.
 *
 * Cuándo se lanza:
 * - Intentar remover un rol que el usuario no tiene
 * - Verificar permisos de un rol no asignado
 * - Operaciones que requieren un rol específico que el usuario no posee
 */
public class RoleNotAssignedException extends RuntimeException {

   private final String roleName;
   private final Long userId;

   /**
    * Constructor con nombre de rol y ID de usuario
    */
   public RoleNotAssignedException(String roleName, Long userId) {
      super(String.format("El rol '%s' no está asignado al usuario con ID: %d",
            roleName, userId));
      this.roleName = roleName;
      this.userId = userId;
   }

   /**
    * Constructor con mensaje genérico
    */
   public RoleNotAssignedException(String message) {
      super(message);
      this.roleName = null;
      this.userId = null;
   }

   /**
    * Constructor con rol, usuario y mensaje personalizado
    */
   public RoleNotAssignedException(String roleName, Long userId, String customMessage) {
      super(customMessage);
      this.roleName = roleName;
      this.userId = userId;
   }

   public String getRoleName() {
      return roleName;
   }

   public Long getUserId() {
      return userId;
   }
}