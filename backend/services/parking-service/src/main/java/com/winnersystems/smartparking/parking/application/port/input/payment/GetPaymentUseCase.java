package com.winnersystems.smartparking.parking.application.port.input.payment;

import com.winnersystems.smartparking.parking.application.dto.query.PaymentDto;

/**
 * Puerto de entrada para obtener información de pagos.
 *
 * Responsabilidades:
 * - Buscar pago por ID
 * - Buscar pago por transacción (relación 1:1)
 * - Buscar pago por número de referencia
 *
 * Usado por:
 * - Vista de detalle de pago
 * - Consultas de comprobantes
 * - Verificación de pagos
 * - Auditoría financiera
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetPaymentUseCase {

   /**
    * Obtiene un pago por su ID.
    *
    * @param paymentId ID del pago
    * @return PaymentDto con información del pago
    *
    * @throws IllegalArgumentException si no existe pago con ese ID
    */
   PaymentDto getPaymentById(Long paymentId);

   /**
    * Obtiene el pago asociado a una transacción.
    *
    * Relación 1:1 - Una transacción tiene máximo un pago.
    *
    * @param transactionId ID de la transacción
    * @return PaymentDto si existe, null si la transacción no tiene pago
    */
   PaymentDto getPaymentByTransaction(Long transactionId);

   /**
    * Obtiene un pago por su número de referencia.
    *
    * Útil para:
    * - Búsqueda rápida por operación bancaria
    * - Conciliación de pagos con tarjeta/QR
    *
    * @param referenceNumber número de referencia del pago
    * @return PaymentDto si existe, null si no existe
    */
   PaymentDto getPaymentByReferenceNumber(String referenceNumber);
}