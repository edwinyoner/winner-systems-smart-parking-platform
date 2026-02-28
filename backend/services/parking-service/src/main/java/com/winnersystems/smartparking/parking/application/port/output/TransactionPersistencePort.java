package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Transaction;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Transaction.
 *
 * Define las operaciones de persistencia que necesita el dominio
 * sin acoplarse a la tecnología de persistencia específica.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface TransactionPersistencePort {

   // ========================= WRITE =========================

   Transaction save(Transaction transaction);

   void delete(Long id);

   // ========================= FIND ÚNICO =========================

   Optional<Transaction> findById(Long id);

   Optional<Transaction> findActiveByVehicleId(Long vehicleId);

   Optional<Transaction> findActiveByPlateNumber(String plateNumber);

   // ========================= EXISTS / COUNT =========================

   boolean existsActiveByVehicleId(Long vehicleId);

   long countActiveByZoneId(Long zoneId);

   // ========================= LIST ACTIVAS (paginado) =========================

   PageResult<Transaction> findAllActive(PageRequest pageRequest);

   PageResult<Transaction> findActiveByZoneId(Long zoneId, PageRequest pageRequest);

   PageResult<Transaction> searchActiveByPlate(String plateNumber, PageRequest pageRequest);

   PageResult<Transaction> findOverdue(int maxMinutes, PageRequest pageRequest);

   // ========================= LIST HISTÓRICO (paginado) =========================

   PageResult<Transaction> findAll(PageRequest pageRequest);

   PageResult<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                           PageRequest pageRequest);

   PageResult<Transaction> findByStatus(String status, PageRequest pageRequest);

   PageResult<Transaction> findByPaymentStatus(String paymentStatus, PageRequest pageRequest);

   PageResult<Transaction> findByZoneId(Long zoneId, PageRequest pageRequest);

   PageResult<Transaction> searchByPlate(String plateNumber, PageRequest pageRequest);
}