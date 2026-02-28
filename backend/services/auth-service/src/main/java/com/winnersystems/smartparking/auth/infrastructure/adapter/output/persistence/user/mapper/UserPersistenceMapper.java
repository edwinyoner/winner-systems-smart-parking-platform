package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper;

import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper que convierte entre User (domain) y UserEntity (infrastructure).
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class UserPersistenceMapper {

   // ============================================================
   // DOMAIN → ENTITY
   // ============================================================

   public UserEntity toEntity(User user) {
      if (user == null) return null;

      UserEntity entity = new UserEntity();

      entity.setId(user.getId());
      entity.setFirstName(user.getFirstName());
      entity.setLastName(user.getLastName());
      entity.setEmail(user.getEmail());
      entity.setPassword(user.getPassword());
      entity.setPhoneNumber(user.getPhoneNumber());
      entity.setProfilePicture(user.getProfilePicture());
      entity.setStatus(user.getStatus());
      entity.setEmailVerified(user.isEmailVerified());

      // Auditoría
      entity.setCreatedAt(user.getCreatedAt());
      entity.setCreatedBy(user.getCreatedBy());
      entity.setUpdatedAt(user.getUpdatedAt());
      entity.setUpdatedBy(user.getUpdatedBy());
      entity.setDeletedAt(user.getDeletedAt());
      entity.setDeletedBy(user.getDeletedBy());

      // Roles
      if (user.getRoles() != null) {
         entity.setRoles(
               user.getRoles().stream()
                     .map(this::roleToEntity)
                     .collect(Collectors.toSet())
         );
      }

      return entity;
   }

   // ============================================================
   // ENTITY → DOMAIN
   // ============================================================

   public User toDomain(UserEntity entity) {
      if (entity == null) return null;

      User user = new User();

      user.setId(entity.getId());
      user.setFirstName(entity.getFirstName());
      user.setLastName(entity.getLastName());
      user.setEmail(entity.getEmail());
      user.setPassword(entity.getPassword());
      user.setPhoneNumber(entity.getPhoneNumber());
      user.setProfilePicture(entity.getProfilePicture());

      // Estado - usar métodos de dominio
      if (entity.isStatus()) {
         user.activate();
      } else {
         user.deactivate();
      }

      // Email verificado - usar método de dominio
      if (entity.isEmailVerified()) {
         user.verifyEmail();
      }

      // Auditoría
      user.setCreatedAt(entity.getCreatedAt());
      user.setCreatedBy(entity.getCreatedBy());
      user.setUpdatedAt(entity.getUpdatedAt());
      user.setUpdatedBy(entity.getUpdatedBy());
      user.setDeletedAt(entity.getDeletedAt());
      user.setDeletedBy(entity.getDeletedBy());

      // Roles CON PERMISOS
      if (entity.getRoles() != null) {
         user.setRoles(
               entity.getRoles().stream()
                     .map(this::roleToDomain)
                     .collect(Collectors.toSet())
         );
      }

      return user;
   }

   // ============================================================
   // HELPERS: ROLE MAPPING
   // ============================================================

   private RoleEntity roleToEntity(Role role) {
      if (role == null) return null;

      RoleEntity entity = new RoleEntity();
      entity.setId(role.getId());
      entity.setName(role.getName());
      entity.setDescription(role.getDescription());
      entity.setStatus(role.getStatus());

      return entity;
   }

   private Role roleToDomain(RoleEntity entity) {
      if (entity == null) return null;

      Role role = new Role();
      role.setId(entity.getId());
      role.setName(entity.getName());
      role.setDescription(entity.getDescription());

      // Status - usar método de dominio
      if (entity.isStatus()) {
         role.activate();
      } else {
         role.deactivate();
      }

      // ✅ MAPEAR PERMISOS (AQUÍ ESTABA EL PROBLEMA)
      if (entity.getPermissions() != null) {
         role.setPermissions(
               entity.getPermissions().stream()
                     .map(this::permissionToDomain)
                     .collect(Collectors.toSet())
         );
      }

      return role;
   }

   // ============================================================
   // HELPERS: PERMISSION MAPPING
   // ============================================================

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

      // Status - usar método de dominio
      if (entity.isStatus()) {
         permission.activate();
      } else {
         permission.deactivate();
      }

      return permission;
   }
}