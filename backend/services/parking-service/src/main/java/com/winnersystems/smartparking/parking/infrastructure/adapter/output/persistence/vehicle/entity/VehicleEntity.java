package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity;

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

import java.time.LocalDateTime;

/**
 * Entidad JPA para Vehicle.
 *
 * Mapeo Oracle:
 * - Tabla: VEHICLES (mayúsculas para Oracle)
 * - PK: VEHICLE_ID con SEQUENCE
 * - UK: LICENSE_PLATE
 *
 * SÍ tiene soft delete (deletedAt, deletedBy)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "VEHICLES")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "vehicle_seq")
   @SequenceGenerator(name = "vehicle_seq", sequenceName = "VEHICLE_SEQ", allocationSize = 1)
   @Column(name = "VEHICLE_ID")
   private Long id;

   @Column(name = "LICENSE_PLATE", nullable = false, unique = true, length = 20)
   private String licensePlate;

   // ========================= TRACKING =========================

   @Column(name = "FIRST_SEEN_DATE")
   private LocalDateTime firstSeenDate;

   @Column(name = "LAST_SEEN_DATE")
   private LocalDateTime lastSeenDate;

   @Column(name = "TOTAL_VISITS")
   private Integer totalVisits;

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
      if (this.firstSeenDate == null) {
         this.firstSeenDate = now;
      }
      if (this.lastSeenDate == null) {
         this.lastSeenDate = now;
      }
      if (this.totalVisits == null) {
         this.totalVisits = 0;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}