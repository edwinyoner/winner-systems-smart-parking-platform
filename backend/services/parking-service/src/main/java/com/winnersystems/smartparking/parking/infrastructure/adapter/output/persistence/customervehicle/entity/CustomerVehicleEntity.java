package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entidad JPA para CustomerVehicle.
 *
 * Mapeo Oracle:
 * - Tabla: CUSTOMER_VEHICLES (mayúsculas para Oracle)
 * - PK: CUSTOMER_VEHICLE_ID con SEQUENCE
 * - UK: CUSTOMER_ID + VEHICLE_ID
 *
 * NO tiene soft delete - histórico permanente
 * NO tiene updatedAt/updatedBy - solo se crea, no se actualiza
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "CUSTOMER_VEHICLES")
@EntityListeners(AuditingEntityListener.class)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerVehicleEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_vehicle_seq")
   @SequenceGenerator(name = "customer_vehicle_seq", sequenceName = "CUSTOMER_VEHICLE_SEQ", allocationSize = 1)
   @Column(name = "CUSTOMER_VEHICLE_ID")
   private Long id;

   @Column(name = "CUSTOMER_ID", nullable = false)
   private Long customerId;

   @Column(name = "VEHICLE_ID", nullable = false)
   private Long vehicleId;

   // ========================= TRACKING OPCIONAL =========================

   @Column(name = "USAGE_COUNT")
   private Integer usageCount;

   // ========================= AUDITORÍA (solo creación) =========================

   @CreatedDate
   @Column(name = "CREATED_AT", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @CreatedBy
   @Column(name = "CREATED_BY", updatable = false)
   private Long createdBy;

   // ========================= LIFECYCLE CALLBACKS =========================

   @PrePersist
   protected void onCreate() {
      this.createdAt = LocalDateTime.now();
      if (this.usageCount == null) {
         this.usageCount = 0;
      }
   }
}