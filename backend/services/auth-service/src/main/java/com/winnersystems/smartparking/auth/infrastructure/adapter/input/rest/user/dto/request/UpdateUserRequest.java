package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO para actualizar un usuario existente.
 *
 * <p>Usado en: PUT /api/users/{id}</p>
 *
 * <p><b>Seguridad:</b></p>
 * <ul>
 *   <li>Solo usuarios con rol ADMIN pueden actualizar usuarios</li>
 *   <li>No se permite cambiar el email (es identificador único)</li>
 *   <li>No se permite cambiar el password (usar endpoint específico)</li>
 * </ul>
 *
 * <p><b>Status válidos:</b></p>
 * <ul>
 *   <li>true - Usuario ACTIVO (puede iniciar sesión)</li>
 *   <li>false - Usuario INACTIVO (bloqueado temporalmente)</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UpdateUserRequest(
      @NotBlank(message = "Nombre es requerido")
      @Size(max = 100, message = "Nombre no debe exceder 100 caracteres")
      String firstName,

      @NotBlank(message = "Apellido es requerido")
      @Size(max = 100, message = "Apellido no debe exceder 100 caracteres")
      String lastName,

      @Size(max = 20, message = "Teléfono no debe exceder 20 caracteres")
      String phoneNumber,

      Boolean status,  // Boolean en lugar de UserStatus enum

      @NotEmpty(message = "Debe asignar al menos un rol")
      Set<String> roles  // tring en lugar de RoleType
) {
}