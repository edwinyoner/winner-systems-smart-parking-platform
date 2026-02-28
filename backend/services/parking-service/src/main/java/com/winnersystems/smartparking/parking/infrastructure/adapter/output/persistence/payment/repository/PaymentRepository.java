package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para PaymentEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

   /**
    * Busca pago por ID de transacción.
    */
   Optional<PaymentEntity> findByTransactionId(Long transactionId);

   /**
    * Verifica si existe pago para una transacción.
    */
   boolean existsByTransactionId(Long transactionId);
}