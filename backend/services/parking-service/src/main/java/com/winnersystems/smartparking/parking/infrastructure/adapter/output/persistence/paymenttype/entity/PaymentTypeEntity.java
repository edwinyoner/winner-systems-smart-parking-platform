package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "PAYMENT_TYPES")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentTypeEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_type_seq")
   @SequenceGenerator(
         name = "payment_type_seq",
         sequenceName = "SEQ_PAYMENT_TYPE",
         allocationSize = 1
   )
   @Column(name = "ID")
   private Long id;

   @Column(name = "CODE", nullable = false, unique = true, length = 20)
   private String code;

   @Column(name = "NAME", nullable = false, length = 100)
   private String name;

   @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
   private String description;

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
         this.status = false;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      if (this.updatedAt == null) {
         this.updatedAt = LocalDateTime.now();
      }
   }
}