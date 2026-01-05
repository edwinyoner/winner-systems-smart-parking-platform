package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un ROL en la base de datos.
 * Mapea directamente con el modelo de dominio Role.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name", nullable = false, unique = true, length = 100)
   private String name;  // "ADMIN", "AUTORIDAD", "OPERADOR"

   @Column(name = "description", length = 255)
   private String description;

   @Column(name = "status", nullable = false)
   private boolean status = true;

   // ========== RELACIONES ==========

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
         name = "role_permissions",
         joinColumns = @JoinColumn(name = "role_id"),
         inverseJoinColumns = @JoinColumn(name = "permission_id")
   )
   @Builder.Default
   private Set<PermissionEntity> permissions = new HashSet<>();

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
}