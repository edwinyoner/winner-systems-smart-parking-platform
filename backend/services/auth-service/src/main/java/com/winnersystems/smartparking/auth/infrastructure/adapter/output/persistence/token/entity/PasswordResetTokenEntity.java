package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens", indexes = {
      @Index(name = "idx_token", columnList = "token", unique = true)
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetTokenEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "token", nullable = false, unique = true, length = 36)
   private String token;  // UUID v4 en texto plano

   @Column(name = "user_id", nullable = false)
   private Long userId;

   @Column(name = "expires_at", nullable = false)
   private LocalDateTime expiresAt;

   @Column(name = "used", nullable = false)
   private boolean used = false;

   @Column(name = "used_at")
   private LocalDateTime usedAt;

   @Column(name = "ip_address", length = 45)
   private String ipAddress;

   @Column(name = "user_agent", length = 500)
   private String userAgent;

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
      updatedAt = LocalDateTime.now();
   }

   @PreUpdate
   protected void onUpdate() {
      updatedAt = LocalDateTime.now();
   }
}