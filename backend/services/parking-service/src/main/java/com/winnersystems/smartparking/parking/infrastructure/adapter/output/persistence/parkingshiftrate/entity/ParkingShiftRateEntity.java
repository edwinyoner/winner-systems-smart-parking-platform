package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para ParkingShiftRate.
 * Configurada para Oracle Database.
 *
 * Representa la configuración de tarifas por turno a nivel de PARQUEO.
 * - Un parqueo puede tener 1, 2 o 3 turnos (Mañana, Tarde, Noche)
 * - Cada turno tiene UNA ÚNICA tarifa asignada
 * - No puede haber turnos duplicados (constraint única en PARKING_ID + SHIFT_ID)
 *
 * Ejemplo:
 * - Parking "Simón Bolívar" + Turno Mañana → Tarifa Plana (S/. 2.00)
 * - Parking "Simón Bolívar" + Turno Tarde → Tarifa Plana (S/. 2.00)
 * - Parking "Simón Bolívar" + Turno Noche → Tarifa Nocturna (S/. 1.50)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(
      name = "PARKING_SHIFT_RATES",
      uniqueConstraints = @UniqueConstraint(
            name = "UK_PARKING_SHIFT",
            columnNames = {"PARKING_ID", "SHIFT_ID"}  // ✅ CORREGIDO: Solo parking + shift
      )
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParkingShiftRateEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "parking_shift_rate_seq")
   @SequenceGenerator(
         name = "parking_shift_rate_seq",
         sequenceName = "SEQ_PARKING_SHIFT_RATE",
         allocationSize = 1
   )
   @Column(name = "ID")
   private Long id;

   @Column(name = "PARKING_ID", nullable = false)
   private Long parkingId;

   @Column(name = "SHIFT_ID", nullable = false)
   private Long shiftId;

   @Column(name = "RATE_ID", nullable = false)
   private Long rateId;

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
         this.status = true;  // Activo por defecto
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}