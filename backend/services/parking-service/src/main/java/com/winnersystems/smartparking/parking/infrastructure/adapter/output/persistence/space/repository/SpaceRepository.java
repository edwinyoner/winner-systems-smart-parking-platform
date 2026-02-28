package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.entity.SpaceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Space.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface SpaceRepository extends JpaRepository<SpaceEntity, Long> {

   // ========================= BÚSQUEDA POR ID =========================

   Optional<SpaceEntity> findByIdAndDeletedAtIsNull(Long id);

   // ========================= BÚSQUEDA POR CÓDIGO =========================

   Optional<SpaceEntity> findByCodeAndDeletedAtIsNull(String code);

   boolean existsByCodeAndDeletedAtIsNull(String code);

   // ========================= LISTA ACTIVOS (sin paginar) =========================

   List<SpaceEntity> findByStatusAndDeletedAtIsNull(String status);

   // ========================= LISTA POR ZONA =========================

   List<SpaceEntity> findByZoneIdAndDeletedAtIsNull(Long zoneId);

   long countByZoneIdAndDeletedAtIsNull(Long zoneId);

   // ========================= PAGINACIÓN CON FILTROS =========================

   /**
    * Todos los espacios no eliminados con filtros opcionales de búsqueda y estado.
    * search filtra por código o descripción (case-insensitive).
    * status filtra por estado exacto. Si es null, retorna todos.
    */
   @Query("SELECT s FROM SpaceEntity s " +
         "WHERE s.deletedAt IS NULL " +
         "AND (:search IS NULL OR UPPER(s.code) LIKE UPPER(CONCAT('%', :search, '%')) " +
         "     OR UPPER(s.description) LIKE UPPER(CONCAT('%', :search, '%'))) " +
         "AND (:status IS NULL OR s.status = :status)")
   Page<SpaceEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );

   // ========================= CONSULTAS OPERACIONALES =========================

   @Query("SELECT s FROM SpaceEntity s " +
         "WHERE s.zoneId = :zoneId AND s.status = 'AVAILABLE' AND s.deletedAt IS NULL")
   List<SpaceEntity> findAvailableByZoneId(@Param("zoneId") Long zoneId);

   @Query("SELECT COUNT(s) FROM SpaceEntity s " +
         "WHERE s.zoneId = :zoneId AND s.status = 'AVAILABLE' AND s.deletedAt IS NULL")
   long countAvailableByZoneId(@Param("zoneId") Long zoneId);

   @Query("SELECT s FROM SpaceEntity s " +
         "WHERE s.zoneId = :zoneId AND s.type = :type AND s.deletedAt IS NULL")
   List<SpaceEntity> findByZoneIdAndType(
         @Param("zoneId") Long zoneId,
         @Param("type") String type
   );
}
