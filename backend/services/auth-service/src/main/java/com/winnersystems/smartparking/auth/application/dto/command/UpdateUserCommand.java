package com.winnersystems.smartparking.auth.application.dto.command;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.domain.enums.UserStatus;

import java.util.Set;

/**
 * Command para actualizar un usuario existente.
 */
public record UpdateUserCommand(
      Long userId,
      String firstName,
      String lastName,
      String phoneNumber,
      UserStatus status,
      Set<RoleType> roles
) {
   public void validate() {
      if (userId == null) {
         throw new IllegalArgumentException("ID de usuario es requerido");
      }
      if (firstName == null || firstName.isBlank()) {
         throw new IllegalArgumentException("Nombre es requerido");
      }
      if (lastName == null || lastName.isBlank()) {
         throw new IllegalArgumentException("Apellido es requerido");
      }
   }
}