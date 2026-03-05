package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateInfractionCommand;
import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;

/**
 * Puerto de entrada para actualizar información de una infracción existente.
 *
 * Responsabilidades:
 * - Actualizar descripción y notas
 * - Agregar/actualizar evidencia
 * - Cambiar severidad
 * - Validar que la infracción exista
 * - Validar que no esté resuelta (RESOLVED)
 *
 * Campos NO actualizables:
 * - infractionCode (generado automáticamente)
 * - parkingId, zoneId, vehicleId (datos originales)
 * - status (solo por ResolveInfractionUseCase)
 * - fineAmount, finePaid (solo por AssignFineUseCase y RecordFinePaymentUseCase)
 *
 * Usado por:
 * - Operadores que agregan más información
 * - Correcciones administrativas
 * - Adición de evidencia posterior
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface UpdateInfractionUseCase {

   /**
    * Actualiza la información de una infracción.
    *
    * @param infractionId ID de la infracción
    * @param command datos a actualizar
    * @return InfractionDto con información actualizada
    *
    * @throws IllegalArgumentException si no existe infracción con ese ID
    * @throws IllegalStateException si la infracción ya está resuelta
    */
   InfractionDto updateInfraction(Long infractionId, UpdateInfractionCommand command);
}