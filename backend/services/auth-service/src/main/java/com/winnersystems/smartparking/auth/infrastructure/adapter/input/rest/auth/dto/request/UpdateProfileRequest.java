package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO para actualizar perfil.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record UpdateProfileRequest(

      @NotBlank(message = "El nombre es requerido")
      @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
      String firstName,

      @NotBlank(message = "El apellido es requerido")
      @Size(min = 2, max = 100, message = "El apellido debe tener entre 2 y 100 caracteres")
      String lastName,

      @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Formato de teléfono inválido")
      String phoneNumber,

      @Size(max = 500, message = "La URL de la foto no puede exceder 500 caracteres")
      String profilePicture
) {}