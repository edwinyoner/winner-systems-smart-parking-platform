package com.winnersystems.smartparking.auth.application.dto.query;

import com.winnersystems.smartparking.auth.domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO que representa un Usuario para respuestas.
 *
 * ¿Por qué NO devolver la entidad User directamente?
 * 1. Seguridad: No exponemos el password
 * 2. Desacoplamiento: La API puede cambiar sin afectar el dominio
 * 3. Optimización: Solo enviamos los datos necesarios
 */
public record UserDto(
      Long id,
      String firstName,
      String lastName,
      String fullName,
      String email,
      String phoneNumber,
      String profilePicture,
      UserStatus status,
      boolean emailVerified,
      Set<RoleDto> roles,
      LocalDateTime lastLoginAt,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
   // Constructor adicional sin roles (lazy loading)
   public UserDto(Long id, String firstName, String lastName, String email,
                  String phoneNumber, String profilePicture, UserStatus status,
                  boolean emailVerified, LocalDateTime lastLoginAt,
                  LocalDateTime createdAt, LocalDateTime updatedAt) {
      this(id, firstName, lastName, firstName + " " + lastName, email,
            phoneNumber, profilePicture, status, emailVerified, Set.of(),
            lastLoginAt, createdAt, updatedAt);
   }
}