package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.response;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * Response DTO para información de usuario.
 */
public record UserResponse(
      Long id,
      String firstName,
      String lastName,
      String fullName,
      String email,
      String phoneNumber,
      String profilePicture,
      boolean status,
      boolean emailVerified,
      Set<RoleInfo> roles,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
   /**
    * Información de rol asignado al usuario.
    *
    * @param name Nombre del rol: "ADMIN", "AUTORIDAD", "OPERADOR"
    * @param displayName Nombre para mostrar: "Administrador"
    * @param description Descripción del rol
    */
   public record RoleInfo(
         String name,          // Sin id
         String displayName,
         String description
   ) {
   }
}