package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Request para actualizar un rol existente.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRoleRequest {

   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name; // ✅ AGREGADO - Opcional (no se puede cambiar en roles de sistema)

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   private Boolean status; // true=activo, false=inactivo

   private Set<Long> permissionIds; // IDs de permisos a asignar
}