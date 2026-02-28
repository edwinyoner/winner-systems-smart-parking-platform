package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para crear un nuevo permiso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePermissionRequest {

   @NotBlank(message = "El nombre del permiso es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   @Pattern(regexp = "^[a-z]+\\.[a-z]+$", message = "El formato debe ser: modulo.accion (ej: users.create)")
   private String name;

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   private Boolean status; // ✅ AGREGADO - true=activo, false=inactivo (default: true)
}