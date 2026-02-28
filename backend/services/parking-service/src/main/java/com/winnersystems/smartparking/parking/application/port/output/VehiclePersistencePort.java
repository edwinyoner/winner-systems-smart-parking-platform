package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.Vehicle;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de Vehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface VehiclePersistencePort {

   /**
    * Guarda un vehículo (crear o actualizar).
    *
    * @param vehicle vehículo a guardar
    * @return vehículo guardado
    */
   Vehicle save(Vehicle vehicle);

   /**
    * Busca un vehículo por su ID.
    *
    * @param id ID del vehículo
    * @return Optional con el vehículo si existe
    */
   Optional<Vehicle> findById(Long id);

   /**
    * Busca un vehículo por su placa.
    *
    * @param plateNumber placa del vehículo
    * @return Optional con el vehículo si existe
    */
   Optional<Vehicle> findByPlateNumber(String plateNumber);

   /**
    * Verifica si existe un vehículo con esa placa.
    *
    * @param plateNumber placa del vehículo
    * @return true si existe
    */
   boolean existsByPlateNumber(String plateNumber);
}