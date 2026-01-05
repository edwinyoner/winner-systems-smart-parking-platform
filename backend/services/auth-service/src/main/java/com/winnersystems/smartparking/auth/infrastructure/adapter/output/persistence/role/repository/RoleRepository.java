package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository de Spring Data JPA para RoleEntity.
 *
 * <p>Spring Data JPA provee automáticamente:</p>
 * <ul>
 *   <li>findById(Long) - Buscar por ID</li>
 *   <li>findAll() - Listar todos</li>
 *   <li>findAllById(Iterable) - Buscar múltiples por IDs</li>
 *   <li>save(Entity) - Guardar o actualizar</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

   /**
    * Busca un rol por su nombre único.
    * Ejemplos: "ADMIN", "AUTORIDAD", "OPERADOR"
    *
    * @param name nombre del rol
    * @return Optional con la entidad si existe
    */
   Optional<RoleEntity> findByName(String name);

}