package com.winnersystems.smartparking.auth.domain.model;

import com.winnersystems.smartparking.auth.domain.enums.TokenType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio que representa un TOKEN DE VERIFICACIÓN DE EMAIL.
 *
 * Flujo:
 * 1. Usuario se registra con email: edwin@example.com
 * 2. Sistema genera un token y envía email:
 *    https://smartparking.com/verify?token=abc123
 * 3. Usuario hace click en el link
 * 4. Sistema valida el token y marca el email como verificado ✓
 * 5. Token se marca como "usado" (solo se puede usar 1 vez)
 */
public class VerificationToken {
   private Long id;
   private String token;                       // Token único (UUID)
   private User user;                          // Usuario dueño del token
   private TokenType tokenType;                // EMAIL_VERIFICATION
   private LocalDateTime expiresAt;            // Expira en 24 horas
   private boolean used;                       // ¿Ya fue usado?
   private LocalDateTime usedAt;               // Cuándo se usó
   private LocalDateTime createdAt;

   // ==================== CONSTRUCTORES ====================

   /**
    * Constructor vacío - Genera token automáticamente
    */
   public VerificationToken() {
      this.token = UUID.randomUUID().toString();
      this.tokenType = TokenType.EMAIL_VERIFICATION;
      this.used = false;
      this.createdAt = LocalDateTime.now();
      this.expiresAt = LocalDateTime.now().plusHours(tokenType.getValidityHours());
   }

   /**
    * Constructor con usuario
    */
   public VerificationToken(User user) {
      this();
      this.user = user;
   }

   // ==================== MÉTODOS DE NEGOCIO ====================

   /**
    * Verifica si el token ha expirado
    * @return true si la fecha actual es posterior a expiresAt
    */
   public boolean isExpired() {
      return LocalDateTime.now().isAfter(expiresAt);
   }

   /**
    * Verifica si el token es válido para usar
    * @return true si NO fue usado y NO expiró
    */
   public boolean isValid() {
      return !used && !isExpired();
   }

   /**
    * Marca el token como usado (se llama al verificar el email)
    * Un token usado no se puede volver a usar
    */
   public void markAsUsed() {
      this.used = true;
      this.usedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el token pertenece al usuario
    * @param user usuario a verificar
    * @return true si el token es de ese usuario
    */
   public boolean belongsTo(User user) {
      return this.user != null && this.user.equals(user);
   }

   /**
    * Calcula el tiempo restante antes de expirar (en minutos)
    * @return minutos restantes (0 si ya expiró)
    */
   public long getMinutesUntilExpiration() {
      if (isExpired()) {
         return 0;
      }
      return java.time.Duration.between(LocalDateTime.now(), expiresAt).toMinutes();
   }

   /**
    * Verifica si el token expira pronto (menos de 1 hora)
    * @return true si expira en menos de 1 hora
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getMinutesUntilExpiration() < 60;
   }

   // ==================== GETTERS Y SETTERS ====================

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

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
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

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   // ==================== EQUALS, HASHCODE Y TOSTRING ====================

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      VerificationToken that = (VerificationToken) o;
      return Objects.equals(id, that.id) &&
            Objects.equals(token, that.token);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, token);
   }

   @Override
   public String toString() {
      return "VerificationToken{" +
            "id=" + id +
            ", token='***'" + // No mostrar token completo
            ", userId=" + (user != null ? user.getId() : null) +
            ", tokenType=" + tokenType +
            ", expiresAt=" + expiresAt +
            ", used=" + used +
            ", minutesLeft=" + getMinutesUntilExpiration() +
            '}';
   }
}