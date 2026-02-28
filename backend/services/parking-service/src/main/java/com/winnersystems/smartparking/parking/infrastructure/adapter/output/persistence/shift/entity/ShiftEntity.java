package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Entidad JPA para Shift.
 * Configurada para Oracle Database.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "SHIFTS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShiftEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "shift_seq")
   @SequenceGenerator(
         name = "shift_seq",
         sequenceName = "SEQ_SHIFT",
         allocationSize = 1
   )
   @Column(name = "ID")
   private Long id;

   @Column(name = "NAME", nullable = false, length = 100)
   private String name;

   @Column(name = "CODE", nullable = false, unique = true, length = 50)
   private String code;

   @Column(name = "DESCRIPTION", columnDefinition = "CLOB")
   private String description;

   @Column(name = "START_TIME", nullable = false)
   private LocalTime startTime;

   @Column(name = "END_TIME", nullable = false)
   private LocalTime endTime;

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
   }

   @PreUpdate
   protected void onUpdate() {
      if (this.updatedAt == null) {
         this.updatedAt = LocalDateTime.now();
      }
   }
}