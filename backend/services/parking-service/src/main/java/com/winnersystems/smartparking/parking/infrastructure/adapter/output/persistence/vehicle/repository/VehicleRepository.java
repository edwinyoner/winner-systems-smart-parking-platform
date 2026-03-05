package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para VehicleEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

   // ========================= FIND ÚNICO =========================

   /**
    * Busca vehículo por placa.
    * UK: LICENSE_PLATE
    */
   Optional<VehicleEntity> findByLicensePlate(String licensePlate);

   // ========================= EXISTS =========================

   boolean existsByLicensePlate(String licensePlate);

   // ========================= FIND CON FILTROS (paginado) =========================

   /**
    * Lista todos los vehículos con filtros opcionales.
    * - search: busca en placa
    * - status: "ACTIVE" (deletedAt IS NULL), "DELETED" (deletedAt IS NOT NULL), "ALL"
    */
   @Query("""
      SELECT v FROM VehicleEntity v
      WHERE (:status = 'ALL'
         OR (:status = 'ACTIVE' AND v.deletedAt IS NULL)
         OR (:status = 'DELETED' AND v.deletedAt IS NOT NULL))
      AND (:search IS NULL OR :search = ''
         OR UPPER(v.licensePlate) LIKE UPPER(CONCAT('%', :search, '%')))
      ORDER BY v.createdAt DESC
      """)
   Page<VehicleEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );

   /**
    * Lista vehículos recurrentes (totalVisits > 1).
    */
   @Query("""
      SELECT v FROM VehicleEntity v
      WHERE v.deletedAt IS NULL
      AND v.totalVisits > 1
      ORDER BY v.totalVisits DESC
      """)
   Page<VehicleEntity> findRecurrent(Pageable pageable);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los vehículos activos (no eliminados).
    */
   @Query("""
      SELECT v FROM VehicleEntity v
      WHERE v.deletedAt IS NULL
      ORDER BY v.createdAt DESC
      """)
   List<VehicleEntity> findAllActive();

   // ========================= COUNT =========================

   @Query("""
      SELECT COUNT(v) FROM VehicleEntity v
      WHERE v.deletedAt IS NULL
      """)
   long countActive();
}