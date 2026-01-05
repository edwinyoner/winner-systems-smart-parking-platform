package com.winnersystems.smartparking.auth.infrastructure.adapter.output.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

/**
 * Servicio JWT para Infrastructure Layer.
 * Usado por JwtAuthenticationFilter para validar y extraer claims de tokens.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
public class JwtService {

   @Value("${jwt.secret}")
   private String secret;

   private SecretKey getSigningKey() {
      return Keys.hmacShaKeyFor(secret.getBytes());
   }

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

   public String extractEmail(String token) {
      return extractClaim(token, Claims::getSubject);
   }

   public Long extractUserId(String token) {
      Claims claims = extractAllClaims(token);
      Object userId = claims.get("userId");
      if (userId instanceof Integer) {
         return ((Integer) userId).longValue();
      }
      return (Long) userId;
   }

   @SuppressWarnings("unchecked")
   public Set<String> extractRoles(String token) {
      return Optional.ofNullable(extractAllClaims(token).get("roles"))
            .map(roles -> (Set<String>) roles)
            .orElse(Set.of());
   }


   @SuppressWarnings("unchecked")
   public Set<String> extractPermissions(String token) {
      Claims claims = extractAllClaims(token);
      Object permissions = claims.get("permissions");
      return permissions != null ? (Set<String>) permissions : Set.of();
   }

   public boolean isTokenExpired(String token) {
      return extractExpiration(token).before(new Date());
   }

   // ========== HELPER METHODS ==========

   private Claims extractAllClaims(String token) {
      return Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
   }

   private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
      final Claims claims = extractAllClaims(token);
      return claimsResolver.apply(claims);
   }

   private Date extractExpiration(String token) {
      return extractClaim(token, Claims::getExpiration);
   }
}