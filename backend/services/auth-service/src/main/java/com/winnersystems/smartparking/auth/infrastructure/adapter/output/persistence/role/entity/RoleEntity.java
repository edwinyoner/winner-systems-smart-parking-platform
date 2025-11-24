package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.permission.entity.PermissionEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un ROL en la base de datos.
 */
@Entity
@Table(name = "roles")
public class RoleEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Enumerated(EnumType.STRING)
   @Column(name = "role_type", nullable = false, unique = true, length = 50)
   private RoleType roleType;

   @Column(name = "description", length = 255)
   private String description;

   @Column(name = "status", nullable = false)
   private boolean status = true;

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
         name = "role_permissions",
         joinColumns = @JoinColumn(name = "role_id"),
         inverseJoinColumns = @JoinColumn(name = "permission_id")
   )
   private Set<PermissionEntity> permissions = new HashSet<>();

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   // Constructors
   public RoleEntity() {
   }

   public RoleEntity(RoleType roleType, String description) {
      this.roleType = roleType;
      this.description = description;
   }

   // Getters y Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public RoleType getRoleType() {
      return roleType;
   }

   public void setRoleType(RoleType roleType) {
      this.roleType = roleType;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public boolean isStatus() {
      return status;
   }

   public void setStatus(boolean status) {
      this.status = status;
   }

   public Set<PermissionEntity> getPermissions() {
      return permissions;
   }

   public void setPermissions(Set<PermissionEntity> permissions) {
      this.permissions = permissions;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public LocalDateTime getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
   }
}