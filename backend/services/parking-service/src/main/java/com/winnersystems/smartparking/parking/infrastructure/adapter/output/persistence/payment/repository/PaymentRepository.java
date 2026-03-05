package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity.PaymentEntity;
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
 * Repositorio JPA para PaymentEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

   // ========================= FIND ÚNICO =========================

   /**
    * Busca pago por ID de transacción (relación 1:1).
    */
   Optional<PaymentEntity> findByTransactionId(Long transactionId);

   /**
    * Busca pago por número de referencia.
    */
   Optional<PaymentEntity> findByReferenceNumber(String referenceNumber);

   // ========================= EXISTS =========================

   boolean existsByTransactionId(Long transactionId);

   boolean existsByReferenceNumber(String referenceNumber);

   // ========================= FIND CON FILTROS (paginado) =========================

   /**
    * Lista todos los pagos con filtro opcional de estado.
    * Si status es "ALL" o null, trae todos.
    */
   @Query("""
        SELECT p FROM PaymentEntity p
        WHERE (:status IS NULL OR :status = 'ALL' OR p.status = :status)
        AND (:search IS NULL OR :search = '' OR UPPER(p.referenceNumber) LIKE UPPER(CONCAT('%', :search, '%')))
        ORDER BY p.paymentDate DESC
        """)
   Page<PaymentEntity> findAllWithFilters(
         @Param("search") String search,
         @Param("status") String status,
         Pageable pageable
   );

   /**
    * Lista pagos por tipo de pago.
    */
   Page<PaymentEntity> findByPaymentTypeId(Long paymentTypeId, Pageable pageable);

   /**
    * Lista pagos por rango de fechas.
    */
   @Query("""
        SELECT p FROM PaymentEntity p
        WHERE p.paymentDate BETWEEN :startDate AND :endDate
        ORDER BY p.paymentDate DESC
        """)
   Page<PaymentEntity> findByDateRange(
         @Param("startDate") LocalDateTime startDate,
         @Param("endDate") LocalDateTime endDate,
         Pageable pageable
   );

   /**
    * Lista pagos con devoluciones (refundAmount > 0).
    */
   @Query("""
        SELECT p FROM PaymentEntity p
        WHERE p.refundAmount IS NOT NULL
        AND p.refundAmount > 0
        ORDER BY p.refundDate DESC
        """)
   Page<PaymentEntity> findWithRefunds(Pageable pageable);

   /**
    * Lista pagos por operador.
    */
   Page<PaymentEntity> findByOperatorId(Long operatorId, Pageable pageable);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los pagos completados.
    */
   @Query("""
        SELECT p FROM PaymentEntity p
        WHERE p.status = 'COMPLETED'
        ORDER BY p.paymentDate DESC
        """)
   List<PaymentEntity> findAllCompleted();

   // ========================= COUNT =========================

   long countByStatus(String status);
}