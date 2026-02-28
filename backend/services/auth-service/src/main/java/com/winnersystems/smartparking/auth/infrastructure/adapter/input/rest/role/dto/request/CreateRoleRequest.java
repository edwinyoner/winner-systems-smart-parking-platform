package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Request para crear un nuevo rol.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRoleRequest {

   @NotBlank(message = "El nombre del rol es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   private Boolean status; // ✅ AGREGADO - true=activo, false=inactivo (default: true)

   private Set<Long> permissionIds; // IDs de permisos a asignar
}