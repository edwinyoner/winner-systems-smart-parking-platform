package com.winnersystems.smartparking.auth.application.dto.query;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;

import java.util.Set;

/**
 * DTO que representa un Rol para respuestas.
 */
public record RoleDto(
      Long id,
      RoleType roleType,
      String displayName,
      String description,
      boolean status,
      Set<PermissionDto> permissions
) {
   // Constructor sin permisos (para lazy loading)
   public RoleDto(Long id, RoleType roleType, String description, boolean status) {
      this(id, roleType, roleType.getDisplayName(), description, status, Set.of());
   }
}