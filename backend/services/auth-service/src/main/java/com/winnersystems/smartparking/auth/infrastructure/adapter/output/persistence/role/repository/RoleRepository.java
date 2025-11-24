package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Spring Data JPA para RoleEntity.
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

   /**
    * Busca un rol por su tipo (ADMIN, USER, etc.)
    */
   Optional<RoleEntity> findByRoleType(RoleType roleType);

   /**
    * Verifica si existe un rol de cierto tipo
    */
   boolean existsByRoleType(RoleType roleType);

   /**
    * Lista solo roles activos
    */
   @Query("SELECT r FROM RoleEntity r WHERE r.status = true")
   List<RoleEntity> findActiveRoles();
}