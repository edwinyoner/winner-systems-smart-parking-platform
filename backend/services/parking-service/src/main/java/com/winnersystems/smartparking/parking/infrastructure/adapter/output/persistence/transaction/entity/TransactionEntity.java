package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Transaction.
 * Configurada para Oracle Database.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "TRANSACTIONS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEntity {

   // ========================= IDENTIFICADOR =========================

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
   @SequenceGenerator(
         name = "transaction_seq",
         sequenceName = "SEQ_TRANSACTION",
         allocationSize = 1
   )
   @Column(name = "ID")
   private Long id;

   // ========================= RELACIONES (FKs) =========================

   @Column(name = "VEHICLE_ID", nullable = false)
   private Long vehicleId;

   @Column(name = "CUSTOMER_ID", nullable = false)
   private Long customerId;

   @Column(name = "PARKING_SPACE_ID", nullable = false)
   private Long parkingSpaceId;

   @Column(name = "ZONE_ID", nullable = false)
   private Long zoneId;

   @Column(name = "RATE_ID", nullable = false)
   private Long rateId;

   // ========================= SEGURIDAD - DOCUMENTOS =========================

   @Column(name = "ENTRY_DOCUMENT_TYPE_ID", nullable = false)
   private Long entryDocumentTypeId;

   @Column(name = "ENTRY_DOCUMENT_NUMBER", nullable = false, length = 20)
   private String entryDocumentNumber;

   @Column(name = "EXIT_DOCUMENT_TYPE_ID")
   private Long exitDocumentTypeId;

   @Column(name = "EXIT_DOCUMENT_NUMBER", length = 20)
   private String exitDocumentNumber;

   // ========================= TIEMPOS =========================

   @Column(name = "ENTRY_TIME", nullable = false)
   private LocalDateTime entryTime;

   @Column(name = "EXIT_TIME")
   private LocalDateTime exitTime;

   @Column(name = "DURATION_MINUTES")
   private Integer durationMinutes;

   // ========================= OPERADORES Y MÉTODOS =========================

   @Column(name = "ENTRY_OPERATOR_ID", nullable = false)
   private Long entryOperatorId;

   @Column(name = "EXIT_OPERATOR_ID")
   private Long exitOperatorId;

   @Column(name = "ENTRY_METHOD", length = 20)
   private String entryMethod;

   @Column(name = "EXIT_METHOD", length = 20)
   private String exitMethod;

   // ========================= EVIDENCIA =========================

   @Column(name = "ENTRY_PHOTO_URL", length = 500)
   private String entryPhotoUrl;

   @Column(name = "EXIT_PHOTO_URL", length = 500)
   private String exitPhotoUrl;

   @Column(name = "ENTRY_PLATE_CONFIDENCE")
   private Double entryPlateConfidence;

   @Column(name = "EXIT_PLATE_CONFIDENCE")
   private Double exitPlateConfidence;

   // ========================= MONTOS =========================

   @Column(name = "CALCULATED_AMOUNT", precision = 10, scale = 2)
   private BigDecimal calculatedAmount;

   @Column(name = "DISCOUNT_AMOUNT", precision = 10, scale = 2)
   private BigDecimal discountAmount;

   @Column(name = "TOTAL_AMOUNT", precision = 10, scale = 2)
   private BigDecimal totalAmount;

   @Column(name = "CURRENCY", length = 3)
   private String currency;

   // ========================= ESTADOS =========================

   @Column(name = "STATUS", nullable = false, length = 20)
   private String status;

   @Column(name = "PAYMENT_STATUS", nullable = false, length = 20)
   private String paymentStatus;

   // ========================= COMPROBANTE DIGITAL =========================

   @Column(name = "RECEIPT_SENT", nullable = false, columnDefinition = "NUMBER(1) DEFAULT 0")
   private Boolean receiptSent = false;

   @Column(name = "RECEIPT_SENT_AT")
   private LocalDateTime receiptSentAt;

   @Column(name = "RECEIPT_WHATSAPP_STATUS", length = 20)
   private String receiptWhatsAppStatus;

   @Column(name = "RECEIPT_EMAIL_STATUS", length = 20)
   private String receiptEmailStatus;

   // ========================= OBSERVACIONES =========================

   @Column(name = "NOTES", columnDefinition = "CLOB")
   private String notes;

   @Column(name = "CANCELLATION_REASON", length = 500)
   private String cancellationReason;

   // ========================= AUDITORÍA =========================

   @Column(name = "CREATED_AT", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "CREATED_BY")
   private Long createdBy;

   @Column(name = "UPDATED_AT", nullable = false)
   private LocalDateTime updatedAt;

   @Column(name = "UPDATED_BY")
   private Long updatedBy;

   @Column(name = "DELETED_AT")
   private LocalDateTime deletedAt;

   @Column(name = "DELETED_BY")
   private Long deletedBy;

   // ========================= LIFECYCLE CALLBACKS =========================

   @PrePersist
   protected void onCreate() {
      if (this.createdAt == null) this.createdAt = LocalDateTime.now();
      if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
      if (this.currency == null) this.currency = "PEN";
      if (this.discountAmount == null) this.discountAmount = BigDecimal.ZERO;
      if (this.receiptSent == null) this.receiptSent = false;
   }

   @PreUpdate
   protected void onUpdate() {
      if (this.updatedAt == null) this.updatedAt = LocalDateTime.now();
   }
}