package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO para transacciones activas (monitoreo en tiempo real).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActiveTransactionResponse {

   private Long id;

   // Vehículo
   private Long vehicleId;
   private String plateNumber;
   private String vehicleBrand;
   private String vehicleColor;

   // Cliente
   private Long customerId;
   private String customerName;
   private String customerPhone;
   private String customerEmail;
   private String documentNumber;

   // Ubicación
   private Long zoneId;
   private String zoneName;
   private String zoneCode;
   private Long spaceId;
   private String spaceCode;

   // Tiempos
   private LocalDateTime entryTime;
   private Integer elapsedMinutes;
   private String elapsedFormatted;
   private String currency;

   // Cálculo en tiempo real
   private BigDecimal hourlyRate;
   private BigDecimal currentAmount;

   // Alertas
   private Integer maxRecommendedMinutes;
   private Boolean isOverdue;
   private Boolean requiresAttention;

   // Operador
   private Long entryOperatorId;
   private String entryMethod;

   // Evidencia
   private String entryPhotoUrl;
   private Double plateConfidence;

   // Observaciones
   private String notes;

   // Auditoría
   private LocalDateTime createdAt;
}