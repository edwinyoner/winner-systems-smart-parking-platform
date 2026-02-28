package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO detallado para consulta individual de transacción.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailResponse {

   private Long id;
   private String status;
   private String paymentStatus;

   // Vehículo
   private VehicleInfo vehicle;
   private String type;

   // Cliente
   private CustomerInfo customer;

   // Zona
   private ZoneInfo zone;

   // Espacio
   private SpaceInfo space;

   // Tiempos
   private LocalDateTime entryTime;
   private LocalDateTime exitTime;
   private Integer durationMinutes;
   private String durationFormatted;

   // Documentos
   private DocumentInfo entryDocument;
   private DocumentInfo exitDocument;

   // Tarifa
   private RateInfo rate;

   // Montos
   private BigDecimal calculatedAmount;
   private BigDecimal discountAmount;
   private BigDecimal totalAmount;
   private String currency;

   // Pago
   private PaymentInfo payment;

   // Evidencia
   private String entryPhotoUrl;
   private String exitPhotoUrl;
   private Double entryPlateConfidence;
   private Double exitPlateConfidence;

   // Comprobante
   private Boolean receiptSent;
   private LocalDateTime receiptSentAt;
   private String receiptWhatsAppStatus;
   private String receiptEmailStatus;

   // Observaciones
   private String notes;
   private String cancellationReason;

   // Auditoría
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;

   // ========================= INNER CLASSES =========================

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class VehicleInfo {
      private Long id;
      private String plateNumber;
      private String brand;
      private String color;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class CustomerInfo {
      private Long id;
      private String documentNumber;
      private String name;
      private String phone;
      private String email;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class ZoneInfo {
      private Long id;
      private String name;
      private String code;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class SpaceInfo {
      private Long id;
      private String code;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class DocumentInfo {
      private String number;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class RateInfo {
      private Long id;
      private String name;
      private BigDecimal hourlyRate;
   }

   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class PaymentInfo {
      private Long id;
      private BigDecimal amount;
      private String referenceNumber;
      private LocalDateTime paymentDate;
      private String status;
   }
}