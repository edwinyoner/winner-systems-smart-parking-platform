package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role;

import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.RoleDto;
import com.winnersystems.smartparking.auth.application.service.role.RoleService;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.request.CreateRoleRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.role.dto.request.UpdateRoleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller para gestión de roles.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleRestAdapter {

   private final RoleService roleService;

   /**
    * GET /roles - Lista roles con paginación
    */
   @GetMapping
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<PagedResponse<RoleDto>> listRoles(
         @RequestParam(required = false) String search,
         @RequestParam(required = false) Boolean status,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size
   ) {
      PagedResponse<RoleDto> response = roleService.listRoles(search, status, page, size);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /roles/all - Obtener TODOS los roles activos
    * Usado para poblar selects en formularios de usuarios
    */
   @GetMapping("/all")
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<List<RoleDto>> getAllRoles() {
      List<RoleDto> roles = roleService.getAllActiveRoles();
      return ResponseEntity.ok(roles);
   }

   /**
    * GET /roles/{id} - Obtener rol por ID
    */
   @GetMapping("/{id}")
   @PreAuthorize("hasAnyRole('ADMIN', 'AUTORIDAD')")
   public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id) {
      RoleDto role = roleService.getRoleById(id);
      return ResponseEntity.ok(role);
   }

   /**
    * POST /roles - Crear nuevo rol
    */
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<RoleDto> createRole(@Valid @RequestBody CreateRoleRequest request) {
      RoleDto role = roleService.createRole(
            request.getName(),
            request.getDescription(),
            request.getStatus() != null ? request.getStatus() : true, // ✅ Default true
            request.getPermissionIds()
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(role);
   }

   /**
    * PUT /roles/{id} - Actualizar rol
    */
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<RoleDto> updateRole(
         @PathVariable Long id,
         @Valid @RequestBody UpdateRoleRequest request
   ) {
      RoleDto role = roleService.updateRole(
            id,
            request.getName(),
            request.getDescription(),
            request.getStatus(),
            request.getPermissionIds()
      );
      return ResponseEntity.ok(role);
   }

   /**
    * DELETE /roles/{id} - Eliminar rol
    */
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
      roleService.deleteRole(id);
      return ResponseEntity.noContent().build();
   }
}