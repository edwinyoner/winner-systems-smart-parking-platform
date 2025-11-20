package com.winnersystems.smartparking.auth.domain.model;

import com.winnersystems.smartparking.auth.domain.enums.TokenType;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio que representa un TOKEN PARA RESTABLECER CONTRASEÑA.
 *
 * Flujo:
 * 1. Usuario olvida su contraseña
 * 2. Click en "Olvidé mi contraseña"
 * 3. Ingresa su email
 * 4. Sistema genera token y envía email:
 *    https://smartparking.com/reset-password?token=xyz789
 * 5. Usuario hace click, ingresa nueva contraseña
 * 6. Sistema valida token, cambia contraseña
 * 7. Token se marca como "usado" (solo se puede usar 1 vez)
 */
public class PasswordResetToken {
   private Long id;
   private String token;                       // Token único (UUID)
   private User user;                          // Usuario dueño del token
   private TokenType tokenType;                // PASSWORD_RESET
   private LocalDateTime expiresAt;            // Expira en 1 hora (por seguridad)
   private boolean used;                       // ¿Ya fue usado?
   private LocalDateTime usedAt;               // Cuándo se usó
   private String ipAddress;                   // IP desde donde se solicitó
   private String userAgent;                   // Navegador/dispositivo
   private LocalDateTime createdAt;

   // ==================== CONSTRUCTORES ====================

   /**
    * Constructor vacío - Genera token automáticamente
    */
   public PasswordResetToken() {
      this.token = UUID.randomUUID().toString();
      this.tokenType = TokenType.PASSWORD_RESET;
      this.used = false;
      this.createdAt = LocalDateTime.now();
      this.expiresAt = LocalDateTime.now().plusHours(tokenType.getValidityHours());
   }

   /**
    * Constructor con usuario
    */
   public PasswordResetToken(User user) {
      this();
      this.user = user;
   }

   /**
    * Constructor completo con información de seguridad
    */
   public PasswordResetToken(User user, String ipAddress, String userAgent) {
      this(user);
      this.ipAddress = ipAddress;
      this.userAgent = userAgent;
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
    * Marca el token como usado (se llama al cambiar la contraseña)
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
    * Verifica si el token fue creado recientemente (últimos 5 minutos)
    * Útil para prevenir múltiples solicitudes de reset
    * @return true si fue creado hace menos de 5 minutos
    */
   public boolean isRecentlyCreated() {
      LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
      return createdAt.isAfter(fiveMinutesAgo);
   }

   /**
    * Verifica si el token expira pronto (menos de 10 minutos)
    * @return true si expira en menos de 10 minutos
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getMinutesUntilExpiration() < 10;
   }

   /**
    * Valida que la IP coincida (seguridad adicional)
    * @param requestIp IP de la solicitud actual
    * @return true si coincide con la IP que solicitó el reset
    */
   public boolean isValidIp(String requestIp) {
      return this.ipAddress != null && this.ipAddress.equals(requestIp);
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

   // ==================== EQUALS, HASHCODE Y TOSTRING ====================

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PasswordResetToken that = (PasswordResetToken) o;
      return Objects.equals(id, that.id) &&
            Objects.equals(token, that.token);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, token);
   }

   @Override
   public String toString() {
      return "PasswordResetToken{" +
            "id=" + id +
            ", token='***'" + // No mostrar token completo por seguridad
            ", userId=" + (user != null ? user.getId() : null) +
            ", tokenType=" + tokenType +
            ", expiresAt=" + expiresAt +
            ", used=" + used +
            ", ipAddress='" + ipAddress + '\'' +
            ", minutesLeft=" + getMinutesUntilExpiration() +
            '}';
   }
}