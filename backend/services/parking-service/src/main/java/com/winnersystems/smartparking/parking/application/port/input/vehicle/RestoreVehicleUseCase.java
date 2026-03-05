package com.winnersystems.smartparking.parking.application.port.input.vehicle;

import com.winnersystems.smartparking.parking.application.dto.query.VehicleDto;

/**
 * Puerto de entrada para restaurar un vehículo previamente eliminado.
 *
 * Responsabilidades:
 * - Restaurar vehículo (deletedAt = null, deletedBy = null)
 * - Validar que el vehículo exista
 * - Validar que esté efectivamente eliminado
 *
 * Usado por:
 * - Corrección de eliminaciones accidentales
 * - Reactivación de vehículos
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RestoreVehicleUseCase {

   /**
    * Restaura un vehículo previamente eliminado.
    *
    * @param vehicleId ID del vehículo
    * @return VehicleDto con el vehículo restaurado
    *
    * @throws IllegalArgumentException si no existe vehículo con ese ID
    * @throws IllegalStateException si el vehículo no está eliminado
    */
   VehicleDto restoreVehicle(Long vehicleId);
}