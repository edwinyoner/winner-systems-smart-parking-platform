package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.mapper;

import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper que convierte entre Role (domain) y RoleEntity (infrastructure).
 */
@Component
public class RolePersistenceMapper {

   /**
    * Convierte de Domain → Entity (para guardar en BD)
    */
   public RoleEntity toEntity(Role role) {
      if (role == null) {
         return null;
      }

      RoleEntity entity = new RoleEntity();
      entity.setId(role.getId());
      entity.setRoleType(role.getRoleType());
      entity.setDescription(role.getDescription());
      entity.setStatus(role.isStatus());
      entity.setCreatedAt(role.getCreatedAt());
      entity.setUpdatedAt(role.getUpdatedAt());

      // Mapear permisos (si existen)
      if (role.getPermissions() != null && !role.getPermissions().isEmpty()) {
         entity.setPermissions(
               role.getPermissions().stream()
                     .map(this::permissionToEntity)
                     .collect(Collectors.toSet())
         );
      }

      return entity;
   }

   /**
    * Convierte de Entity → Domain (al leer de BD)
    */
   public Role toDomain(RoleEntity entity) {
      if (entity == null) {
         return null;
      }

      Role role = new Role();
      role.setId(entity.getId());
      role.setRoleType(entity.getRoleType());
      role.setDescription(entity.getDescription());
      role.setStatus(entity.isStatus());
      role.setCreatedAt(entity.getCreatedAt());
      role.setUpdatedAt(entity.getUpdatedAt());

      // Mapear permisos (si existen y están cargados)
      if (entity.getPermissions() != null && !entity.getPermissions().isEmpty()) {
         role.setPermissions(
               entity.getPermissions().stream()
                     .map(this::permissionToDomain)
                     .collect(Collectors.toSet())
         );
      }

      return role;
   }

   // ========== HELPER: Mapeo de Permission ==========

   private PermissionEntity permissionToEntity(Permission permission) {
      if (permission == null) return null;

      PermissionEntity entity = new PermissionEntity();
      entity.setId(permission.getId());
      entity.setName(permission.getName());
      entity.setDescription(permission.getDescription());
      entity.setModule(permission.getModule());
      return entity;
   }

   private Permission permissionToDomain(PermissionEntity entity) {
      if (entity == null) return null;

      Permission permission = new Permission();
      permission.setId(entity.getId());
      permission.setName(entity.getName());
      permission.setDescription(entity.getDescription());
      permission.setModule(entity.getModule());
      return permission;
   }
}