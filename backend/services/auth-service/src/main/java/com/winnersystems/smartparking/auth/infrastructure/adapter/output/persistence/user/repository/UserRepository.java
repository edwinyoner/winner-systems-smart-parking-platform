package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.repository;

import com.winnersystems.smartparking.auth.domain.enums.UserStatus;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Spring Data JPA para UserEntity.
 *
 * ¿Qué es esto?
 * Spring Data JPA genera automáticamente la implementación de estos métodos.
 * NO necesitas escribir SQL, Spring lo hace por ti.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

   /**
    * Busca un usuario por email
    * Spring genera: SELECT * FROM users WHERE email = ?
    */
   Optional<UserEntity> findByEmail(String email);

   /**
    * Verifica si existe un usuario con ese email
    * Spring genera: SELECT COUNT(*) > 0 FROM users WHERE email = ?
    */
   boolean existsByEmail(String email);

   /**
    * Busca usuarios por status
    * Spring genera: SELECT * FROM users WHERE status = ?
    */
   List<UserEntity> findByStatus(UserStatus status);

   /**
    * Busca usuarios activos y NO eliminados
    * Query personalizada con @Query
    */
   @Query("SELECT u FROM UserEntity u WHERE u.status = 'ACTIVE' AND u.deleted = false")
   List<UserEntity> findActiveUsers();

   /**
    * Busca usuarios por rol (usando JOIN)
    */
   @Query("SELECT DISTINCT u FROM UserEntity u JOIN u.roles r WHERE r.roleType = :roleType AND u.deleted = false")
   List<UserEntity> findByRoleType(@Param("roleType") String roleType);

   /**
    * Cuenta usuarios activos
    */
   @Query("SELECT COUNT(u) FROM UserEntity u WHERE u.status = 'ACTIVE' AND u.deleted = false")
   long countActiveUsers();

   /**
    * Busca usuarios NO eliminados (para findAll personalizado)
    */
   @Query("SELECT u FROM UserEntity u WHERE u.deleted = false")
   List<UserEntity> findAllNotDeleted();
}