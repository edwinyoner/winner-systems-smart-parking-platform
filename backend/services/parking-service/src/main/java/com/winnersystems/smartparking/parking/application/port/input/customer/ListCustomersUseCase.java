package com.winnersystems.smartparking.parking.application.port.input.customer;

import com.winnersystems.smartparking.parking.application.dto.query.CustomerDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;

import java.util.List;

/**
 * Puerto de entrada para listar clientes con filtros y paginación.
 *
 * Responsabilidades:
 * - Listar todos los clientes con búsqueda y filtros
 * - Listar clientes recurrentes
 * - Listar clientes activos (no eliminados)
 *
 * Usado por:
 * - Dashboard administrativo
 * - Reportes de clientes
 * - Búsquedas avanzadas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListCustomersUseCase {

   /**
    * Lista todos los clientes con búsqueda por nombre/documento y paginación.
    *
    * @param pageNumber número de página (0-indexed)
    * @param pageSize tamaño de página
    * @param search término de búsqueda (nombre, apellido, documento) - opcional
    * @param status filtro por estado: "ACTIVE", "DELETED", "ALL" - opcional
    * @return PagedResponse con lista de clientes
    */
   PagedResponse<CustomerDto> listAllCustomers(int pageNumber, int pageSize,
                                               String search, String status);

   /**
    * Lista todos los clientes activos (sin paginación).
    *
    * Útil para:
    * - Dropdowns/selectores en formularios
    * - Exportaciones completas
    * - Caches de clientes frecuentes
    *
    * @return Lista completa de clientes activos
    */
   List<CustomerDto> listAllActiveCustomers();

   /**
    * Lista clientes recurrentes (totalVisits > 1) ordenados por visitas DESC.
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con clientes recurrentes
    */
   PagedResponse<CustomerDto> listRecurrentCustomers(int pageNumber, int pageSize);
}