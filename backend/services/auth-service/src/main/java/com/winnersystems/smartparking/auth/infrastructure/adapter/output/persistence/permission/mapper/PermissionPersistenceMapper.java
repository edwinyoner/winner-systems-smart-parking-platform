package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.mapper;

import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper que convierte entre Permission (domain) y PermissionEntity (infrastructure).
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
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
      entity.setStatus(permission.getStatus());
      entity.setCreatedAt(permission.getCreatedAt());
      entity.setCreatedBy(permission.getCreatedBy());
      entity.setUpdatedAt(permission.getUpdatedAt());
      entity.setUpdatedBy(permission.getUpdatedBy());
      entity.setDeletedAt(permission.getDeletedAt());
      entity.setDeletedBy(permission.getDeletedBy());

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
      permission.setCreatedAt(entity.getCreatedAt());
      permission.setCreatedBy(entity.getCreatedBy());
      permission.setUpdatedAt(entity.getUpdatedAt());
      permission.setUpdatedBy(entity.getUpdatedBy());
      permission.setDeletedAt(entity.getDeletedAt());
      permission.setDeletedBy(entity.getDeletedBy());

      // Restaurar el estado usando las reglas del dominio
      if (entity.isStatus()) {
         permission.activate();
      } else {
         permission.deactivate();
      }

      return permission;
   }
}