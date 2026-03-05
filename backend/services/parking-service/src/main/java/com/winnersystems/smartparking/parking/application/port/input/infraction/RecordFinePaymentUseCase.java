package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.command.RecordFinePaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;

/**
 * Puerto de entrada para registrar el pago de una multa.
 *
 * Responsabilidades:
 * - Validar que la infracción exista
 * - Validar que tenga multa asignada (fineAmount > 0)
 * - Validar que la multa no esté pagada
 * - Registrar pago (monto, referencia, fecha)
 * - Marcar finePaid = true
 * - Automáticamente resolver la infracción como PAID
 *
 * Reglas de negocio:
 * - La infracción debe tener multa asignada
 * - El monto pagado debe ser >= fineAmount
 * - La multa no debe estar pagada previamente
 * - Al pagar, la infracción se resuelve automáticamente (status = RESOLVED)
 * - Se registra número de referencia del pago
 *
 * Usado por:
 * - Operadores al recibir pago de multa
 * - Integración con pasarelas de pago
 * - Caja municipal
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RecordFinePaymentUseCase {

   /**
    * Registra el pago de una multa.
    *
    * @param infractionId ID de la infracción
    * @param command datos del pago (monto, referencia)
    * @return InfractionDto con multa pagada y estado RESOLVED
    *
    * @throws IllegalArgumentException
    *         si no existe infracción, no tiene multa o monto insuficiente
    * @throws IllegalStateException
    *         si la multa ya está pagada
    */
   InfractionDto recordFinePayment(Long infractionId, RecordFinePaymentCommand command);
}