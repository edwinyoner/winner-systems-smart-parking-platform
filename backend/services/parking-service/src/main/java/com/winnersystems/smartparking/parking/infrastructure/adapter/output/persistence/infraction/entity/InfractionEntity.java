package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.entity;

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
 * Entidad JPA para Infraction.
 *
 * Mapeo Oracle:
 * - Tabla: INFRACTIONS (mayúsculas para Oracle)
 * - PK: INFRACTION_ID con SEQUENCE
 * - UK: INFRACTION_CODE
 *
 * SÍ tiene soft delete (deletedAt, deletedBy)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "INFRACTIONS")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InfractionEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "infraction_seq")
   @SequenceGenerator(name = "infraction_seq", sequenceName = "INFRACTION_SEQ", allocationSize = 1)
   @Column(name = "INFRACTION_ID")
   private Long id;

   @Column(name = "INFRACTION_CODE", unique = true, length = 50)
   private String infractionCode;

   // ========================= RELACIONES (FKs) =========================

   @Column(name = "PARKING_ID", nullable = false)
   private Long parkingId;

   @Column(name = "ZONE_ID", nullable = false)
   private Long zoneId;

   @Column(name = "SPACE_ID")
   private Long spaceId;

   @Column(name = "TRANSACTION_ID")
   private Long transactionId;

   @Column(name = "VEHICLE_ID", nullable = false)
   private Long vehicleId;

   @Column(name = "CUSTOMER_ID")
   private Long customerId;

   // ========================= CAMPOS DE INFRACCIÓN =========================

   @Column(name = "INFRACTION_TYPE", nullable = false, length = 50)
   private String infractionType;

   @Column(name = "SEVERITY", length = 20)
   private String severity;

   @Column(name = "DETECTED_AT", nullable = false)
   private LocalDateTime detectedAt;

   @Column(name = "DETECTED_BY")
   private Long detectedBy;

   @Column(name = "DETECTION_METHOD", length = 20)
   private String detectionMethod;

   @Column(name = "DESCRIPTION", length = 1000)
   private String description;

   @Column(name = "EVIDENCE", length = 2000)
   private String evidence;

   // ========================= CAMPOS DE MULTA =========================

   @Column(name = "FINE_AMOUNT", precision = 10, scale = 2)
   private BigDecimal fineAmount;

   @Column(name = "CURRENCY", length = 10)
   private String currency;

   @Column(name = "FINE_DUE_DATE")
   private LocalDateTime fineDueDate;

   // ========================= CAMPOS DE RESOLUCIÓN =========================

   @Column(name = "STATUS", nullable = false, length = 20)
   private String status;

   @Column(name = "RESOLVED_AT")
   private LocalDateTime resolvedAt;

   @Column(name = "RESOLVED_BY")
   private Long resolvedBy;

   @Column(name = "RESOLUTION", length = 1000)
   private String resolution;

   @Column(name = "RESOLUTION_TYPE", length = 20)
   private String resolutionType;

   // ========================= CAMPOS DE PAGO DE MULTA =========================

   @Column(name = "FINE_PAID")
   private Boolean finePaid;

   @Column(name = "FINE_PAID_AT")
   private LocalDateTime finePaidAt;

   @Column(name = "FINE_PAID_AMOUNT", precision = 10, scale = 2)
   private BigDecimal finePaidAmount;

   @Column(name = "FINE_PAYMENT_REFERENCE", length = 100)
   private String finePaymentReference;

   // ========================= CAMPOS DE NOTIFICACIÓN =========================

   @Column(name = "NOTIFICATION_SENT")
   private Boolean notificationSent;

   @Column(name = "NOTIFICATION_SENT_AT")
   private LocalDateTime notificationSentAt;

   @Column(name = "NOTIFICATION_METHOD", length = 20)
   private String notificationMethod;

   // ========================= OBSERVACIONES =========================

   @Column(name = "NOTES", length = 1000)
   private String notes;

   // ========================= AUDITORÍA =========================

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

   @Column(name = "DELETED_AT")
   private LocalDateTime deletedAt;

   @Column(name = "DELETED_BY")
   private Long deletedBy;

   // ========================= LIFECYCLE CALLBACKS =========================

   @PrePersist
   protected void onCreate() {
      LocalDateTime now = LocalDateTime.now();
      this.createdAt = now;
      this.updatedAt = now;
      if (this.detectedAt == null) {
         this.detectedAt = now;
      }
      if (this.status == null) {
         this.status = "PENDING";
      }
      if (this.currency == null) {
         this.currency = "PEN";
      }
      if (this.finePaid == null) {
         this.finePaid = false;
      }
      if (this.notificationSent == null) {
         this.notificationSent = false;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}