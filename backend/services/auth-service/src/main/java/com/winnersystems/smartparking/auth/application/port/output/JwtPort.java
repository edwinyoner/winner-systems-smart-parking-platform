package com.winnersystems.smartparking.auth.application.port.output;

import java.util.Set;

/**
 * Puerto de salida para operaciones JWT.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface JwtPort {

   /**
    * Genera un Access Token JWT con información completa del usuario.
    *
    * @param userId          ID del usuario
    * @param email           Email del usuario
    * @param firstName       Nombre del usuario
    * @param lastName        Apellido del usuario
    * @param phoneNumber     Teléfono del usuario
    * @param profilePicture  URL de foto de perfil
    * @param status          Estado activo/inactivo
    * @param emailVerified   Email verificado o no
    * @param activeRole      ROL CON EL QUE INICIÓ SESIÓN
    * @param roles           Todos los roles del usuario
    * @param permissions     Permisos del usuario
    * @return Access Token JWT
    */
   String generateAccessToken(
         Long userId,
         String email,
         String firstName,
         String lastName,
         String phoneNumber,
         String profilePicture,
         Boolean status,
         Boolean emailVerified,
         String activeRole,
         Set<String> roles,
         Set<String> permissions
   );

   String extractEmail(String token);
   boolean validateToken(String token);
   Long extractUserId(String token);
}