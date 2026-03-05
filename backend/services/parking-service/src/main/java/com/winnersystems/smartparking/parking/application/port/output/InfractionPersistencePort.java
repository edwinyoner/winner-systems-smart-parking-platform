package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Infraction;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Infraction.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface InfractionPersistencePort {

   // ========================= WRITE =========================

   Infraction save(Infraction infraction);

   void delete(Long id);

   // ========================= FIND ÚNICO =========================

   Optional<Infraction> findById(Long id);

   /**
    * Busca infracción por código único.
    *
    * @param infractionCode código de infracción (ej: "INF-2026-001234")
    * @return Optional con la infracción si existe
    */
   Optional<Infraction> findByCode(String infractionCode);

   // ========================= EXISTS =========================

   boolean existsByCode(String infractionCode);

   // ========================= LIST (paginado) =========================

   /**
    * Lista todas las infracciones con búsqueda por código y filtro de estado.
    *
    * @param pageRequest parámetros de paginación
    * @param search búsqueda en código de infracción (opcional)
    * @param status filtro: "PENDING", "IN_REVIEW", "RESOLVED", "ESCALATED", "ALL" (opcional)
    * @return PageResult con infracciones
    */
   PageResult<Infraction> findAll(PageRequest pageRequest, String search, String status);

   /**
    * Lista infracciones por vehículo.
    *
    * @param vehicleId ID del vehículo
    * @param pageRequest parámetros de paginación
    * @return PageResult con infracciones del vehículo
    */
   PageResult<Infraction> findByVehicle(Long vehicleId, PageRequest pageRequest);

   /**
    * Lista infracciones por cliente.
    *
    * @param customerId ID del cliente
    * @param pageRequest parámetros de paginación
    * @return PageResult con infracciones del cliente
    */
   PageResult<Infraction> findByCustomer(Long customerId, PageRequest pageRequest);

   /**
    * Lista infracciones por transacción.
    *
    * @param transactionId ID de la transacción
    * @return Lista de infracciones asociadas a la transacción
    */
   List<Infraction> findByTransaction(Long transactionId);

   /**
    * Lista infracciones con multas pendientes (fineAmount > 0 AND finePaid = false).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con infracciones con multas pendientes
    */
   PageResult<Infraction> findWithPendingFines(PageRequest pageRequest);

   /**
    * Lista infracciones con multas vencidas (fineDueDate < now AND finePaid = false).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con infracciones con multas vencidas
    */
   PageResult<Infraction> findWithOverdueFines(PageRequest pageRequest);

   /**
    * Lista infracciones por rango de fechas de detección.
    *
    * @param startDate fecha inicio
    * @param endDate fecha fin
    * @param pageRequest parámetros de paginación
    * @return PageResult con infracciones detectadas en el rango
    */
   PageResult<Infraction> findByDetectionDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                   PageRequest pageRequest);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todas las infracciones pendientes (status = PENDING).
    *
    * @return Lista completa de infracciones pendientes
    */
   List<Infraction> findAllPending();

   // ========================= COUNT =========================

   long count();

   long countByStatus(String status);

   long countPendingFines();

   long countOverdueFines();
}