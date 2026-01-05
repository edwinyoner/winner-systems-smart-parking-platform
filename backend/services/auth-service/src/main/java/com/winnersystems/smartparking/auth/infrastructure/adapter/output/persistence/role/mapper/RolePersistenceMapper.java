package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.mapper;

import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper que convierte entre Role (domain) y RoleEntity (infrastructure).
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class RolePersistenceMapper {

   // ======================================================
   // DOMAIN → ENTITY
   // ======================================================
   public RoleEntity toEntity(Role role) {
      if (role == null) return null;

      RoleEntity entity = new RoleEntity();
      entity.setId(role.getId());
      entity.setName(role.getName());
      entity.setDescription(role.getDescription());
      entity.setStatus(role.getStatus());   // Mantener estado real del dominio

      entity.setCreatedAt(role.getCreatedAt());
      entity.setCreatedBy(role.getCreatedBy());
      entity.setUpdatedAt(role.getUpdatedAt());
      entity.setUpdatedBy(role.getUpdatedBy());
      entity.setDeletedAt(role.getDeletedAt());
      entity.setDeletedBy(role.getDeletedBy());

      // Mapear permisos
      if (role.getPermissions() != null) {
         entity.setPermissions(
               role.getPermissions().stream()
                     .map(this::permissionToEntity)
                     .collect(Collectors.toSet())
         );
      }

      return entity;
   }

   // ======================================================
   // ENTITY → DOMAIN
   // ======================================================
   public Role toDomain(RoleEntity entity) {
      if (entity == null) return null;

      Role role = new Role();
      role.setId(entity.getId());
      role.setName(entity.getName());
      role.setDescription(entity.getDescription());

      // IMPORTANTE: respetar estado real del entity (no activar automáticamente)
      if (entity.isStatus()) {
         role.activate();
      } else {
         role.deactivate();
      }

      role.setCreatedAt(entity.getCreatedAt());
      role.setCreatedBy(entity.getCreatedBy());
      role.setUpdatedAt(entity.getUpdatedAt());
      role.setUpdatedBy(entity.getUpdatedBy());
      role.setDeletedAt(entity.getDeletedAt());
      role.setDeletedBy(entity.getDeletedBy());

      // Mapear permisos
      if (entity.getPermissions() != null) {
         role.setPermissions(
               entity.getPermissions().stream()
                     .map(this::permissionToDomain)
                     .collect(Collectors.toSet())
         );
      }

      return role;
   }

   // ======================================================
   // HELPERS: PERMISSION MAPPING
   // ======================================================

   private PermissionEntity permissionToEntity(Permission permission) {
      if (permission == null) return null;

      PermissionEntity entity = new PermissionEntity();
      entity.setId(permission.getId());
      entity.setName(permission.getName());
      entity.setDescription(permission.getDescription());
      entity.setStatus(permission.getStatus());
      return entity;
   }

   private Permission permissionToDomain(PermissionEntity entity) {
      if (entity == null) return null;

      Permission permission = new Permission();
      permission.setId(entity.getId());
      permission.setName(entity.getName());
      permission.setDescription(entity.getDescription());

      // Mantener estado real
      if (entity.isStatus()) {
         permission.activate();
      } else {
         permission.deactivate();
      }

      return permission;
   }
}