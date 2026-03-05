package com.winnersystems.smartparking.parking.application.port.input.customervehicle;

import com.winnersystems.smartparking.parking.application.dto.query.CustomerVehicleDto;

/**
 * Puerto de entrada para obtener relaciones cliente-vehículo.
 *
 * Responsabilidades:
 * - Buscar relación específica por customerId + vehicleId
 * - Validar si existe una relación
 *
 * Usado por:
 * - RecordEntryUseCase para sugerir cliente frecuente
 * - Validaciones de asociaciones
 * - Reportes de uso
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetCustomerVehicleUseCase {

   /**
    * Obtiene la relación entre un cliente y un vehículo.
    *
    * @param customerId ID del cliente
    * @param vehicleId ID del vehículo
    * @return CustomerVehicleDto si existe, null si no existe
    */
   CustomerVehicleDto getCustomerVehicle(Long customerId, Long vehicleId);

   /**
    * Verifica si existe una relación entre cliente y vehículo.
    *
    * @param customerId ID del cliente
    * @param vehicleId ID del vehículo
    * @return true si existe la relación
    */
   boolean existsCustomerVehicle(Long customerId, Long vehicleId);
}