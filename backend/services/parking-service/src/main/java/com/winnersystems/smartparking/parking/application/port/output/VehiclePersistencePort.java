package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Vehicle;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Vehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface VehiclePersistencePort {

   // ========================= WRITE =========================

   Vehicle save(Vehicle vehicle);

   void delete(Long id);

   // ========================= FIND ÚNICO =========================

   Optional<Vehicle> findById(Long id);

   Optional<Vehicle> findByPlateNumber(String plateNumber);

   // ========================= EXISTS =========================

   boolean existsByPlateNumber(String plateNumber);

   // ========================= LIST (paginado) =========================

   /**
    * Lista todos los vehículos con búsqueda y filtro de estado.
    *
    * @param pageRequest parámetros de paginación
    * @param search búsqueda en placa, marca, color (opcional)
    * @param status filtro: "ACTIVE", "DELETED", "ALL" (opcional)
    * @return PageResult con vehículos
    */
   PageResult<Vehicle> findAll(PageRequest pageRequest, String search, String status);

   /**
    * Lista vehículos recurrentes (totalVisits > 1).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con vehículos recurrentes ordenados por totalVisits DESC
    */
   PageResult<Vehicle> findRecurrent(PageRequest pageRequest);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los vehículos activos (deletedAt IS NULL).
    *
    * @return Lista completa de vehículos activos
    */
   List<Vehicle> findAllActive();

   // ========================= COUNT =========================

   long count();

   long countActive();
}