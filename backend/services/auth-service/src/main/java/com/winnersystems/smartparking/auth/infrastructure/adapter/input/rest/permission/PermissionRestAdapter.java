package com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission;

import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.PermissionDto;
import com.winnersystems.smartparking.auth.application.service.permission.PermissionService;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.request.CreatePermissionRequest;
import com.winnersystems.smartparking.auth.infrastructure.adapter.input.rest.permission.dto.request.UpdatePermissionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller para gestión de permisos.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
public class PermissionRestAdapter {

   private final PermissionService permissionService;

   /**
    * GET /permissions - Lista permisos con paginación
    */
   @GetMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<PagedResponse<PermissionDto>> listPermissions(
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String module,
         @RequestParam(required = false) Boolean status,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size
   ) {
      PagedResponse<PermissionDto> response = permissionService.listPermissions(search, module, status, page, size);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /permissions/all - Obtener TODOS los permisos activos
    * Usado para poblar selects en formularios de roles
    */
   @GetMapping("/all")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<List<PermissionDto>> getAllPermissions() {
      List<PermissionDto> permissions = permissionService.getAllActivePermissions();
      return ResponseEntity.ok(permissions);
   }

   /**
    * GET /permissions/{id} - Obtener permiso por ID
    */
   @GetMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<PermissionDto> getPermissionById(@PathVariable Long id) {
      PermissionDto permission = permissionService.getPermissionById(id);
      return ResponseEntity.ok(permission);
   }

   /**
    * POST /permissions - Crear nuevo permiso
    */
   @PostMapping
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<PermissionDto> createPermission(@Valid @RequestBody CreatePermissionRequest request) {
      PermissionDto permission = permissionService.createPermission(
            request.getName(),
            request.getDescription(),
            request.getStatus() != null ? request.getStatus() : true
      );
      return ResponseEntity.status(HttpStatus.CREATED).body(permission);
   }

   /**
    * PUT /permissions/{id} - Actualizar permiso
    */
   @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<PermissionDto> updatePermission(
         @PathVariable Long id,
         @Valid @RequestBody UpdatePermissionRequest request
   ) {
      PermissionDto permission = permissionService.updatePermission(
            id,
            request.getName(),
            request.getDescription(),
            request.getStatus()
      );
      return ResponseEntity.ok(permission);
   }

   /**
    * DELETE /permissions/{id} - Eliminar permiso
    */
   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
   public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
      permissionService.deletePermission(id);
      return ResponseEntity.noContent().build();
   }
}