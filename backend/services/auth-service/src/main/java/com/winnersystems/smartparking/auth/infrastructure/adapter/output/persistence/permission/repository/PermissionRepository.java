package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.repository;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository de Spring Data JPA para PermissionEntity.
 */
@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {

   /**
    * Busca un permiso por su nombre (ej: "users.create")
    */
   Optional<PermissionEntity> findByName(String name);

   /**
    * Verifica si existe un permiso con ese nombre
    */
   boolean existsByName(String name);

   /**
    * Lista permisos por módulo (ej: "users", "parking")
    */
   List<PermissionEntity> findByModule(String module);

   /**
    * Busca permisos por nombre parcial (para búsquedas)
    */
   @Query("SELECT p FROM PermissionEntity p WHERE p.name LIKE %:searchTerm% OR p.description LIKE %:searchTerm%")
   List<PermissionEntity> searchByNameOrDescription(@Param("searchTerm") String searchTerm);
}