package com.winnersystems.smartparking.auth.application.dto.query;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * DTO de consulta para Usuario.
 * Contiene información completa del usuario para respuestas.
 *
 * @param id ID del usuario
 * @param firstName Nombre
 * @param lastName Apellido
 * @param email Email único
 * @param phoneNumber Teléfono (opcional)
 * @param profilePicture URL foto perfil (opcional)
 * @param status true=activo, false=inactivo
 * @param emailVerified true=verificado, false=pendiente
 * @param roles Conjunto de nombres de roles
 * @param createdAt Fecha creación
 * @param updatedAt Fecha última actualización
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UserDto(
      Long id,
      String firstName,
      String lastName,
      String email,
      String phoneNumber,
      String profilePicture,
      boolean status,
      boolean emailVerified,
      Set<String> roles,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
}