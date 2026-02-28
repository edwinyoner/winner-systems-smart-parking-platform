package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entidad JPA para Rate.
 * Configurada para Oracle Database.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "RATES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RateEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rate_seq")
   @SequenceGenerator(
         name = "rate_seq",
         sequenceName = "SEQ_RATE",
         allocationSize = 1
   )
   @Column(name = "ID")
   private Long id;

   @Column(name = "NAME", nullable = false, length = 100)
   private String name;

   @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
   private String description;

   @Column(name = "AMOUNT", nullable = false, precision = 10, scale = 2)
   private BigDecimal amount;

   @Column(name = "CURRENCY", nullable = false, length = 3)
   private String currency;

   @Column(name = "STATUS", nullable = false)
   private Boolean status;

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
      if (this.createdAt == null) {
         this.createdAt = LocalDateTime.now();
      }
      if (this.updatedAt == null) {
         this.updatedAt = LocalDateTime.now();
      }
      if (this.status == null) {
         this.status = false;  // Inactivo por defecto (consistente con dominio)
      }
      if (this.currency == null) {
         this.currency = "PEN";
      }
   }

   @PreUpdate
   protected void onUpdate() {
      if (this.updatedAt == null) {
         this.updatedAt = LocalDateTime.now();
      }
   }
}