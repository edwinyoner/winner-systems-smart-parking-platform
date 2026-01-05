package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role;

import com.winnersystems.smartparking.auth.application.port.output.RolePersistencePort;
import com.winnersystems.smartparking.auth.domain.model.Role;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.mapper.RolePersistenceMapper;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

   @Override
   public Role save(Role role) {
      RoleEntity entity = mapper.toEntity(role);
      RoleEntity savedEntity = roleRepository.save(entity);
      return mapper.toDomain(savedEntity);
   }
}