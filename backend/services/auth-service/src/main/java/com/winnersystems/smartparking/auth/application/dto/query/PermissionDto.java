package com.winnersystems.smartparking.auth.application.dto.query;

/**
 * DTO que representa un Permiso para respuestas.
 */
public record PermissionDto(
      Long id,
      String name,
      String description,
      String module
) {
}