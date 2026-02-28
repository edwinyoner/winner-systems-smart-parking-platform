package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para Vehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "vehicles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "license_plate", nullable = false, unique = true, length = 20)
   private String licensePlate;

   @Column(name = "color", length = 50)
   private String color;

   @Column(name = "brand", length = 50)
   private String brand;

   @Column(name = "first_seen_date")
   private LocalDateTime firstSeenDate;

   @Column(name = "last_seen_date")
   private LocalDateTime lastSeenDate;

   @Column(name = "total_visits")
   private Integer totalVisits;

   // ========================= CAMPOS DE AUDITORÍA (igual que en UserEntity, CustomerEntity, etc.) =========================

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
      if (this.totalVisits == null) {
         this.totalVisits = 0;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}