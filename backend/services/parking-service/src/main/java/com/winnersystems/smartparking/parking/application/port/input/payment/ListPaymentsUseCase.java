package com.winnersystems.smartparking.parking.application.port.input.payment;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentDto;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Puerto de entrada para listar pagos con filtros y paginación.
 *
 * Responsabilidades:
 * - Listar todos los pagos con búsqueda y filtros
 * - Listar pagos por tipo de pago (efectivo, tarjeta, QR)
 * - Listar pagos por rango de fechas
 * - Listar pagos con devoluciones (refunds)
 * - Listar pagos por estado
 *
 * Usado por:
 * - Reportes financieros
 * - Dashboard de caja
 * - Auditoría de pagos
 * - Conciliación bancaria
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ListPaymentsUseCase {

   /**
    * Lista todos los pagos con búsqueda por referencia y paginación.
    *
    * @param pageNumber número de página (0-indexed)
    * @param pageSize tamaño de página
    * @param search término de búsqueda (número de referencia) - opcional
    * @param status filtro por estado: "COMPLETED", "REFUNDED", "CANCELLED", "ALL" - opcional
    * @return PagedResponse con lista de pagos
    */
   PagedResponse<PaymentDto> listAllPayments(int pageNumber, int pageSize,
                                             String search, String status);

   /**
    * Lista pagos por tipo de pago.
    *
    * @param paymentTypeId ID del tipo de pago (efectivo, tarjeta, etc.)
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con pagos del tipo especificado
    */
   PagedResponse<PaymentDto> listPaymentsByType(Long paymentTypeId,
                                                int pageNumber,
                                                int pageSize);

   /**
    * Lista pagos por rango de fechas.
    *
    * Útil para:
    * - Reportes diarios/mensuales
    * - Cierre de caja
    * - Conciliación bancaria
    *
    * @param startDate fecha inicio
    * @param endDate fecha fin
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con pagos en el rango
    */
   PagedResponse<PaymentDto> listPaymentsByDateRange(LocalDateTime startDate,
                                                     LocalDateTime endDate,
                                                     int pageNumber,
                                                     int pageSize);

   /**
    * Lista pagos con devoluciones (refunds).
    *
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con pagos que tienen refundAmount > 0
    */
   PagedResponse<PaymentDto> listPaymentsWithRefunds(int pageNumber, int pageSize);

   /**
    * Lista pagos realizados por un operador específico.
    *
    * @param operatorId ID del operador
    * @param pageNumber número de página
    * @param pageSize tamaño de página
    * @return PagedResponse con pagos del operador
    */
   PagedResponse<PaymentDto> listPaymentsByOperator(Long operatorId,
                                                    int pageNumber,
                                                    int pageSize);

   /**
    * Lista todos los pagos completados (sin paginación).
    *
    * Útil para:
    * - Exportaciones completas
    * - Reportes consolidados
    * - Cálculos de totales
    *
    * @return Lista completa de pagos completados
    */
   List<PaymentDto> listAllCompletedPayments();
}