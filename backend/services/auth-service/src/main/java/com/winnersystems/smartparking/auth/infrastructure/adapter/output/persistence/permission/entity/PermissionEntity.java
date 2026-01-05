package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Entidad JPA que representa un PERMISO en la base de datos.
 * Mapea directamente con el modelo de dominio Permission.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PermissionEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false, unique = true, length = 100)
   private String name;  // Ejemplo: "users.create", "parking.update"

   @Column(name = "description", length = 255)
   private String description;  // Ejemplo: "Crear Usuarios"

   @Column(name = "status", nullable = false)
   private boolean status = true;  // true = activo, false = inactivo

   // ========== AUDITORÍA ==========

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "created_by")
   private Long createdBy;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   @Column(name = "updated_by")
   private Long updatedBy;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;

   @Column(name = "deleted_by")
   private Long deletedBy;

   // ========== LIFECYCLE CALLBACKS ==========

   @PrePersist
   protected void onCreate() {
      if (createdAt == null) {
         createdAt = LocalDateTime.now();
      }
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   // ========== CONSTRUCTORES =========

   // ========== GETTERS Y SETTERS ==========
}