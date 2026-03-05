package com.winnersystems.smartparking.parking.application.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO para transacciones ACTIVAS (vehículos actualmente dentro del parqueo).
 *
 * Usado en:
 * - Dashboard de operador (lista de vehículos dentro)
 * - Pantallas de monitoreo en tiempo real
 * - Búsqueda de vehículos actualmente estacionados
 * - Cálculo de disponibilidad actual
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ActiveTransactionDto(
      // ========================= IDENTIFICACIÓN =========================
      Long id,

      // ========================= VEHÍCULO =========================
      Long vehicleId,
      String plateNumber,

      // ========================= CLIENTE =========================
      Long customerId,
      String customerName,
      String customerPhone,
      String customerEmail,
      String documentType,
      String documentNumber,

      // ========================= UBICACIÓN =========================
      Long parkingId,
      String parkingName,
      Long zoneId,
      String zoneName,
      Long spaceId,
      String spaceCode,

      // ========================= TIEMPOS =========================
      LocalDateTime entryTime,
      Integer elapsedMinutes,       // Minutos transcurridos desde entrada
      String elapsedFormatted,      // "2h 15min"

      // ========================= CÁLCULO EN TIEMPO REAL =========================
      BigDecimal hourlyRate,
      BigDecimal currentAmount,     // Monto acumulado ACTUAL (calculado en tiempo real)
      String currency,              // Default "PEN"

      // ========================= ALERTAS =========================
      Boolean isOverdue,            // ¿Excedió tiempo máximo esperado?
      Integer maxRecommendedMinutes,
      Boolean requiresAttention,    // ¿Requiere atención del operador?

      // ========================= OPERADOR =========================
      Long entryOperatorId,
      String entryOperatorName,
      String entryMethod,           // MANUAL, CAMERA_AI, SENSOR

      // ========================= EVIDENCIA =========================
      String entryPhotoUrl,
      Double plateConfidence,

      // ========================= OBSERVACIONES =========================
      String notes,

      // ========================= AUDITORÍA =========================
      LocalDateTime createdAt
) {

   /**
    * Factory method principal — crea con valores por defecto para los campos
    * booleanos y moneda. Es el método que usará el mapper.
    *
    * Los campos de alerta (isOverdue, requiresAttention) se calculan en el
    * Use Case antes de construir el DTO, no se dejan null.
    */
   public static ActiveTransactionDto of(
         Long id,
         Long vehicleId, String plateNumber,
         Long customerId, String customerName, String customerPhone,
         String customerEmail, String documentType, String documentNumber,
         Long parkingId, String parkingName,
         Long zoneId, String zoneName,
         Long spaceId, String spaceCode,
         LocalDateTime entryTime, Integer elapsedMinutes, String elapsedFormatted,
         BigDecimal hourlyRate, BigDecimal currentAmount,
         Boolean isOverdue, Integer maxRecommendedMinutes, Boolean requiresAttention,
         Long entryOperatorId, String entryOperatorName, String entryMethod,
         String entryPhotoUrl, Double plateConfidence,
         String notes, LocalDateTime createdAt
   ) {
      return new ActiveTransactionDto(
            id,
            vehicleId, plateNumber,
            customerId, customerName, customerPhone, customerEmail, documentType, documentNumber,
            parkingId, parkingName,
            zoneId, zoneName,
            spaceId, spaceCode,
            entryTime, elapsedMinutes, elapsedFormatted,
            hourlyRate, currentAmount, "PEN",              // currency siempre "PEN"
            isOverdue != null ? isOverdue : false,
            maxRecommendedMinutes,
            requiresAttention != null ? requiresAttention : false,
            entryOperatorId, entryOperatorName, entryMethod,
            entryPhotoUrl, plateConfidence,
            notes, createdAt
      );
   }
}