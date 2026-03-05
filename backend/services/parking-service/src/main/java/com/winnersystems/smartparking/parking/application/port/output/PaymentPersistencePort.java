package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Payment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Payment.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface PaymentPersistencePort {

   // ========================= WRITE =========================

   Payment save(Payment payment);

   // No tiene delete - histórico permanente

   // ========================= FIND ÚNICO =========================

   Optional<Payment> findById(Long id);

   /**
    * Busca pago por ID de transacción (relación 1:1).
    *
    * @param transactionId ID de la transacción
    * @return Optional con el pago si existe
    */
   Optional<Payment> findByTransactionId(Long transactionId);

   /**
    * Busca pago por número de referencia.
    *
    * @param referenceNumber número de referencia del pago
    * @return Optional con el pago si existe
    */
   Optional<Payment> findByReferenceNumber(String referenceNumber);

   // ========================= EXISTS =========================

   boolean existsByTransactionId(Long transactionId);

   boolean existsByReferenceNumber(String referenceNumber);

   // ========================= LIST (paginado) =========================

   /**
    * Lista todos los pagos con búsqueda por referencia y filtro de estado.
    *
    * @param pageRequest parámetros de paginación
    * @param search búsqueda en número de referencia (opcional)
    * @param status filtro: "COMPLETED", "REFUNDED", "CANCELLED", "ALL" (opcional)
    * @return PageResult con pagos
    */
   PageResult<Payment> findAll(PageRequest pageRequest, String search, String status);

   /**
    * Lista pagos por tipo de pago.
    *
    * @param paymentTypeId ID del tipo de pago
    * @param pageRequest parámetros de paginación
    * @return PageResult con pagos del tipo especificado
    */
   PageResult<Payment> findByPaymentType(Long paymentTypeId, PageRequest pageRequest);

   /**
    * Lista pagos por rango de fechas.
    *
    * @param startDate fecha inicio
    * @param endDate fecha fin
    * @param pageRequest parámetros de paginación
    * @return PageResult con pagos en el rango
    */
   PageResult<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                       PageRequest pageRequest);

   /**
    * Lista pagos con devoluciones (refundAmount > 0).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con pagos que tienen refund
    */
   PageResult<Payment> findWithRefunds(PageRequest pageRequest);

   /**
    * Lista pagos por operador.
    *
    * @param operatorId ID del operador
    * @param pageRequest parámetros de paginación
    * @return PageResult con pagos del operador
    */
   PageResult<Payment> findByOperator(Long operatorId, PageRequest pageRequest);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los pagos completados.
    *
    * @return Lista completa de pagos con status COMPLETED
    */
   List<Payment> findAllCompleted();

   // ========================= COUNT =========================

   long count();

   long countByStatus(String status);
}