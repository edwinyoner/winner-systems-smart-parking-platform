package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role;

import com.winnersystems.smartparking.auth.application.port.output.RolePersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository.PermissionRepository;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.mapper.RolePersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Role.
 * Implementa RolePersistencePort usando JPA.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class RolePersistenceAdapter implements RolePersistencePort {

   private final RoleRepository roleRepository;
   private final PermissionRepository permissionRepository; // ✅ AGREGAR
   private final RolePersistenceMapper mapper;

   @Override
   public List<Role> findAllByIds(Set<Long> ids) {
      return roleRepository.findAllById(ids).stream()
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<Role> findAll() {
      return roleRepository.findAll().stream()
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public Optional<Role> findById(Long id) {
      return roleRepository.findById(id)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Role> findByName(String name) {
      return roleRepository.findByName(name)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   /**
    * ✅ MÉTODO ACTUALIZADO: Recarga permisos como entidades MANAGED
    */
   @Override
   public Role save(Role role) {
      RoleEntity entity = mapper.toEntity(role);

      // Recargar los permisos desde la BD para que estén MANAGED
      if (entity.getPermissions() != null && !entity.getPermissions().isEmpty()) {
         // Extraer IDs de los permisos
         Set<Long> permissionIds = entity.getPermissions().stream()
               .map(PermissionEntity::getId)
               .collect(Collectors.toSet());

         // Cargar entidades MANAGED desde la BD
         Set<PermissionEntity> managedPermissions = new HashSet<>(
               permissionRepository.findAllById(permissionIds)
         );

         // Reemplazar permisos DETACHED por MANAGED
         entity.setPermissions(managedPermissions);
      }

      RoleEntity savedEntity = roleRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public List<Role> findAllActive() {
      List<RoleEntity> entities = roleRepository.findByStatusTrueAndDeletedAtIsNull();
      return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }
}