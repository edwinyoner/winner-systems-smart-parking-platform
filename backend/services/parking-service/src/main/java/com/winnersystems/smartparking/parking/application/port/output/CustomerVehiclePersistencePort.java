package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.CustomerVehicle;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de CustomerVehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CustomerVehiclePersistencePort {

   // ========================= WRITE =========================

   CustomerVehicle save(CustomerVehicle customerVehicle);

   // No tiene delete - histórico permanente

   // ========================= FIND ÚNICO =========================

   Optional<CustomerVehicle> findById(Long id);

   /**
    * Busca relación específica por customerId + vehicleId.
    *
    * @param customerId ID del cliente
    * @param vehicleId ID del vehículo
    * @return Optional con la relación si existe
    */
   Optional<CustomerVehicle> findByCustomerAndVehicle(Long customerId, Long vehicleId);

   // ========================= EXISTS =========================

   /**
    * Verifica si existe relación entre cliente y vehículo.
    *
    * @param customerId ID del cliente
    * @param vehicleId ID del vehículo
    * @return true si existe la relación
    */
   boolean existsByCustomerAndVehicle(Long customerId, Long vehicleId);

   // ========================= LIST (por customer) =========================

   /**
    * Lista todos los vehículos usados por un cliente.
    *
    * @param customerId ID del cliente
    * @return Lista de relaciones
    */
   List<CustomerVehicle> findByCustomerId(Long customerId);

   // ========================= LIST (por vehicle) =========================

   /**
    * Lista todos los clientes que han usado un vehículo.
    *
    * @param vehicleId ID del vehículo
    * @return Lista de relaciones
    */
   List<CustomerVehicle> findByVehicleId(Long vehicleId);

   // ========================= LIST (combinaciones frecuentes - paginado) =========================

   /**
    * Lista combinaciones frecuentes (usageCount >= minUsageCount).
    *
    * @param minUsageCount umbral mínimo de usos
    * @param pageRequest parámetros de paginación
    * @return PageResult con combinaciones frecuentes ordenadas por usageCount DESC
    */
   PageResult<CustomerVehicle> findFrequentCombinations(int minUsageCount, PageRequest pageRequest);

   /**
    * Lista todas las relaciones con paginación.
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con todas las relaciones
    */
   PageResult<CustomerVehicle> findAll(PageRequest pageRequest);

   // ========================= COUNT =========================

   long count();

   long countByCustomerId(Long customerId);

   long countByVehicleId(Long vehicleId);
}