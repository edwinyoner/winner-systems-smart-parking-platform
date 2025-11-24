package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role;

import com.winnersystems.smartparking.auth.application.port.output.RolePersistencePort;
import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.mapper.RolePersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Role.
 * Implementa RolePersistencePort usando JPA.
 */
@Component
public class RolePersistenceAdapter implements RolePersistencePort {

   private final RoleRepository roleRepository;
   private final RolePersistenceMapper mapper;

   public RolePersistenceAdapter(
         RoleRepository roleRepository,
         RolePersistenceMapper mapper) {
      this.roleRepository = roleRepository;
      this.mapper = mapper;
   }

   @Override
   public Role save(Role role) {
      RoleEntity entity = mapper.toEntity(role);
      RoleEntity savedEntity = roleRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   public Optional<Role> findById(Long id) {
      return roleRepository.findById(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Role> findByRoleType(RoleType roleType) {
      return roleRepository.findByRoleType(roleType)
            .map(mapper::toDomain);
   }

   @Override
   public boolean existsByRoleType(RoleType roleType) {
      return roleRepository.existsByRoleType(roleType);
   }

   @Override
   public List<Role> findAll() {
      return roleRepository.findAll().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public List<Role> findActiveRoles() {
      return roleRepository.findActiveRoles().stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public void deleteById(Long id) {
      roleRepository.deleteById(id);
   }
}