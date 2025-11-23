package com.winnersystems.smartparking.auth.application.port.output;

import com.winnersystems.smartparking.auth.domain.model.User;

import java.util.Map;

/**
 * Puerto de salida para operaciones con JWT (JSON Web Tokens).
 * La implementación puede usar jjwt, auth0-jwt, etc.
 */
public interface JwtPort {

   /**
    * Genera un Access Token (JWT) para el usuario
    * @param user usuario autenticado
    * @return token JWT (típicamente dura 15 minutos)
    */
   String generateAccessToken(User user);

   /**
    * Genera un Access Token con claims adicionales
    * @param user usuario autenticado
    * @param additionalClaims claims extra (permisos, roles, etc.)
    * @return token JWT
    */
   String generateAccessToken(User user, Map<String, Object> additionalClaims);

   /**
    * Valida un token JWT
    * @param token token a validar
    * @return true si es válido, false si no
    */
   boolean validateToken(String token);

   /**
    * Extrae el email del usuario del token
    * @param token token JWT
    * @return email del usuario
    */
   String extractEmail(String token);

   /**
    * Extrae el ID del usuario del token
    * @param token token JWT
    * @return ID del usuario
    */
   Long extractUserId(String token);

   /**
    * Extrae todos los claims del token
    * @param token token JWT
    * @return mapa con todos los claims
    */
   Map<String, Object> extractAllClaims(String token);

   /**
    * Verifica si el token ha expirado
    * @param token token JWT
    * @return true si expiró, false si no
    */
   boolean isTokenExpired(String token);
}