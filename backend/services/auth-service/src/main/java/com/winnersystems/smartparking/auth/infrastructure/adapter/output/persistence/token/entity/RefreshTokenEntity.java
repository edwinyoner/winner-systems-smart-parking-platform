package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_tokens", indexes = {
      @Index(name = "idx_token", columnList = "token", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "token", nullable = false, unique = true, length = 36)
   private String token;  // UUID v4 en texto plano

   @Column(name = "user_id", nullable = false)
   private Long userId;

   @Column(name = "issued_at", nullable = false)
   private LocalDateTime issuedAt;

   @Column(name = "expires_at", nullable = false)
   private LocalDateTime expiresAt;

   @Column(name = "revoked", nullable = false)
   private boolean revoked = false;

   @Column(name = "revoked_at")
   private LocalDateTime revokedAt;

   @Column(name = "device_info", length = 500)
   private String deviceInfo;

   @Column(name = "ip_address", length = 45)
   private String ipAddress;

   @Column(name = "created_at", nullable = false, updatable = false)
   private LocalDateTime createdAt;

   @Column(name = "created_by")
   private Long createdBy;

   @Column(name = "updated_at", nullable = false)
   private LocalDateTime updatedAt;

   @Column(name = "updated_by")
   private Long updatedBy;

   @PrePersist
   protected void onCreate() {
      if (createdAt == null) createdAt = LocalDateTime.now();
      if (issuedAt == null) issuedAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}