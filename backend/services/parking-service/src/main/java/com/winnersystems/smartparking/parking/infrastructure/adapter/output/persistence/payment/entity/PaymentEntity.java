package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Payment.
 *
 * Mapeo Oracle:
 * - Tabla: PAYMENTS (mayúsculas para Oracle)
 * - PK: PAYMENT_ID con SEQUENCE
 * - FK: TRANSACTION_ID (UNIQUE - relación 1:1)
 *
 * NO tiene soft delete - histórico permanente
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "PAYMENTS")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_seq")
   @SequenceGenerator(name = "payment_seq", sequenceName = "PAYMENT_SEQ", allocationSize = 1)
   @Column(name = "PAYMENT_ID")
   private Long id;

   @Column(name = "TRANSACTION_ID", nullable = false, unique = true)
   private Long transactionId;

   @Column(name = "PAYMENT_TYPE_ID", nullable = false)
   private Long paymentTypeId;

   @Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
   private BigDecimal amount;

   @Column(name = "CURRENCY", nullable = false, length = 10)
   private String currency;

   @Column(name = "PAYMENT_DATE", nullable = false)
   private LocalDateTime paymentDate;

   @Column(name = "REFERENCE_NUMBER", length = 100)
   private String referenceNumber;

   @Column(name = "OPERATOR_ID", nullable = false)
   private Long operatorId;

   @Column(name = "STATUS", nullable = false, length = 20)
   private String status;

   // ========================= CAMPOS DE DEVOLUCIÓN =========================

   @Column(name = "REFUND_AMOUNT", precision = 10, scale = 2)
   private BigDecimal refundAmount;

   @Column(name = "REFUND_DATE")
   private LocalDateTime refundDate;

   @Column(name = "REFUND_REASON", length = 500)
   private String refundReason;

   @Column(name = "REFUND_OPERATOR_ID")
   private Long refundOperatorId;

   // ========================= OBSERVACIONES =========================

   @Column(name = "NOTES", length = 1000)
   private String notes;

   // ========================= CAMPOS DE AUDITORÍA =========================

   @CreatedDate
   @Column(name = "CREATED_AT", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @CreatedBy
   @Column(name = "CREATED_BY", updatable = false)
   private Long createdBy;

   @LastModifiedDate
   @Column(name = "UPDATED_AT")
   private LocalDateTime updatedAt;

   @LastModifiedBy
   @Column(name = "UPDATED_BY")
   private Long updatedBy;

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