package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.Payment;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de Payment.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface PaymentPersistencePort {

   /**
    * Guarda un pago (crear o actualizar).
    *
    * @param payment pago a guardar
    * @return pago guardado
    */
   Payment save(Payment payment);

   /**
    * Busca un pago por su ID.
    *
    * @param id ID del pago
    * @return Optional con el pago si existe
    */
   Optional<Payment> findById(Long id);

   /**
    * Busca pago por ID de transacción.
    *
    * @param transactionId ID de la transacción
    * @return Optional con el pago si existe
    */
   Optional<Payment> findByTransactionId(Long transactionId);

   /**
    * Verifica si existe pago para una transacción.
    *
    * @param transactionId ID de la transacción
    * @return true si existe pago
    */
   boolean existsByTransactionId(Long transactionId);
}