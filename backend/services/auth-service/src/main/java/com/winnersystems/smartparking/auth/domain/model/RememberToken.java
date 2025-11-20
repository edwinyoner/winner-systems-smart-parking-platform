package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio que representa un REMEMBER TOKEN.
 *
 * ¿Para qué sirve?
 * Cuando el usuario marca "Recuérdame" al hacer login:
 * 1. Sistema genera un RememberToken (duración larga: 90 días)
 * 2. Se guarda en BD y en cookie del navegador
 * 3. Usuario puede volver días/semanas después
 * 4. Sistema lo reconoce automáticamente sin pedir login
 *
 * Diferencia con RefreshToken:
 * - RefreshToken: Renovar access token (corto plazo)
 * - RememberToken: Mantener sesión activa (largo plazo)
 */
public class RememberToken {
   private Long id;
   private String token;                       // Token único (UUID)
   private User user;                          // Usuario dueño del token
   private String deviceInfo;                  // Información del dispositivo
   private String ipAddress;                   // IP desde donde se generó
   private String userAgent;                   // Navegador/app
   private LocalDateTime lastUsedAt;           // Última vez que se usó
   private LocalDateTime expiresAt;            // Expira en 90 días
   private boolean revoked;                    // ¿Fue revocado?
   private LocalDateTime createdAt;

   // ==================== CONSTRUCTORES ====================

   /**
    * Constructor vacío - Genera token automáticamente
    */
   public RememberToken() {
      this.token = UUID.randomUUID().toString();
      this.revoked = false;
      this.createdAt = LocalDateTime.now();
      this.lastUsedAt = LocalDateTime.now();
      this.expiresAt = LocalDateTime.now().plusDays(90); // 90 días por defecto
   }

   /**
    * Constructor con usuario
    */
   public RememberToken(User user) {
      this();
      this.user = user;
   }

   /**
    * Constructor completo con información de dispositivo
    */
   public RememberToken(User user, String deviceInfo, String ipAddress, String userAgent) {
      this(user);
      this.deviceInfo = deviceInfo;
      this.ipAddress = ipAddress;
      this.userAgent = userAgent;
   }

   /**
    * Constructor con duración personalizada
    */
   public RememberToken(User user, int validityDays) {
      this(user);
      this.expiresAt = LocalDateTime.now().plusDays(validityDays);
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
    * Verifica si el token es válido (no revocado y no expirado)
    * @return true si el token puede usarse
    */
   public boolean isValid() {
      return !revoked && !isExpired();
   }

   /**
    * Revoca el token (se usa al hacer logout)
    * Un token revocado no se puede usar aunque no haya expirado
    */
   public void revoke() {
      this.revoked = true;
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
    * Actualiza la fecha de último uso
    * Se llama cada vez que el usuario accede con este token
    */
   public void updateLastUsed() {
      this.lastUsedAt = LocalDateTime.now();
   }

   /**
    * Extiende la validez del token (si está válido)
    * Útil para "renovar" el remember token automáticamente
    * @param additionalDays días adicionales de validez
    */
   public void extendValidity(int additionalDays) {
      if (!revoked && !isExpired()) {
         this.expiresAt = this.expiresAt.plusDays(additionalDays);
      }
   }

   /**
    * Calcula los días restantes hasta expirar
    * @return días restantes (0 si ya expiró)
    */
   public long getDaysUntilExpiration() {
      if (isExpired()) {
         return 0;
      }
      return java.time.Duration.between(LocalDateTime.now(), expiresAt).toDays();
   }

   /**
    * Verifica si el token está cerca de expirar (menos de 7 días)
    * @return true si expira en menos de 7 días
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getDaysUntilExpiration() < 7;
   }

   /**
    * Verifica si el token es reciente (menos de 1 día)
    * @return true si fue creado hace menos de 1 día
    */
   public boolean isRecent() {
      LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
      return createdAt.isAfter(oneDayAgo);
   }

   /**
    * Verifica si ha sido usado recientemente (últimas 24 horas)
    * @return true si fue usado en las últimas 24 horas
    */
   public boolean isActivelyUsed() {
      LocalDateTime oneDayAgo = LocalDateTime.now().minusDays(1);
      return lastUsedAt.isAfter(oneDayAgo);
   }

   /**
    * Valida que la IP coincida (seguridad adicional opcional)
    * @param requestIp IP de la solicitud actual
    * @return true si coincide con la IP original
    */
   public boolean isValidIp(String requestIp) {
      return this.ipAddress != null && this.ipAddress.equals(requestIp);
   }

   /**
    * Valida que el User-Agent coincida (seguridad adicional opcional)
    * @param requestUserAgent User-Agent de la solicitud actual
    * @return true si coincide con el User-Agent original
    */
   public boolean isValidUserAgent(String requestUserAgent) {
      return this.userAgent != null && this.userAgent.equals(requestUserAgent);
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

   public String getDeviceInfo() {
      return deviceInfo;
   }

   public void setDeviceInfo(String deviceInfo) {
      this.deviceInfo = deviceInfo;
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

   public LocalDateTime getLastUsedAt() {
      return lastUsedAt;
   }

   public void setLastUsedAt(LocalDateTime lastUsedAt) {
      this.lastUsedAt = lastUsedAt;
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

   // ==================== EQUALS, HASHCODE Y TOSTRING ====================

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RememberToken that = (RememberToken) o;
      return Objects.equals(id, that.id) &&
            Objects.equals(token, that.token);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, token);
   }

   @Override
   public String toString() {
      return "RememberToken{" +
            "id=" + id +
            ", token='***'" + // No mostrar token completo por seguridad
            ", userId=" + (user != null ? user.getId() : null) +
            ", deviceInfo='" + deviceInfo + '\'' +
            ", lastUsedAt=" + lastUsedAt +
            ", expiresAt=" + expiresAt +
            ", revoked=" + revoked +
            ", daysLeft=" + getDaysUntilExpiration() +
            '}';
   }
}