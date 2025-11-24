package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.mapper;

import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.domain.model.User;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper que convierte entre User (domain) y UserEntity (infrastructure).
 *
 * ESTE ES EL PUENTE entre las capas:
 * - User (domain): Lógica de negocio, sin JPA
 * - UserEntity (infra): Persistencia en BD, con JPA
 *
 * ¿Por qué separar?
 * - Si cambias de BD (MySQL → MongoDB), solo cambias Entity y Mapper
 * - Domain permanece PURO, sin dependencias de frameworks
 */
@Component
public class UserPersistenceMapper {

   /**
    * Convierte de Domain → Entity (para guardar en BD)
    */
   public UserEntity toEntity(User user) {
      if (user == null) {
         return null;
      }

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
      entity.setDeleted(user.isDeleted());
      entity.setDeletedAt(user.getDeletedAt());
      entity.setLastLoginAt(user.getLastLoginAt());
      entity.setCreatedAt(user.getCreatedAt());
      entity.setUpdatedAt(user.getUpdatedAt());

      // Mapear roles (si existen)
      if (user.getRoles() != null && !user.getRoles().isEmpty()) {
         entity.setRoles(
               user.getRoles().stream()
                     .map(this::roleToEntity)
                     .collect(Collectors.toSet())
         );
      }

      return entity;
   }

   /**
    * Convierte de Entity → Domain (al leer de BD)
    */
   public User toDomain(UserEntity entity) {
      if (entity == null) {
         return null;
      }

      User user = new User();
      user.setId(entity.getId());
      user.setFirstName(entity.getFirstName());
      user.setLastName(entity.getLastName());
      user.setEmail(entity.getEmail());
      user.setPassword(entity.getPassword());
      user.setPhoneNumber(entity.getPhoneNumber());
      user.setProfilePicture(entity.getProfilePicture());
      user.setStatus(entity.getStatus());
      user.setEmailVerified(entity.isEmailVerified());
      user.setDeleted(entity.isDeleted());
      user.setDeletedAt(entity.getDeletedAt());
      user.setLastLoginAt(entity.getLastLoginAt());
      user.setCreatedAt(entity.getCreatedAt());
      user.setUpdatedAt(entity.getUpdatedAt());

      // Mapear roles (si existen y están cargados)
      if (entity.getRoles() != null && !entity.getRoles().isEmpty()) {
         user.setRoles(
               entity.getRoles().stream()
                     .map(this::roleToDomain)
                     .collect(Collectors.toSet())
         );
      }

      return user;
   }

   // ========== HELPER: Mapeo de Role ==========

   private RoleEntity roleToEntity(Role role) {
      if (role == null) return null;

      RoleEntity entity = new RoleEntity();
      entity.setId(role.getId());
      entity.setRoleType(role.getRoleType());
      entity.setDescription(role.getDescription());
      entity.setStatus(role.isStatus());
      // Permisos se mapean aparte si es necesario
      return entity;
   }

   private Role roleToDomain(RoleEntity entity) {
      if (entity == null) return null;

      Role role = new Role();
      role.setId(entity.getId());
      role.setRoleType(entity.getRoleType());
      role.setDescription(entity.getDescription());
      role.setStatus(entity.isStatus());
      // Permisos se mapean aparte si es necesario
      return role;
   }
}