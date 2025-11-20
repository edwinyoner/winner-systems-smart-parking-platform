package com.winnersystems.smartparking.auth.domain.enums;

/***
 * Estados posibles de un usuario en el sistema.
 */
public enum UserStatus {

   /***
    * Usuario activo - Puede usar el sistema normalmente
    */
   ACTIVE("Activo", "Usuario activo y operativo"),

   /***
    * Usuario inactivo - No puede acceder al sistema
    */
   INACTIVE("Inactivo", "Usuario desactivado temporalmente"),

   /***
    * Usuario pendiente de verificación - Email no verificado
    */
   PENDING("Pendiente", "Esperando verificación de email"),

   /***
    * Usuario suspendido - Bloqueado por razones administrativas
    */
   SUSPENDED("Suspendido", "Usuario suspendido por violación de políticas");

   private final String displayName;
   private final String description;

   UserStatus(String displayName, String description) {
      this.displayName = displayName;
      this.description = description;
   }

   public String getDisplayName() {
      return displayName;
   }

   public String getDescription() {
      return description;
   }

   /***
    * Verifica si el usuario puede operar en el sistema
    * @return true si el status permite operaciones
    */
   public boolean canOperate() {
      return this == ACTIVE;
   }

   /***
    * Verifica si el usuario está bloqueado
    * @return true si está suspendido o inactivo
    */
   public boolean isBlocked() {
      return this == SUSPENDED || this == INACTIVE;
   }
}