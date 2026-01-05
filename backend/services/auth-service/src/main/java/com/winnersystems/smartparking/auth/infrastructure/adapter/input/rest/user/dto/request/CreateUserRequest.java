package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Set;

/**
 * Request DTO para crear un nuevo usuario interno.
 *
 * <p>Usado en: POST /api/users</p>
 *
 * <p><b>Seguridad:</b></p>
 * <ul>
 *   <li>Solo usuarios con rol ADMIN pueden crear usuarios</li>
 *   <li>Password debe cumplir requisitos de complejidad</li>
 *   <li>reCAPTCHA v3 opcional para prevenir automatización</li>
 * </ul>
 *
 * <p><b>Roles válidos:</b></p>
 * <ul>
 *   <li>ADMIN - Acceso completo al sistema</li>
 *   <li>AUTORIDAD - Gestión de zonas y tarifas</li>
 *   <li>OPERADOR - Operación diaria del sistema</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
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
      @Size(max = 255, message = "Email no debe exceder 255 caracteres")
      String email,

      @NotBlank(message = "Password es requerido")
      @Size(min = 8, max = 128, message = "Password debe tener entre 8 y 128 caracteres")
      @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password debe contener: 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial"
      )
      String password,

      @Size(max = 20, message = "Teléfono no debe exceder 20 caracteres")
      String phoneNumber,

      @NotEmpty(message = "Debe asignar al menos un rol")
      Set<String> roles,  // ✅ String en lugar de RoleType

      String captchaToken  // Opcional
) {
}