package com.winnersystems.smartparking.auth.infrastructure.adapter.output.jwt;

import com.winnersystems.smartparking.auth.application.port.output.JwtPort;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Component
public class JwtAdapter implements JwtPort {

   private final SecretKey secretKey;
   private final long accessTokenExpiration;
   private final long refreshTokenExpiration;
   private final long refreshTokenExpirationRememberMe;

   public JwtAdapter(
         @Value("${jwt.secret}") String secret,
         @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
         @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration,
         @Value("${jwt.refresh-token-expiration-remember-me}") long refreshTokenExpirationRememberMe) {

      byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
      if (keyBytes.length < 32) {
         keyBytes = Base64.getUrlDecoder().decode(secret);
      }
      this.secretKey = Keys.hmacShaKeyFor(keyBytes);

      this.accessTokenExpiration = accessTokenExpiration;
      this.refreshTokenExpiration = refreshTokenExpiration;
      this.refreshTokenExpirationRememberMe = refreshTokenExpirationRememberMe;
   }

   @Override
   public String generateAccessToken(
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
         Set<String> permissions) {

      Map<String, Object> claims = new HashMap<>();

      claims.put("userId", userId);
      claims.put("email", email);
      claims.put("firstName", firstName);
      claims.put("lastName", lastName);
      claims.put("phoneNumber", phoneNumber);
      claims.put("profilePicture", profilePicture);
      claims.put("status", status);
      claims.put("emailVerified", emailVerified);

      // ROL ACTIVO (con el que inició sesión)
      claims.put("activeRole", activeRole);

      // TODOS LOS ROLES
      claims.put("roles", roles);
      claims.put("permissions", permissions);

      return Jwts.builder()
            .claims(claims)
            .subject(email)
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
            .signWith(secretKey)
            .compact();
   }

   @Override
   public String extractEmail(String token) {
      return extractClaims(token).getSubject();
   }

   @Override
   public Long extractUserId(String token) {
      return extractClaims(token).get("userId", Long.class);
   }

   @Override
   public boolean validateToken(String token) {
      try {
         extractClaims(token);
         return true;
      } catch (Exception e) {
         return false;
      }
   }

   private Claims extractClaims(String token) {
      return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
   }
}