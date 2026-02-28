package com.winnersystems.smartparking.auth.application.service.role;

import com.winnersystems.smartparking.auth.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.auth.application.dto.query.PermissionDto;
import com.winnersystems.smartparking.auth.application.dto.query.RoleDto;
import com.winnersystems.smartparking.auth.application.port.output.PermissionPersistencePort;
import com.winnersystems.smartparking.auth.application.port.output.RolePersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.domain.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación para gestión de roles.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {

   private final RolePersistencePort rolePersistencePort;
   private final PermissionPersistencePort permissionPersistencePort;

   /**
    * Lista roles con paginación y filtros
    */
   public PagedResponse<RoleDto> listRoles(String search, Boolean status, int page, int size) {
      // Obtener todos los roles activos
      List<Role> roles = rolePersistencePort.findAllActive();

      // Filtrar por búsqueda si existe
      if (search != null && !search.isBlank()) {
         String searchLower = search.toLowerCase();
         roles = roles.stream()
               .filter(role ->
                     role.getName().toLowerCase().contains(searchLower) ||
                           (role.getDescription() != null && role.getDescription().toLowerCase().contains(searchLower))
               )
               .collect(Collectors.toList());
      }

      // Filtrar por estado si existe
      if (status != null) {
         roles = roles.stream()
               .filter(role -> role.isActive() == status)
               .collect(Collectors.toList());
      }

      // Calcular paginación manual
      int start = page * size;
      int end = Math.min(start + size, roles.size());

      // Validar que start no sea mayor que el tamaño de la lista
      if (start >= roles.size()) {
         return PagedResponse.of(List.of(), page, size, roles.size());
      }

      List<Role> paginatedRoles = roles.subList(start, end);

      // Mapear a DTOs
      List<RoleDto> roleDtos = paginatedRoles.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());

      // Crear respuesta paginada usando el método estático
      return PagedResponse.of(roleDtos, page, size, roles.size());
   }

   /**
    * Obtiene un rol por ID
    */
   public RoleDto getRoleById(Long id) {
      Role role = rolePersistencePort.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));
      return mapToDto(role);
   }

   /**
    * Crea un nuevo rol
    */
   public RoleDto createRole(String name, String description, boolean status, Set<Long> permissionIds) {
      // Cargar permisos
      Set<Permission> permissions = new HashSet<>();
      if (permissionIds != null && !permissionIds.isEmpty()) {
         for (Long permissionId : permissionIds) {
            permissionPersistencePort.findById(permissionId)
                  .ifPresent(permissions::add);
         }
      }

      // Crear rol
      Role role = new Role(name, description);
      if (status) {
         role.activate();
      } else {
         role.deactivate();
      }

      // Asignar permisos
      permissions.forEach(role::addPermission);

      // Guardar
      Role savedRole = rolePersistencePort.save(role);
      return mapToDto(savedRole);
   }

   /**
    * Actualiza un rol existente
    */
   public RoleDto updateRole(Long id, String name, String description, Boolean status, Set<Long> permissionIds) {
      Role role = rolePersistencePort.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));

      // Actualizar campos
      if (name != null && description != null && !role.isSystemRole()) {
         role.updateDetails(name, description);
      }

      if (status != null) {
         if (status) {
            role.activate();
         } else {
            role.deactivate();
         }
      }

      // Actualizar permisos si se proporcionaron
      if (permissionIds != null) {
         role.clearPermissions();
         Set<Permission> permissions = new HashSet<>();
         for (Long permissionId : permissionIds) {
            permissionPersistencePort.findById(permissionId)
                  .ifPresent(permissions::add);
         }
         permissions.forEach(role::addPermission);
      }

      Role updatedRole = rolePersistencePort.save(role);
      return mapToDto(updatedRole);
   }

   /**
    * Elimina un rol
    */
   public void deleteRole(Long id) {
      Role role = rolePersistencePort.findById(id)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + id));

      if (role.isSystemRole()) {
         throw new RuntimeException("No se pueden eliminar roles de sistema");
      }

      role.markAsDeleted(null); // TODO: Obtener ID del usuario autenticado
      rolePersistencePort.save(role);
   }

   /**
    * Mapea Role (domain) a RoleDto
    */
   private RoleDto mapToDto(Role role) {
      Set<PermissionDto> permissionDtos = role.getPermissions().stream()
            .map(p -> new PermissionDto(
                  p.getId(),
                  p.getName(),
                  p.getDescription(),
                  p.isActive(),
                  p.getCreatedAt(),
                  p.getCreatedBy(),
                  p.getUpdatedAt(),
                  p.getUpdatedBy(),
                  p.getDeletedAt(),
                  p.getDeletedBy()
            ))
            .collect(Collectors.toSet());

      return new RoleDto(
            role.getId(),
            role.getName(),
            role.getDescription(),
            role.isActive(),
            permissionDtos,
            role.getCreatedAt(),
            role.getCreatedBy(),
            role.getUpdatedAt(),
            role.getUpdatedBy(),
            role.getDeletedAt(),
            role.getDeletedBy()
      );
   }

   /**
    * Obtiene todos los roles activos sin paginación
    * Usado para selects en formularios
    */
   public List<RoleDto> getAllActiveRoles() {
      List<Role> roles = rolePersistencePort.findAllActive();

      return roles.stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
   }
}