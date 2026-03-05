package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO para Infraction.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfractionResponse {

   private Long id;
   private String infractionCode;

   // Relaciones
   private Long parkingId;
   private String parkingName;
   private Long zoneId;
   private String zoneName;
   private Long spaceId;
   private String spaceCode;
   private Long transactionId;
   private Long vehicleId;
   private String vehiclePlate;
   private Long customerId;
   private String customerName;

   // Infracción
   private String infractionType;
   private String severity;
   private String description;
   private String evidence;
   private LocalDateTime detectedAt;
   private Long detectedBy;
   private String detectedByName;
   private String detectionMethod;

   // Multa
   private BigDecimal fineAmount;
   private String currency;
   private LocalDateTime fineDueDate;
   private Boolean finePaid;
   private LocalDateTime finePaidAt;

   // Resolución
   private String status;
   private LocalDateTime resolvedAt;
   private String resolutionType;
   private String resolution;

   // Notificación
   private Boolean notificationSent;

   // Auditoría
   private LocalDateTime createdAt;
}