package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.response;

/**
 * DTO REST para la respuesta de login.
 * Esto es lo que el cliente (Angular/Flutter) recibe.
 */
public record LoginResponse(
      String accessToken,
      String refreshToken,
      String tokenType,
      long expiresIn,
      UserInfo user
) {
   /**
    * Record interno para información del usuario
    */
   public record UserInfo(
         Long id,
         String firstName,
         String lastName,
         String fullName,
         String email,
         String profilePicture,
         String status
   ) {
   }
}