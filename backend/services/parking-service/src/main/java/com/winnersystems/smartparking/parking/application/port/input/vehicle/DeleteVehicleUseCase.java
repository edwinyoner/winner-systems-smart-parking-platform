package com.winnersystems.smartparking.parking.application.port.input.vehicle;

/**
 * Puerto de entrada para eliminar (soft delete) un vehículo.
 *
 * Responsabilidades:
 * - Marcar vehículo como eliminado (deletedAt, deletedBy)
 * - Validar que el vehículo exista
 * - Validar que no tenga transacciones activas
 *
 * Reglas de negocio:
 * - Solo soft delete (no eliminación física)
 * - No se puede eliminar si tiene transacciones ACTIVE
 * - Los datos eliminados se mantienen para auditoría
 *
 * Usado por:
 * - Corrección de errores (vehículo registrado por error)
 * - Limpieza de placas mal ingresadas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface DeleteVehicleUseCase {

   /**
    * Elimina (soft delete) un vehículo.
    *
    * @param vehicleId ID del vehículo
    * @param deletedBy ID del usuario que elimina
    *
    * @throws IllegalArgumentException si no existe vehículo con ese ID
    * @throws IllegalStateException si tiene transacciones activas
    */
   void deleteVehicle(Long vehicleId, Long deletedBy);
}