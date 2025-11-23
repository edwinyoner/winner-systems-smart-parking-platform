package com.winnersystems.smartparking.auth.application.dto.command;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;

import java.util.Set;

/**
 * Command para crear un nuevo usuario.
 */
public record CreateUserCommand(
      String firstName,
      String lastName,
      String email,
      String password,
      String phoneNumber,
      Set<RoleType> roles,      // Roles a asignar
      String captchaToken
) {
   public void validate() {
      if (firstName == null || firstName.isBlank()) {
         throw new IllegalArgumentException("Nombre es requerido");
      }
      if (lastName == null || lastName.isBlank()) {
         throw new IllegalArgumentException("Apellido es requerido");
      }
      if (email == null || email.isBlank()) {
         throw new IllegalArgumentException("Email es requerido");
      }
      if (password == null || password.length() < 8) {
         throw new IllegalArgumentException("Password debe tener al menos 8 caracteres");
      }
      if (roles == null || roles.isEmpty()) {
         throw new IllegalArgumentException("Debe asignar al menos un rol");
      }
   }
}