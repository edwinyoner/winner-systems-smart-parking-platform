package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission;

import com.winnersystems.smartparking.auth.application.port.output.PermissionPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.mapper.PermissionPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Permission.
 * Implementa PermissionPersistencePort usando JPA.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PermissionPersistenceAdapter implements PermissionPersistencePort {

   private final PermissionRepository permissionRepository;
   private final PermissionPersistenceMapper mapper;

   @Override
   public List<Permission> findAllByIds(Set<Long> ids) {
      return permissionRepository.findAllById(ids).stream()
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<Permission> findAll() {
      return permissionRepository.findAll().stream()
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public Optional<Permission> findById(Long id) {
      return permissionRepository.findById(id)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Permission> findByName(String name) {
      return permissionRepository.findByName(name)
            .filter(entity -> entity.getDeletedAt() == null)  // Excluir eliminados
            .map(mapper::toDomain);
   }

   @Override
   public Permission save(Permission permission) {
      PermissionEntity entity = mapper.toEntity(permission);
      PermissionEntity savedEntity = permissionRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }
}