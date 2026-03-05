package com.winnersystems.smartparking.parking.application.port.input.vehicle;

import com.winnersystems.smartparking.parking.application.dto.query.VehicleDto;

/**
 * Puerto de entrada para obtener información de vehículos.
 *
 * Responsabilidades:
 * - Buscar vehículo por ID
 * - Buscar vehículo por placa (búsqueda principal)
 *
 * Usado por:
 * - Vista de detalle de vehículo
 * - RecordEntryUseCase para validar vehículo existente
 * - Búsquedas rápidas por operadores
 * - Consultas de historial
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetVehicleUseCase {

   /**
    * Obtiene un vehículo por su ID.
    *
    * @param vehicleId ID del vehículo
    * @return VehicleDto con información del vehículo
    *
    * @throws IllegalArgumentException si no existe vehículo con ese ID
    */
   VehicleDto getVehicleById(Long vehicleId);

   /**
    * Obtiene un vehículo por su placa.
    *
    * La búsqueda es exacta (case-insensitive) después de normalizar.
    *
    * @param licensePlate placa del vehículo (se normaliza automáticamente)
    * @return VehicleDto si existe, null si no existe
    */
   VehicleDto getVehicleByPlate(String licensePlate);
}