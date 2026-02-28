package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parkingshiftrate.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response con información de configuración de tarifa por turno en un parqueo.
 * Incluye datos expandidos de Shift y Rate para mostrar en el frontend.
 *
 * Ejemplo de respuesta:
 * {
 *   "id": 1,
 *   "parkingId": 5,
 *   "shiftId": 1,
 *   "shiftName": "Mañana",
 *   "shiftCode": "MORNING",
 *   "rateId": 1,
 *   "rateName": "Tarifa Auto",
 *   "rateAmount": 2.00,
 *   "rateCurrency": "PEN",
 *   "status": true,
 *   "createdAt": "2025-02-20T10:00:00",
 *   "updatedAt": "2025-02-20T10:00:00"
 * }
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ParkingShiftRateResponse(
      Long id,
      Long parkingId,           // parkingId
      Long shiftId,
      String shiftName,         // Ej: "Mañana"
      String shiftCode,         // Ej: "MORNING"
      Long rateId,
      String rateName,          // Ej: "Tarifa Auto"
      BigDecimal rateAmount,    // Ej: 2.00
      String rateCurrency,      // Ej: "PEN"
      Boolean status,           // Activo/Inactivo
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}