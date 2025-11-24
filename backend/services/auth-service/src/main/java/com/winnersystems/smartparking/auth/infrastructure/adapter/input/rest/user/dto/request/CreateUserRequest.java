package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * DTO REST para crear un nuevo usuario.
 */
public record CreateUserRequest(
      @NotBlank(message = "Nombre es requerido")
      @Size(max = 100, message = "Nombre no debe exceder 100 caracteres")
      String firstName,

      @NotBlank(message = "Apellido es requerido")
      @Size(max = 100, message = "Apellido no debe exceder 100 caracteres")
      String lastName,

      @NotBlank(message = "Email es requerido")
      @Email(message = "Email debe ser válido")
      String email,

      @NotBlank(message = "Password es requerido")
      @Size(min = 8, message = "Password debe tener al menos 8 caracteres")
      String password,

      @Size(max = 20, message = "Teléfono no debe exceder 20 caracteres")
      String phoneNumber,

      @NotEmpty(message = "Debe asignar al menos un rol")
      Set<RoleType> roles,

      String captchaToken
) {
}