package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity;

import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA para RefreshToken (token de 30 días)
 */
@Entity
@Table(name = "refresh_tokens")
public class RefreshTokenEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "token", nullable = false, unique = true, length = 500)
   private String token;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private UserEntity user;

   @Column(name = "expires_at", nullable = false)
   private LocalDateTime expiresAt;

   @Column(name = "revoked", nullable = false)
   private boolean revoked = false;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
   }

   // Constructors
   public RefreshTokenEntity() {
   }

   // Getters y Setters
   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getToken() {
      return token;
   }

   public void setToken(String token) {
      this.token = token;
   }

   public UserEntity getUser() {
      return user;
   }

   public void setUser(UserEntity user) {
      this.user = user;
   }

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public boolean isRevoked() {
      return revoked;
   }

   public void setRevoked(boolean revoked) {
      this.revoked = revoked;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }
}