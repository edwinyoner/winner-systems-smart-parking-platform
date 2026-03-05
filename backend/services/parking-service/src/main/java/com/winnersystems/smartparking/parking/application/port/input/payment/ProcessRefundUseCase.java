package com.winnersystems.smartparking.parking.application.port.input.payment;

import com.winnersystems.smartparking.parking.application.dto.command.RefundPaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentDto;

/**
 * Puerto de entrada para procesar devoluciones (refunds) de pagos.
 *
 * Responsabilidades:
 * - Validar que el pago exista y esté COMPLETED
 * - Procesar devolución completa o parcial
 * - Actualizar estado del pago a REFUNDED
 * - Registrar razón de devolución
 * - Registrar operador que procesa el refund
 * - Actualizar Transaction.paymentStatus a PENDING si refund completo
 *
 * Reglas de negocio:
 * - Solo se pueden devolver pagos COMPLETED
 * - El monto de devolución debe ser <= amount del pago original
 * - Debe proporcionarse una razón obligatoria
 * - Se registra el operador que autoriza el refund
 * - Si es refund completo, Transaction vuelve a PENDING
 *
 * Usado por:
 * - Errores en el cobro
 * - Cancelaciones autorizadas
 * - Ajustes administrativos
 * - Reclamos de clientes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ProcessRefundUseCase {

   /**
    * Procesa una devolución completa del pago.
    *
    * @param paymentId ID del pago
    * @param command datos del refund (razón, operador)
    * @return PaymentDto con información del pago y refund registrado
    *
    * @throws IllegalArgumentException
    *         si no existe pago con ese ID
    * @throws IllegalStateException
    *         si el pago no está COMPLETED o ya tiene refund
    */
   PaymentDto processFullRefund(Long paymentId, RefundPaymentCommand command);

   /**
    * Procesa una devolución parcial del pago.
    *
    * @param paymentId ID del pago
    * @param command datos del refund (monto, razón, operador)
    * @return PaymentDto con información del pago y refund parcial registrado
    *
    * @throws IllegalArgumentException
    *         si no existe pago con ese ID o monto inválido
    * @throws IllegalStateException
    *         si el pago no está COMPLETED o ya tiene refund
    */
   PaymentDto processPartialRefund(Long paymentId, RefundPaymentCommand command);
}