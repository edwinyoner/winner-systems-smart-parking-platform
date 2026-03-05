package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de entrada para listar infracciones con filtros y paginación.
 *
 * Responsabilidades:
 * - Listar todas las infracciones con búsqueda y filtros
 * - Listar infracciones por estado (PENDING, IN_REVIEW, RESOLVED, ESCALATED)
 * - Listar infracciones por vehículo
 * - Listar infracciones por transacción
 * - Listar infracciones con multas pendientes
 * - Listar infracciones por rango de fechas
 *
 * Usado por:
 * - Dashboard de autoridad
 * - Reportes de infracciones
 * - Auditoría
 * - Búsquedas avanzadas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListInfractionsUseCase {

   /**
    * Lista todas las infracciones con búsqueda por código y paginación.
    *
    * @param pageNumber número de página (0-indexed)
    * @param pageSize tamaño de página
    * @param search término de búsqueda (código de infracción) - opcional
    * @param status filtro por estado: "PENDING", "IN_REVIEW", "RESOLVED", "ESCALATED", "ALL" - opcional
    * @return PagedResponse con lista de infracciones
    */
   PagedResponse<InfractionDto> listAllInfractions(int pageNumber, int pageSize,
                                                   String search, String status);

   /**
    * Lista infracciones por vehículo.
    *
    * @param vehicleId ID del vehículo
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con infracciones del vehículo
    */
   PagedResponse<InfractionDto> listInfractionsByVehicle(Long vehicleId,
                                                         int pageNumber,
                                                         int pageSize);

   /**
    * Lista infracciones por cliente.
    *
    * @param customerId ID del cliente
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con infracciones del cliente
    */
   PagedResponse<InfractionDto> listInfractionsByCustomer(Long customerId,
                                                          int pageNumber,
                                                          int pageSize);

   /**
    * Lista infracciones por transacción.
    *
    * @param transactionId ID de la transacción
    * @return Lista de infracciones asociadas a la transacción
    */
   List<InfractionDto> listInfractionsByTransaction(Long transactionId);

   /**
    * Lista infracciones con multas pendientes de pago.
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con infracciones que tienen multa y finePaid = false
    */
   PagedResponse<InfractionDto> listInfractionsWithPendingFines(int pageNumber, int pageSize);

   /**
    * Lista infracciones con multas vencidas.
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con infracciones donde fineDueDate < now y finePaid = false
    */
   PagedResponse<InfractionDto> listInfractionsWithOverdueFines(int pageNumber, int pageSize);

   /**
    * Lista infracciones por rango de fechas.
    *
    * @param startDate fecha inicio
    * @param endDate fecha fin
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con infracciones detectadas en el rango
    */
   PagedResponse<InfractionDto> listInfractionsByDateRange(LocalDateTime startDate,
                                                           LocalDateTime endDate,
                                                           int pageNumber,
                                                           int pageSize);

   /**
    * Lista todas las infracciones pendientes (sin paginación).
    *
    * Útil para:
    * - Dashboards de autoridad
    * - Alertas en tiempo real
    *
    * @return Lista completa de infracciones pendientes
    */
   List<InfractionDto> listAllPendingInfractions();
}