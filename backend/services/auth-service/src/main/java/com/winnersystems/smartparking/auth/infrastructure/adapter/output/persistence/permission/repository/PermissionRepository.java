package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Spring Data JPA para PermissionEntity.
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
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

   /**
    * Busca un permiso por su nombre único.
    * Ejemplos: "users.create", "parking.update"
    *
    * @param name nombre del permiso
    * @return Optional con la entidad si existe
    */
   Optional<PermissionEntity> findByName(String name);

   /**
    * Busca todos los permisos activos y no eliminados
    */
   List<PermissionEntity> findByStatusTrueAndDeletedAtIsNull();
}