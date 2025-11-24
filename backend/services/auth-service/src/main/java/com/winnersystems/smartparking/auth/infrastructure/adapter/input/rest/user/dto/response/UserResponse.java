package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO REST para respuesta de usuario.
 */
public record UserResponse(
      Long id,
      String firstName,
      String lastName,
      String fullName,
      String email,
      String phoneNumber,
      String profilePicture,
      String status,
      boolean emailVerified,
      Set<RoleInfo> roles,
      LocalDateTime lastLoginAt,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
   /**
    * Record interno para información del rol
    */
   public record RoleInfo(
         Long id,
         String roleType,
         String displayName,
         String description
   ) {
   }
}