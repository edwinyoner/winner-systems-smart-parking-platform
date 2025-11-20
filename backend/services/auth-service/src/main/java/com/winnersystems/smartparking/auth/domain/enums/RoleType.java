package com.winnersystems.smartparking.auth.domain.enums;

/***
 * Enumeración de tipos de roles en el sistema Smart Parking.
 * Define los roles principales.
 */
public enum RoleType {

   /***
    * Administrador del sistema - Acceso completo a todas las funcionalidades
    */
   ADMIN("Administrador", "Acceso completo al sistema"),

   /***
    * Autoridad - Supervisión y Reportes
    */
   AUTORIDAD("Autoridad", "Supervisión y generación de reportes"),

   /***
    * Operador de estacionamiento - Gestión Operativa diaria
    */
   OPERADOR("Operador", "Gestión de espacios de estacionamiento y operaciones diarias"),

   /***
    * Usuario final - Búsqueda y reserva de espacios de estacionamiento
    */
   USUARIO("Usuario", "Búsqueda de espacios de estacionamiento");

   private final String displayName;
   private final String description;

   RoleType(String displayName, String description) {
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
    * Verifica si un rol tiene privilegios administrativos
    * @return true si el rol es ADMIN o AUTORIDAD
    */
   public boolean isAdministrative() {
      return this == ADMIN || this == AUTORIDAD;
   }

   /***
    * Verifica si un rol tiene capacidades operativas
    * @return true si el rol es OPERADOR o ADMIN
    */
   public boolean isOperational() {
      return this == OPERADOR || this == ADMIN;
   }

   /***
    * Verifica si un rol es de usuario final
    * @return true si el rol es USUARIO
    */
   public boolean isEndUser() {
      return this == USUARIO;
   }
}