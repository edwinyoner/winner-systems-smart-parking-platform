package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad JPA para Customer.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "customers",
      uniqueConstraints = @UniqueConstraint(columnNames = {"document_type_id", "document_number"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "document_type_id", nullable = false)
   private Long documentTypeId;

   @Column(name = "document_number", nullable = false, length = 20)
   private String documentNumber;

   @Column(name = "first_name", length = 100)
   private String firstName;

   @Column(name = "last_name", length = 100)
   private String lastName;

   @Column(name = "phone", length = 20)
   private String phone;

   @Column(name = "email", length = 100)
   private String email;

   @Column(name = "address", length = 255)
   private String address;

   @Column(name = "registration_date")
   private LocalDateTime registrationDate;

   @Column(name = "first_seen_date")
   private LocalDateTime firstSeenDate;

   @Column(name = "last_seen_date")
   private LocalDateTime lastSeenDate;

   @Column(name = "total_visits")
   private Integer totalVisits;

   @Column(name = "auth_external_id")
   private Long authExternalId;

   // ========================= CAMPOS DE AUDITORÍA (igual que en UserEntity) =========================

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
      if (this.registrationDate == null) {
         this.registrationDate = now;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      this.updatedAt = LocalDateTime.now();
   }
}