package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para actualizar un permiso existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePermissionRequest {

   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   @Pattern(regexp = "^[a-z]+\\.[a-z]+$", message = "El formato debe ser: modulo.accion (ej: users.create)")
   private String name; // ✅ AGREGADO - Opcional

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   private Boolean status; // true=activo, false=inactivo
}