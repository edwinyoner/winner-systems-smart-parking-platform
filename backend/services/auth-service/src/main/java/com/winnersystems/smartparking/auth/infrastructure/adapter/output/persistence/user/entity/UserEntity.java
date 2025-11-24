package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity;

import com.winnersystems.smartparking.auth.domain.enums.UserStatus;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.role.entity.RoleEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entidad JPA que representa un Usuario en la BASE DE DATOS.
 *
 * DIFERENCIA CON User (domain):
 * - User (domain): Lógica de negocio, POJO puro
 * - UserEntity (infra): Mapeo a BD, anotaciones JPA
 *
 * Estas dos entidades están SEPARADAS intencionalmente (Hexagonal Architecture).
 */
@Entity
@Table(name = "users")
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

   @Enumerated(EnumType.STRING)
   @Column(name = "status", nullable = false, length = 20)
   private UserStatus status;

   @Column(name = "email_verified", nullable = false)
   private boolean emailVerified = false;

   @Column(name = "deleted", nullable = false)
   private boolean deleted = false;

   @Column(name = "deleted_at")
   private LocalDateTime deletedAt;

   @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
   @JoinTable(
         name = "user_roles",
         joinColumns = @JoinColumn(name = "user_id"),
         inverseJoinColumns = @JoinColumn(name = "role_id")
   )
   private Set<RoleEntity> roles = new HashSet<>();

   @Column(name = "last_login_at")
   private LocalDateTime lastLoginAt;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   // ========== LIFECYCLE CALLBACKS ==========

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
      if (status == null) {
         status = UserStatus.PENDING;
      }
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }

   // ========== CONSTRUCTORS ==========

   public UserEntity() {
   }

   public UserEntity(String firstName, String lastName, String email, String password) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.password = password;
      this.status = UserStatus.PENDING;
   }

   // ========== GETTERS Y SETTERS ==========

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getProfilePicture() {
      return profilePicture;
   }

   public void setProfilePicture(String profilePicture) {
      this.profilePicture = profilePicture;
   }

   public UserStatus getStatus() {
      return status;
   }

   public void setStatus(UserStatus status) {
      this.status = status;
   }

   public boolean isEmailVerified() {
      return emailVerified;
   }

   public void setEmailVerified(boolean emailVerified) {
      this.emailVerified = emailVerified;
   }

   public boolean isDeleted() {
      return deleted;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }

   public LocalDateTime getDeletedAt() {
      return deletedAt;
   }

   public void setDeletedAt(LocalDateTime deletedAt) {
      this.deletedAt = deletedAt;
   }

   public Set<RoleEntity> getRoles() {
      return roles;
   }

   public void setRoles(Set<RoleEntity> roles) {
      this.roles = roles;
   }

   public LocalDateTime getLastLoginAt() {
      return lastLoginAt;
   }

   public void setLastLoginAt(LocalDateTime lastLoginAt) {
      this.lastLoginAt = lastLoginAt;
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