package com.winnersystems.smartparking.auth.application.dto.query;

import java.time.LocalDateTime;

/**
 * DTO que representa un Permiso para respuestas.
 *
 * @param id ID del permiso
 * @param name Código único (ej: "users.create")
 * @param description Descripción legible
 * @param status Estado (activo/inactivo)
 * @param createdAt Fecha creación
 * @param createdBy Usuario creador
 * @param updatedAt Fecha actualización
 * @param updatedBy Usuario actualizador
 * @param deletedAt Fecha eliminación
 * @param deletedBy Usuario eliminador
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record PermissionDto(
      Long id,
      String name,
      String description,
      boolean status,
      LocalDateTime createdAt,
      Long createdBy,
      LocalDateTime updatedAt,
      Long updatedBy,
      LocalDateTime deletedAt,
      Long deletedBy
) {
}