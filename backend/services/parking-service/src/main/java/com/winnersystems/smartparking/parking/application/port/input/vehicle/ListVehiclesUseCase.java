package com.winnersystems.smartparking.parking.application.port.input.vehicle;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.VehicleDto;

import java.util.List;

/**
 * Puerto de entrada para listar vehículos con filtros y paginación.
 *
 * Responsabilidades:
 * - Listar todos los vehículos con búsqueda por placa y filtros
 * - Listar vehículos recurrentes
 * - Listar vehículos activos sin paginación (dropdowns)
 *
 * Usado por:
 * - Dashboard administrativo
 * - Reportes de vehículos
 * - Búsquedas avanzadas por operadores
 * - Análisis de vehículos frecuentes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListVehiclesUseCase {

   /**
    * Lista todos los vehículos con búsqueda por placa y paginación.
    *
    * @param pageNumber número de página (0-indexed)
    * @param pageSize tamaño de página
    * @param search término de búsqueda (placa, marca, color) - opcional
    * @param status filtro por estado: "ACTIVE", "DELETED", "ALL" - opcional
    * @return PagedResponse con lista de vehículos
    */
   PagedResponse<VehicleDto> listAllVehicles(int pageNumber, int pageSize,
                                             String search, String status);

   /**
    * Lista todos los vehículos activos (sin paginación).
    *
    * Útil para:
    * - Dropdowns/selectores en formularios
    * - Exportaciones completas
    * - Caches de vehículos frecuentes
    *
    * @return Lista completa de vehículos activos
    */
   List<VehicleDto> listAllActiveVehicles();

   /**
    * Lista vehículos recurrentes (totalVisits > 1) ordenados por visitas DESC.
    *
    * Útil para identificar vehículos frecuentes del sistema.
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con vehículos recurrentes
    */
   PagedResponse<VehicleDto> listRecurrentVehicles(int pageNumber, int pageSize);
}