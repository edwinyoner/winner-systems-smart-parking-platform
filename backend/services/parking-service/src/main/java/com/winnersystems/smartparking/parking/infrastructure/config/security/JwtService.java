package com.winnersystems.smartparking.parking.infrastructure.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * Servicio para decodificar y validar JWT tokens.
 * NO genera tokens, solo los valida (eso lo hace auth-service).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
public class JwtService {

   @Value("${jwt.secret}")
   private String secretKey;

   /**
    * Extrae todos los claims del token JWT.
    */
   public Claims extractAllClaims(String token) {
      return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
   }

   /**
    * Extrae el email (subject) del token.
    */
   public String extractEmail(String token) {
      return extractAllClaims(token).getSubject();
   }

   /**
    * Extrae el userId del token.
    */
   public Long extractUserId(String token) {
      Claims claims = extractAllClaims(token);
      return claims.get("userId", Long.class);
   }

   /**
    * Extrae el rol del usuario del token.
    */
   public String extractRole(String token) {
      Claims claims = extractAllClaims(token);
      return claims.get("role", String.class);
   }

   /**
    * Valida si el token está expirado.
    */
   public boolean isTokenExpired(String token) {
      try {
         Date expiration = extractAllClaims(token).getExpiration();
         return expiration.before(new Date());
      } catch (Exception e) {
         return true;
      }
   }

   /**
    * Valida el token JWT.
    */
   public boolean validateToken(String token) {
      try {
         extractAllClaims(token);
         return !isTokenExpired(token);
      } catch (Exception e) {
         return false;
      }
   }

   /**
    * Obtiene la clave de firma del token.
    */
   private SecretKey getSigningKey() {
      byte[] keyBytes = Decoders.BASE64.decode(secretKey);
      return Keys.hmacShaKeyFor(keyBytes);
   }
}