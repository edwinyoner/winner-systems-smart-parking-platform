package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.entity.InfractionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para InfractionEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface InfractionRepository extends JpaRepository<InfractionEntity, Long> {

   // ========================= FIND ÚNICO =========================

   /**
    * Busca infracción por código único.
    * UK: INFRACTION_CODE
    */
   Optional<InfractionEntity> findByInfractionCode(String infractionCode);

   // ========================= EXISTS =========================

   boolean existsByInfractionCode(String infractionCode);

   // ========================= FIND CON FILTROS (paginado) =========================

   /**
    * Lista todas las infracciones con filtros opcionales.
    * - search: busca en código de infracción
    * - status: "PENDING", "IN_REVIEW", "RESOLVED", "ESCALATED", "ALL"
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE (:status = 'ALL' OR i.status = :status)
      AND (:search IS NULL OR :search = ''
         OR UPPER(i.infractionCode) LIKE UPPER(CONCAT('%', :search, '%')))
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   Page<InfractionEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );

   /**
    * Lista infracciones por vehículo.
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.vehicleId = :vehicleId
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   Page<InfractionEntity> findByVehicleId(
         @Param("vehicleId") Long vehicleId,
         Pageable pageable
   );

   /**
    * Lista infracciones por cliente.
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.customerId = :customerId
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   Page<InfractionEntity> findByCustomerId(
         @Param("customerId") Long customerId,
         Pageable pageable
   );

   /**
    * Lista infracciones por transacción.
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.transactionId = :transactionId
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   List<InfractionEntity> findByTransactionId(@Param("transactionId") Long transactionId);

   /**
    * Lista infracciones con multas pendientes.
    * Condición: fineAmount > 0 AND finePaid = false
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.fineAmount IS NOT NULL
      AND i.fineAmount > 0
      AND i.finePaid = false
      AND i.deletedAt IS NULL
      ORDER BY i.fineDueDate ASC
      """)
   Page<InfractionEntity> findWithPendingFines(Pageable pageable);

   /**
    * Lista infracciones con multas vencidas.
    * Condición: fineDueDate < now AND finePaid = false
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.fineDueDate < :now
      AND i.finePaid = false
      AND i.deletedAt IS NULL
      ORDER BY i.fineDueDate ASC
      """)
   Page<InfractionEntity> findWithOverdueFines(
         @Param("now") LocalDateTime now,
         Pageable pageable
   );

   /**
    * Lista infracciones por rango de fechas de detección.
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.detectedAt BETWEEN :startDate AND :endDate
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   Page<InfractionEntity> findByDetectionDateRange(
         @Param("startDate") LocalDateTime startDate,
         @Param("endDate") LocalDateTime endDate,
         Pageable pageable
   );

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todas las infracciones pendientes.
    */
   @Query("""
      SELECT i FROM InfractionEntity i
      WHERE i.status = 'PENDING'
      AND i.deletedAt IS NULL
      ORDER BY i.detectedAt DESC
      """)
   List<InfractionEntity> findAllPending();

   // ========================= COUNT =========================

   long countByStatus(String status);

   @Query("""
      SELECT COUNT(i) FROM InfractionEntity i
      WHERE i.fineAmount IS NOT NULL
      AND i.fineAmount > 0
      AND i.finePaid = false
      AND i.deletedAt IS NULL
      """)
   long countPendingFines();

   @Query("""
      SELECT COUNT(i) FROM InfractionEntity i
      WHERE i.fineDueDate < :now
      AND i.finePaid = false
      AND i.deletedAt IS NULL
      """)
   long countOverdueFines(@Param("now") LocalDateTime now);
}