package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.entity.ZoneEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Zone.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface ZoneRepository extends JpaRepository<ZoneEntity, Long> {

   // ========================= BÚSQUEDA POR ID =========================

   Optional<ZoneEntity> findByIdAndDeletedAtIsNull(Long id);

   // ========================= BÚSQUEDA POR CÓDIGO =========================

   Optional<ZoneEntity> findByCodeAndDeletedAtIsNull(String code);

   boolean existsByCodeAndDeletedAtIsNull(String code);

   // ========================= BÚSQUEDA POR NOMBRE =========================

   boolean existsByNameAndDeletedAtIsNull(String name);

   // ========================= LISTA ACTIVOS (sin paginar) =========================

   List<ZoneEntity> findByStatusAndDeletedAtIsNullOrderByNameAsc(String status);

   // ========================= LISTA POR PARKING (sin paginar) =========================

   List<ZoneEntity> findByParkingIdAndDeletedAtIsNull(Long parkingId);   // ← AGREGADO

   // ========================= PAGINACIÓN CON FILTROS =========================

   /**
    * Todas las zonas no eliminadas con filtros opcionales de búsqueda y estado.
    * search filtra por nombre o código (case-insensitive).
    * status filtra por estado exacto. Si es null, retorna todos.
    */
   @Query("SELECT z FROM ZoneEntity z " +
         "WHERE z.deletedAt IS NULL " +
         "AND (:search IS NULL OR UPPER(z.name) LIKE UPPER(CONCAT('%', :search, '%')) " +
         "     OR UPPER(z.code) LIKE UPPER(CONCAT('%', :search, '%'))) " +
         "AND (:status IS NULL OR z.status = :status)")
   Page<ZoneEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );
}