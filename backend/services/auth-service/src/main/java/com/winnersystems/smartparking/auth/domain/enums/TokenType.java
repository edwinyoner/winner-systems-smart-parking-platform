package com.winnersystems.smartparking.auth.domain.enums;

/***
 * Tipos de tokens utilizados en el sistema de autenticación.
 * Cada tipo tiene su propia duración de validez
 */
public enum TokenType {

   /***
    * Token para verificar email al registrarse
    * Duración: 24 horas
    */
   EMAIL_VERIFICATION("Verificación de Email", 24),

   /***
    * Token para restablecer contraseña olvidada
    * Duración: 1 hora
    */
   PASSWORD_RESET("Restablecimiento de Contraseña", 1),

   /***
    * Token para refrescar el access token sin volver hacer login
    * 30 días (720 horas)
    */
   REFRESH_TOKEN("Token de Refresco", 720);

   private final String description;
   private final int validityHours;

   TokenType(String description, int validityHours) {
      this.description = description;
      this.validityHours = validityHours;
   }

   public String getDescription() {
      return description;
   }

   public int getValidityHours() {
      return validityHours;
   }

   /***
    * Obtiene la validez del token en milisegundos
    * @return validez en milisegundos
    */
   public long getValidityMillis() {
      return validityHours * 60L * 60L * 1000L;
   }

   /***
    * Verifica si el token es de corta duración (menos de 2 horas)
    * @return true si es de corta duración
    */
   public boolean isShortLived() {
      return validityHours <= 2;
   }
}