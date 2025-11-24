package com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.token.entity;

import com.winnersystems.smartparking.auth.domain.enums.TokenType;
import com.winnersystems.smartparking.auth.infrastructure.adapter.output.persistence.user.entity.UserEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entidad JPA para PasswordResetToken (token de 1 hora)
 */
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetTokenEntity {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "token", nullable = false, unique = true, length = 500)
   private String token;

   @ManyToOne(fetch = FetchType.LAZY)
   @JoinColumn(name = "user_id", nullable = false)
   private UserEntity user;

   @Enumerated(EnumType.STRING)
   @Column(name = "token_type", nullable = false, length = 50)
   private TokenType tokenType;

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

   @PrePersist
   protected void onCreate() {
      createdAt = LocalDateTime.now();
   }

   // Constructors
   public PasswordResetTokenEntity() {
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

   public TokenType getTokenType() {
      return tokenType;
   }

   public void setTokenType(TokenType tokenType) {
      this.tokenType = tokenType;
   }

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public boolean isUsed() {
      return used;
   }

   public void setUsed(boolean used) {
      this.used = used;
   }

   public LocalDateTime getUsedAt() {
      return usedAt;
   }

   public void setUsedAt(LocalDateTime usedAt) {
      this.usedAt = usedAt;
   }

   public String getIpAddress() {
      return ipAddress;
   }

   public void setIpAddress(String ipAddress) {
      this.ipAddress = ipAddress;
   }

   public String getUserAgent() {
      return userAgent;
   }

   public void setUserAgent(String userAgent) {
      this.userAgent = userAgent;
   }

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }
}