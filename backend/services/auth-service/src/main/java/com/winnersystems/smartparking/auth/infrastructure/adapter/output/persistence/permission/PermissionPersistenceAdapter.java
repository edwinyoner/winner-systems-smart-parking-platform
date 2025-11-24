package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission;

import com.winnersystems.smartparking.auth.application.port.output.PermissionPersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Permission;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.mapper.PermissionPersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository.PermissionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Permission.
 * Implementa PermissionPersistencePort usando JPA.
 */
@Component
public class PermissionPersistenceAdapter implements PermissionPersistencePort {

   private final PermissionRepository permissionRepository;
   private final PermissionPersistenceMapper mapper;

   public PermissionPersistenceAdapter(
         PermissionRepository permissionRepository,
         PermissionPersistenceMapper mapper) {
      this.permissionRepository = permissionRepository;
      this.mapper = mapper;
   }

   @Override
   public Permission save(Permission permission) {
      PermissionEntity entity = mapper.toEntity(permission);
      PermissionEntity savedEntity = permissionRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<Permission> findById(Long id) {
      return permissionRepository.findById(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Permission> findByName(String name) {
      return permissionRepository.findByName(name)
            .map(mapper::toDomain);
   }

   @Override
   public boolean existsByName(String name) {
      return permissionRepository.existsByName(name);
   }

   @Override
   public List<Permission> findAll() {
      return permissionRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<Permission> findByModule(String module) {
      return permissionRepository.findByModule(module).stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public void deleteById(Long id) {
      permissionRepository.deleteById(id);
   }
}