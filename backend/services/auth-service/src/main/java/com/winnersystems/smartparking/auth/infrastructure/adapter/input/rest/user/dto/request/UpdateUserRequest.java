package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.domain.enums.UserStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO REST para actualizar un usuario existente.
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

      UserStatus status,

      Set<RoleType> roles
) {
}