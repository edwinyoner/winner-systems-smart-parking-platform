package com.winnersystems.smartparking.parking.application.port.input.vehicle;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.query.VehicleDto;

/**
 * Puerto de entrada para actualizar información de un vehículo existente.
 *
 * Responsabilidades:
 * - Actualizar información visual (color, marca)
 * - Validar que el vehículo exista
 * - Validar que no esté eliminado (soft delete)
 *
 * Campos NO actualizables:
 * - licensePlate (identificador único legal, no cambia)
 * - totalVisits, firstSeenDate, lastSeenDate (calculados automáticamente)
 *
 * Casos de uso:
 * - Operadores corrigen/mejoran datos visuales
 * - Actualización de color tras repintado
 * - Corrección de marca mal registrada
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface UpdateVehicleUseCase {

   /**
    * Actualiza la información visual de un vehículo.
    *
    * @param vehicleId ID del vehículo
    * @param command datos a actualizar (color, marca)
    * @return VehicleDto con información actualizada
    *
    * @throws IllegalArgumentException si no existe vehículo con ese ID
    * @throws IllegalStateException si el vehículo está eliminado
    */
   VehicleDto updateVehicle(Long vehicleId, UpdateVehicleCommand command);
}