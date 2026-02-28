package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;

/**
 * DTO de consulta para configuración de tarifa por turno en un parqueo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ParkingShiftRateDto(
      Long id,
      Long parkingId,
      Long shiftId,
      String shiftName,           // Nombre del turno (ej: "Mañana")
      String shiftCode,           // Código del turno (ej: "MORNING")
      Long rateId,
      String rateName,            // Nombre de la tarifa (ej: "Tarifa Auto")
      java.math.BigDecimal rateAmount,  // Monto de la tarifa
      String rateCurrency,        // Moneda (ej: "PEN")
      Boolean status,             // Activo/Inactivo
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
   /**
    * Factory method para crear sin datos de Shift y Rate.
    * Útil cuando solo necesitas IDs.
    */
   public static ParkingShiftRateDto of(
         Long id,
         Long parkingId,
         Long shiftId,
         Long rateId,
         Boolean status
   ) {
      return new ParkingShiftRateDto(
            id, parkingId, shiftId, null, null, rateId, null, null, null, status, null, null
      );
   }
}