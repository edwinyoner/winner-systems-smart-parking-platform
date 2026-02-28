package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Repositorio Spring Data JPA para TransactionEntity.
 *
 * OPTIMIZADO PARA ORACLE DATABASE
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

   // ========================= TRANSACCIONES ACTIVAS =========================

   @Query("SELECT t FROM TransactionEntity t WHERE t.vehicleId = :vehicleId AND t.status = 'ACTIVE'")
   Optional<TransactionEntity> findActiveByVehicleId(@Param("vehicleId") Long vehicleId);

   @Query("SELECT t FROM TransactionEntity t " +
         "WHERE t.vehicleId IN (SELECT v.id FROM VehicleEntity v WHERE v.licensePlate = :plateNumber) " +
         "AND t.status = 'ACTIVE'")
   Optional<TransactionEntity> findActiveByPlateNumber(@Param("plateNumber") String plateNumber);

   @Query("SELECT t FROM TransactionEntity t WHERE t.status = 'ACTIVE'")
   Page<TransactionEntity> findAllActive(Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t WHERE t.zoneId = :zoneId AND t.status = 'ACTIVE'")
   Page<TransactionEntity> findActiveByZoneId(@Param("zoneId") Long zoneId, Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t " +
         "WHERE t.vehicleId IN (SELECT v.id FROM VehicleEntity v WHERE UPPER(v.licensePlate) LIKE UPPER(CONCAT('%', :plateNumber, '%'))) " +
         "AND t.status = 'ACTIVE'")
   Page<TransactionEntity> searchActiveByPlate(@Param("plateNumber") String plateNumber, Pageable pageable);

   /**
    * Lista transacciones activas que superan el tiempo máximo permitido.
    *
    * Oracle: la resta de TIMESTAMP produce INTERVAL DAY TO SECOND,
    * por lo que EXTRACT(DAY/HOUR/MINUTE FROM interval) es la sintaxis correcta.
    */
   @Query(value = "SELECT * FROM transactions t " +
         "WHERE t.status = 'ACTIVE' " +
         "AND EXTRACT(DAY FROM (CURRENT_TIMESTAMP - t.entry_time)) * 24 * 60 + " +
         "    EXTRACT(HOUR FROM (CURRENT_TIMESTAMP - t.entry_time)) * 60 + " +
         "    EXTRACT(MINUTE FROM (CURRENT_TIMESTAMP - t.entry_time)) > :maxMinutes",
         countQuery = "SELECT COUNT(*) FROM transactions t " +
               "WHERE t.status = 'ACTIVE' " +
               "AND EXTRACT(DAY FROM (CURRENT_TIMESTAMP - t.entry_time)) * 24 * 60 + " +
               "    EXTRACT(HOUR FROM (CURRENT_TIMESTAMP - t.entry_time)) * 60 + " +
               "    EXTRACT(MINUTE FROM (CURRENT_TIMESTAMP - t.entry_time)) > :maxMinutes",
         nativeQuery = true)
   Page<TransactionEntity> findOverdue(@Param("maxMinutes") int maxMinutes, Pageable pageable);

   @Query("SELECT CASE WHEN COUNT(t) > 0 THEN true ELSE false END " +
         "FROM TransactionEntity t WHERE t.vehicleId = :vehicleId AND t.status = 'ACTIVE'")
   boolean existsActiveByVehicleId(@Param("vehicleId") Long vehicleId);

   @Query("SELECT COUNT(t) FROM TransactionEntity t WHERE t.zoneId = :zoneId AND t.status = 'ACTIVE'")
   long countActiveByZoneId(@Param("zoneId") Long zoneId);

   // ========================= HISTORIAL COMPLETO =========================

   /**
    * ✅ FIX: ORDER BY eliminado — el sort lo controla Pageable para evitar
    * conflictos entre ORDER BY explícito y el Sort de Pageable en Hibernate.
    */
   @Query("SELECT t FROM TransactionEntity t " +
         "WHERE t.entryTime >= :startDate AND t.entryTime <= :endDate")
   Page<TransactionEntity> findByDateRange(@Param("startDate") LocalDateTime startDate,
                                           @Param("endDate") LocalDateTime endDate,
                                           Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t WHERE t.status = :status")
   Page<TransactionEntity> findByStatus(@Param("status") String status, Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t WHERE t.paymentStatus = :paymentStatus")
   Page<TransactionEntity> findByPaymentStatus(@Param("paymentStatus") String paymentStatus, Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t WHERE t.zoneId = :zoneId")
   Page<TransactionEntity> findByZoneId(@Param("zoneId") Long zoneId, Pageable pageable);

   @Query("SELECT t FROM TransactionEntity t " +
         "WHERE t.vehicleId IN (SELECT v.id FROM VehicleEntity v WHERE UPPER(v.licensePlate) LIKE UPPER(CONCAT('%', :plateNumber, '%')))")
   Page<TransactionEntity> searchByPlate(@Param("plateNumber") String plateNumber, Pageable pageable);
}