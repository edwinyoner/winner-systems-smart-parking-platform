package com.winnersystems.smartparking.auth.infrastructure.adapter.output.jwt;

import com.winnersystems.smartparking.auth.application.port.output.JwtPort;
import com.winnersystems.smartparking.auth.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Adaptador JWT que implementa JwtPort.
 * Usa la librería io.jsonwebtoken (jjwt) para generar y validar tokens.
 *
 * NOTA: Esta implementación usa jjwt 0.12.3 que tiene una API diferente a versiones anteriores.
 *
 * Dependencias necesarias en pom.xml:
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-api</artifactId>
 *   <version>0.12.3</version>
 * </dependency>
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-impl</artifactId>
 *   <version>0.12.3</version>
 *   <scope>runtime</scope>
 * </dependency>
 * <dependency>
 *   <groupId>io.jsonwebtoken</groupId>
 *   <artifactId>jjwt-jackson</artifactId>
 *   <version>0.12.3</version>
 *   <scope>runtime</scope>
 * </dependency>
 */
@Component
public class JwtAdapter implements JwtPort {

   @Value("${jwt.secret}")
   private String secret;

   @Value("${jwt.expiration}")
   private long expiration; // Milisegundos (ej: 900000 = 15 minutos)

   /**
    * Genera la clave de firma usando el secret
    * IMPORTANTE: El secret debe tener al menos 256 bits (32 caracteres) para HS256
    */
   private SecretKey getSigningKey() {
      return Keys.hmacShaKeyFor(secret.getBytes());
   }

   @Override
   public String generateAccessToken(User user) {
      Map<String, Object> claims = new HashMap<>();
      claims.put("userId", user.getId());
      claims.put("email", user.getEmail());
      claims.put("roles", user.getRoles().stream()
            .map(role -> role.getRoleType().name())
            .toArray());

      return generateAccessToken(user, claims);
   }

   @Override
   public String generateAccessToken(User user, Map<String, Object> additionalClaims) {
      Map<String, Object> claims = new HashMap<>(additionalClaims);
      claims.put("userId", user.getId());
      claims.put("email", user.getEmail());

      return Jwts.builder()
            .claims(claims)
            .subject(user.getEmail())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSigningKey())
            .compact();
   }

   @Override
   public boolean validateToken(String token) {
      try {
         Jwts.parser()
               .verifyWith(getSigningKey())
               .build()
               .parseSignedClaims(token);
         return !isTokenExpired(token);
      } catch (Exception e) {
         return false;
      }
   }

   @Override
   public String extractEmail(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   @Override
   public Long extractUserId(String token) {
      Claims claims = extractAllClaimsInternal(token);
      Object userId = claims.get("userId");
      if (userId instanceof Integer) {
         return ((Integer) userId).longValue();
      }
      return (Long) userId;
   }

   @Override
   public Map<String, Object> extractAllClaims(String token) {
      Claims claims = extractAllClaimsInternal(token);
      return new HashMap<>(claims);
   }

   @Override
   public boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   // ========== HELPER METHODS ==========

   /**
    * Extrae todos los claims del token
    * NOTA: Este método es privado para evitar confusión con el método público del interface
    */
   private Claims extractAllClaimsInternal(String token) {
      return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
   }

   /**
    * Extrae un claim específico usando un resolver
    */
   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaimsInternal(token);
      return claimsResolver.apply(claims);
   }

   /**
    * Extrae la fecha de expiración del token
    */
   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }
}