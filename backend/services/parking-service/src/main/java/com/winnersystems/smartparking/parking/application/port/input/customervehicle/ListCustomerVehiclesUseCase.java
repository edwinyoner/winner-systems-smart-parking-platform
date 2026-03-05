package com.winnersystems.smartparking.parking.application.port.input.customervehicle;

import com.winnersystems.smartparking.parking.application.dto.query.CustomerVehicleDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;

import java.util.List;

/**
 * Puerto de entrada para listar relaciones cliente-vehículo.
 *
 * Responsabilidades:
 * - Listar todos los vehículos de un cliente
 * - Listar todos los clientes de un vehículo
 * - Listar combinaciones frecuentes (usageCount > 5)
 *
 * Usado por:
 * - Historial de uso
 * - Sugerencias inteligentes en RecordEntryUseCase
 * - Reportes de patrones de uso
 * - Dashboard de clientes frecuentes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListCustomerVehiclesUseCase {

   /**
    * Lista todos los vehículos usados por un cliente.
    *
    * Útil para:
    * - Ver historial de vehículos de un cliente
    * - Sugerir vehículo al registrar entrada
    *
    * @param customerId ID del cliente
    * @return Lista de relaciones (incluye datos de vehículos)
    */
   List<CustomerVehicleDto> listVehiclesByCustomer(Long customerId);

   /**
    * Lista todos los clientes que han usado un vehículo.
    *
    * Útil para:
    * - Ver historial de conductores de un vehículo
    * - Detectar vehículos compartidos (taxis, Uber, etc.)
    *
    * @param vehicleId ID del vehículo
    * @return Lista de relaciones (incluye datos de clientes)
    */
   List<CustomerVehicleDto> listCustomersByVehicle(Long vehicleId);

   /**
    * Lista combinaciones frecuentes (usageCount > threshold).
    *
    * @param minUsageCount umbral mínimo de usos (default 5)
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con combinaciones frecuentes ordenadas por usageCount DESC
    */
   PagedResponse<CustomerVehicleDto> listFrequentCombinations(int minUsageCount,
                                                              int pageNumber,
                                                              int pageSize);

   /**
    * Lista todas las relaciones con paginación.
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con todas las relaciones
    */
   PagedResponse<CustomerVehicleDto> listAllCustomerVehicles(int pageNumber, int pageSize);
}