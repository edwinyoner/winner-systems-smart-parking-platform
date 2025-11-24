package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.mapper;

import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Permission (domain) y PermissionEntity (infrastructure).
 */
@Component
public class PermissionPersistenceMapper {

   /**
    * Convierte de Domain → Entity (para guardar en BD)
    */
   public PermissionEntity toEntity(Permission permission) {
      if (permission == null) {
         return null;
      }

      PermissionEntity entity = new PermissionEntity();
      entity.setId(permission.getId());
      entity.setName(permission.getName());
      entity.setDescription(permission.getDescription());
      entity.setModule(permission.getModule());
      entity.setCreatedAt(permission.getCreatedAt());
      entity.setUpdatedAt(permission.getUpdatedAt());

      return entity;
   }

   /**
    * Convierte de Entity → Domain (al leer de BD)
    */
   public Permission toDomain(PermissionEntity entity) {
      if (entity == null) {
         return null;
      }

      Permission permission = new Permission();
      permission.setId(entity.getId());
      permission.setName(entity.getName());
      permission.setDescription(entity.getDescription());
      permission.setModule(entity.getModule());
      permission.setCreatedAt(entity.getCreatedAt());
      permission.setUpdatedAt(entity.getUpdatedAt());

      return permission;
   }
}