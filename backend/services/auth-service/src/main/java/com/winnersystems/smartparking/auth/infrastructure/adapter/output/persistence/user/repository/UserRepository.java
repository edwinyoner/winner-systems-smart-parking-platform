package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository de Spring Data JPA para UserEntity.
 *
 * <p>Spring Data JPA provee automáticamente:</p>
 * <ul>
 *   <li>findById(Long) - Buscar por ID</li>
 *   <li>save(Entity) - Guardar o actualizar</li>
 * </ul>
 *
 * <p>JpaSpecificationExecutor provee:</p>
 * <ul>
 *   <li>findAll(Specification, Pageable) - Búsquedas complejas con paginación</li>
 *   <li>count(Specification) - Contar con criterios</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>,
      JpaSpecificationExecutor<UserEntity> {

   /**
    * Busca un usuario por su email único.
    *
    * <p>Usado en:</p>
    * <ul>
    *   <li>LoginUseCase - Autenticación</li>
    *   <li>CreateUserUseCase - Validar email único</li>
    *   <li>ForgotPasswordUseCase - Buscar usuario para reset</li>
    * </ul>
    *
    * @param email email del usuario
    * @return Optional con la entidad si existe
    */
   @EntityGraph(attributePaths = {"roles", "roles.permissions"})
   Optional<UserEntity> findByEmail(String email);

   /**
    * Verifica si existe un usuario con el email especificado.
    *
    * @param email email a verificar
    * @return true si existe, false si no
    */
   boolean existsByEmail(String email);

}