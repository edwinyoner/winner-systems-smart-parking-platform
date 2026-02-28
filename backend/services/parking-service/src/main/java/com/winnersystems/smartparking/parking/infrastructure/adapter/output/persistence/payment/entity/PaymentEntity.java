package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Payment.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "payments")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "transaction_id", nullable = false, unique = true)
   private Long transactionId;

   @Column(name = "payment_type_id", nullable = false)
   private Long paymentTypeId;

   @Column(name = "amount", nullable = false, precision = 10, scale = 2)
   private BigDecimal amount;

   @Column(name = "currency", length = 3)
   private String currency;

   @Column(name = "payment_date", nullable = false)
   private LocalDateTime paymentDate;

   @Column(name = "reference_number", length = 100)
   private String referenceNumber;

   @Column(name = "operator_id", nullable = false)
   private Long operatorId;

   @Column(name = "status", nullable = false, length = 20)
   private String status;

   // ========================= CAMPOS DE DEVOLUCIÓN =========================

   @Column(name = "refund_amount", precision = 10, scale = 2)
   private BigDecimal refundAmount;

   @Column(name = "refund_date")
   private LocalDateTime refundDate;

   @Column(name = "refund_reason", length = 500)
   private String refundReason;

   @Column(name = "refund_operator_id")
   private Long refundOperatorId;

   // ========================= OBSERVACIONES =========================

   @Column(name = "notes", columnDefinition = "CLOB")
   private String notes;

   // ========================= CAMPOS DE AUDITORÍA (igual que UserEntity y CustomerEntity) =========================

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "created_by")
   private Long createdBy;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   @Column(name = "updated_by")
   private Long updatedBy;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;        // Soft delete

   @Column(name = "deleted_by")
   private Long deletedBy;

   // ========================= LIFECYCLE CALLBACKS =========================

   @PrePersist
   protected void onCreate() {
      LocalDateTime now = LocalDateTime.now();
      this.createdAt = now;
      this.updatedAt = now;
      if (this.paymentDate == null) {
         this.paymentDate = now;
      }
      if (this.status == null) {
         this.status = "COMPLETED";
      }
      if (this.currency == null) {
         this.currency = "PEN";
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}