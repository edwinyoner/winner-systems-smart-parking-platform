package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO simplificado para listados de transacciones.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

   private Long id;
   private String plateNumber;
   private String customerName;
   private String zoneName;
   private String spaceCode;
   private LocalDateTime entryTime;
   private LocalDateTime exitTime;
   private String duration;
   private BigDecimal totalAmount;
   private String status;
   private String paymentStatus;
   private LocalDateTime createdAt;
}