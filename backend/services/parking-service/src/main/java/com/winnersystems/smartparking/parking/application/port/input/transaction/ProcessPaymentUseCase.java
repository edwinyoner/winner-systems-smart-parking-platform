package com.winnersystems.smartparking.parking.application.port.input.transaction;

import com.winnersystems.smartparking.parking.application.dto.command.ProcessPaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDetailDto;

/**
 * Puerto de entrada para procesar el PAGO de una transacción de estacionamiento.
 *
 * Responsabilidades:
 * - Validar que la transacción esté COMPLETED y PENDING de pago
 * - Validar que el monto pagado coincida con el total
 * - Validar método de pago y número de referencia (si aplica)
 * - Crear registro de Payment
 * - Actualizar Transaction a paymentStatus PAID
 * - Enviar comprobante digital (WhatsApp + Email) si está configurado
 *
 * Reglas de negocio:
 * - La transacción debe estar COMPLETED
 * - El estado de pago debe ser PENDING
 * - El monto pagado debe ser >= totalAmount
 * - Si el método requiere referencia, debe proporcionarse
 * - Un pago completo marca la transacción como PAID
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ProcessPaymentUseCase {

   /**
    * Procesa el pago de una transacción.
    *
    * @param command datos del pago
    * @return TransactionDetailDto con información del pago registrado
    *
    * @throws com.winnersystems.smartparking.parking.domain.exception.InvalidTransactionStateException
    *         si la transacción no está en estado correcto para pago
    * @throws IllegalArgumentException
    *         si el monto pagado no coincide con el total de la transacción
    */
   TransactionDetailDto processPayment(ProcessPaymentCommand command);
}