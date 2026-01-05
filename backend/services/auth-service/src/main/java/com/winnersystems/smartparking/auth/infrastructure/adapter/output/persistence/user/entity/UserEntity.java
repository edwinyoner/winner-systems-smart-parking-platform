package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un Usuario en la base de datos.
 * Mapea directamente con el modelo de dominio User.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "first_name", nullable = false, length = 100)
   private String firstName;

   @Column(name = "last_name", nullable = false, length = 100)
   private String lastName;

   @Column(name = "email", nullable = false, unique = true, length = 255)
   private String email;

   @Column(name = "password", nullable = false, length = 255)
   private String password;

   @Column(name = "phone_number", length = 20)
   private String phoneNumber;

   @Column(name = "profile_picture", length = 500)
   private String profilePicture;

   @Column(name = "status", nullable = false)
   private boolean status = false;  // false por defecto hasta verificar email

   @Column(name = "email_verified", nullable = false)
   private boolean emailVerified = false;

   // ========== RELACIONES ==========

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
         name = "user_roles",
         joinColumns = @JoinColumn(name = "user_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   @Builder.Default
   private Set<RoleEntity> roles = new HashSet<>();

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