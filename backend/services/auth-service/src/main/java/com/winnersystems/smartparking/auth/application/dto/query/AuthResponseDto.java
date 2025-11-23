package com.winnersystems.smartparking.auth.application.dto.query;

/**
 * DTO de respuesta para operaciones de autenticación (login).
 * Contiene los tokens y la información del usuario.
 */
public record AuthResponseDto(
      String accessToken,
      String refreshToken,
      String tokenType,      // "Bearer"
      long expiresIn,        // Segundos hasta expiración
      UserDto user
) {
   public AuthResponseDto(String accessToken, String refreshToken, long expiresIn, UserDto user) {
      this(accessToken, refreshToken, "Bearer", expiresIn, user);
   }
}