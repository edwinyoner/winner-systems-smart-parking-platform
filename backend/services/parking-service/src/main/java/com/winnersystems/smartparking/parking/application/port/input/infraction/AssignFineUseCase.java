package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.command.AssignFineCommand;
import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;

/**
 * Puerto de entrada para asignar una multa a una infracción.
 *
 * Responsabilidades:
 * - Validar que la infracción exista
 * - Asignar monto de multa y fecha de vencimiento
 * - Validar que la infracción no esté resuelta
 * - Validar que no tenga multa previamente asignada
 *
 * Reglas de negocio:
 * - Solo se puede asignar multa a infracciones PENDING o IN_REVIEW
 * - El monto debe ser mayor a 0
 * - La fecha de vencimiento debe ser futura
 * - Si no se proporciona fecha, se asigna 7 días por defecto
 * - Una infracción puede tener solo UNA multa
 *
 * Usado por:
 * - Autoridades después de revisar infracción
 * - Sistema automático para infracciones predefinidas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface AssignFineUseCase {

   /**
    * Asigna una multa a una infracción.
    *
    * @param infractionId ID de la infracción
    * @param command datos de la multa (monto, fecha vencimiento)
    * @return InfractionDto con la multa asignada
    *
    * @throws IllegalArgumentException
    *         si no existe infracción o monto inválido
    * @throws IllegalStateException
    *         si la infracción ya está resuelta o ya tiene multa
    */
   InfractionDto assignFine(Long infractionId, AssignFineCommand command);
}