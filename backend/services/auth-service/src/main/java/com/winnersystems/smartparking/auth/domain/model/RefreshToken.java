package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un REFRESH TOKEN en el sistema.
 *
 * <p>Los refresh tokens permiten renovar access tokens (JWT) sin reautenticación:</p>
 * <ul>
 *   <li>Access Token (JWT): Duración corta (15-30 minutos)</li>
 *   <li>Refresh Token: Duración larga (30 días / 720 horas)</li>
 * </ul>
 *
 * <p><b>Flujo de uso:</b></p>
 * <ol>
 *   <li>Usuario hace login → recibe access token + refresh token</li>
 *   <li>Access token expira → usa refresh token para obtener nuevo access token</li>
 *   <li>Logout → refresh token se revoca</li>
 * </ol>
 *
 * <p><b>Seguridad:</b></p>
 * El token es un UUID v4 (128 bits de entropía = 2^122 combinaciones posibles).
 * Imposible de adivinar, se almacena en texto plano en BD con índice único.
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class RefreshToken {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String token;                                 // UUID v4 en texto plano
   private Long userId;                                  // ID del usuario dueño

   // ========================= CAMPOS DE ESTADO Y VALIDEZ =========================

   private LocalDateTime issuedAt;                       // Cuándo se emitió
   private LocalDateTime expiresAt;                      // Fecha de expiración (30 días)
   private boolean revoked;                              // ¿Fue revocado? (logout)
   private LocalDateTime revokedAt;                      // Cuándo se revocó

   // ========================= CAMPOS DE TRACKING (Multi-device) =========================

   private String deviceInfo;                            // User-Agent del dispositivo
   private String ipAddress;                             // IP desde donde se creó

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                      // Cuándo se creó
   private Long createdBy;                               // ID del usuario que lo creó
   private LocalDateTime updatedAt;                      // Última actualización
   private Long updatedBy;                               // ID del usuario que actualizó

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    */
   public RefreshToken() {
      this.revoked = false;
      this.issuedAt = LocalDateTime.now();
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con token y usuario.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario dueño
    * @param validityHours horas de validez (30 días = 720 horas recomendado)
    */
   public RefreshToken(String token, Long userId, int validityHours) {
      this();
      this.token = token;
      this.userId = userId;
      this.expiresAt = LocalDateTime.now().plusHours(validityHours);
   }

   /**
    * Constructor completo con tracking.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario
    * @param validityHours horas de validez
    * @param deviceInfo User-Agent del dispositivo
    * @param ipAddress IP del cliente
    */
   public RefreshToken(String token, Long userId, int validityHours,
                       String deviceInfo, String ipAddress) {
      this(token, userId, validityHours);
      this.deviceInfo = deviceInfo;
      this.ipAddress = ipAddress;
   }

   // ========================= MÉTODOS DE NEGOCIO - VALIDACIÓN =========================

   /**
    * Verifica si el token ha expirado.
    *
    * @return true si la fecha actual es posterior a expiresAt
    */
   public boolean isExpired() {
      return LocalDateTime.now().isAfter(expiresAt);
   }

   /**
    * Verifica si el token es válido (no revocado y no expirado).
    *
    * @return true si el token puede usarse para renovar access token
    */
   public boolean isValid() {
      return !revoked && !isExpired();
   }

   /**
    * Verifica si el token está revocado.
    *
    * @return true si fue revocado manualmente
    */
   public boolean isRevoked() {
      return revoked;
   }

   /**
    * Verifica si el token pertenece a un usuario específico.
    *
    * @param userId ID del usuario a verificar
    * @return true si el token es de ese usuario
    */
   public boolean belongsTo(Long userId) {
      return this.userId != null && this.userId.equals(userId);
   }

   // ========================= MÉTODOS DE NEGOCIO - REVOCACIÓN =========================

   /**
    * Revoca el token (se usa al hacer logout).
    * Un token revocado no se puede usar aunque no haya expirado.
    */
   public void revoke() {
      this.revoked = true;
      this.revokedAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si puede ser revocado.
    * Solo tokens válidos pueden revocarse.
    *
    * @return true si puede revocarse
    */
   public boolean canBeRevoked() {
      return !revoked && !isExpired();
   }

   // ========================= MÉTODOS DE NEGOCIO - CÁLCULOS DE TIEMPO =========================

   /**
    * Calcula los días restantes hasta expirar.
    *
    * @return días restantes (0 si ya expiró)
    */
   public long getDaysUntilExpiration() {
      if (isExpired()) {
         return 0;
      }
      return java.time.Duration.between(LocalDateTime.now(), expiresAt).toDays();
   }

   /**
    * Calcula las horas restantes hasta expirar.
    *
    * @return horas restantes (0 si ya expiró)
    */
   public long getHoursUntilExpiration() {
      if (isExpired()) {
         return 0;
      }
      return java.time.Duration.between(LocalDateTime.now(), expiresAt).toHours();
   }

   /**
    * Verifica si el token está próximo a expirar (menos de 24 horas).
    * Útil para mostrar advertencias al usuario.
    *
    * @return true si expira en menos de 24 horas
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getHoursUntilExpiration() < 24;
   }

   /**
    * Calcula cuánto tiempo lleva activo el token.
    *
    * @return días desde emisión
    */
   public long getDaysSinceIssued() {
      return java.time.Duration.between(issuedAt, LocalDateTime.now()).toDays();
   }

   // ========================= MÉTODOS DE NEGOCIO - TRACKING =========================

   /**
    * Verifica si tiene información de dispositivo.
    *
    * @return true si deviceInfo está configurado
    */
   public boolean hasDeviceInfo() {
      return deviceInfo != null && !deviceInfo.isEmpty();
   }

   /**
    * Verifica si tiene información de IP.
    *
    * @return true si ipAddress está configurado
    */
   public boolean hasIpAddress() {
      return ipAddress != null && !ipAddress.isEmpty();
   }

   /**
    * Actualiza el usuario que modificó este registro.
    * Útil para auditoría.
    *
    * @param userId ID del usuario que modifica
    */
   public void updateModifiedBy(Long userId) {
      this.updatedBy = userId;
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= GETTERS Y SETTERS =========================

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

   public Long getUserId() {
      return userId;
   }

   public void setUserId(Long userId) {
      this.userId = userId;
   }

   public LocalDateTime getIssuedAt() {
      return issuedAt;
   }

   public void setIssuedAt(LocalDateTime issuedAt) {
      this.issuedAt = issuedAt;
   }

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public boolean getRevoked() {
      return revoked;
   }

   // NO HAY setRevoked() público - usar revoke()

   public LocalDateTime getRevokedAt() {
      return revokedAt;
   }

   public void setRevokedAt(LocalDateTime revokedAt) {
      this.revokedAt = revokedAt;
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

   public LocalDateTime getCreatedAt() {
      return createdAt;
   }

   public void setCreatedAt(LocalDateTime createdAt) {
      this.createdAt = createdAt;
   }

   public Long getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(Long createdBy) {
      this.createdBy = createdBy;
   }

   public LocalDateTime getUpdatedAt() {
      return updatedAt;
   }

   public void setUpdatedAt(LocalDateTime updatedAt) {
      this.updatedAt = updatedAt;
   }

   public Long getUpdatedBy() {
      return updatedBy;
   }

   public void setUpdatedBy(Long updatedBy) {
      this.updatedBy = updatedBy;
   }

   // ========================= EQUALS, HASHCODE Y TOSTRING =========================

   /**
    * Dos refresh tokens son iguales si tienen el mismo ID y token.
    * Solo se usan campos únicos/inmutables.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RefreshToken that = (RefreshToken) o;
      return Objects.equals(id, that.id) && Objects.equals(token, that.token);
   }

   /**
    * HashCode basado en ID y token únicamente.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, token);
   }

   /**
    * ToString simplificado para logging y debugging.
    * NO muestra el token completo por seguridad (solo primeros y últimos 4 caracteres).
    */
   @Override
   public String toString() {
      String maskedToken = token != null && token.length() > 8
            ? token.substring(0, 4) + "..." + token.substring(token.length() - 4)
            : "***";

      return "RefreshToken{" +
            "id=" + id +
            ", token='" + maskedToken + '\'' +
            ", userId=" + userId +
            ", issuedAt=" + issuedAt +
            ", expiresAt=" + expiresAt +
            ", revoked=" + revoked +
            ", daysLeft=" + getDaysUntilExpiration() +
            ", deviceInfo='" + (deviceInfo != null ? deviceInfo.substring(0, Math.min(20, deviceInfo.length())) + "..." : null) + '\'' +
            '}';
   }
}