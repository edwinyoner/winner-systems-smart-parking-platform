package com.winnersystems.smartparking.auth.application.port.output;

import java.util.Set;

public interface JwtPort {

   /**
    * Genera un access token JWT.
    */
   String generateAccessToken(Long userId, String email, Set<String> roles, Set<String> permissions);

   /**
    * Extrae el email del token JWT.
    */
   String extractEmail(String token);

   /**
    * Valida si el token JWT es válido (firma correcta, no expirado).
    */
   boolean validateToken(String token);

   /**
    * Extrae el userId del token JWT.
    */
   Long extractUserId(String token);
}