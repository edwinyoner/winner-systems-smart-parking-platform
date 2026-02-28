package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.entity.ParkingEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Parking.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface ParkingRepository extends JpaRepository<ParkingEntity, Long> {

   // ========================= BÚSQUEDA POR ID =========================

   Optional<ParkingEntity> findByIdAndDeletedAtIsNull(Long id);

   // ========================= BÚSQUEDA POR CÓDIGO =========================

   Optional<ParkingEntity> findByCodeAndDeletedAtIsNull(String code);

   boolean existsByCodeAndDeletedAtIsNull(String code);

   // ========================= LISTA ACTIVOS (sin paginar) =========================

   List<ParkingEntity> findByStatusAndDeletedAtIsNull(String status);

   // ========================= PAGINACIÓN CON FILTROS =========================

   /**
    * Todos los parkings no eliminados con filtros opcionales de búsqueda y estado.
    * search filtra por nombre o código (case-insensitive).
    * status filtra por estado exacto. Si es null, retorna todos.
    */
   @Query("SELECT p FROM ParkingEntity p " +
         "WHERE p.deletedAt IS NULL " +
         "AND (:search IS NULL OR UPPER(p.name) LIKE UPPER(CONCAT('%', :search, '%')) " +
         "     OR UPPER(p.code) LIKE UPPER(CONCAT('%', :search, '%'))) " +
         "AND (:status IS NULL OR p.status = :status)")
   Page<ParkingEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );
}
