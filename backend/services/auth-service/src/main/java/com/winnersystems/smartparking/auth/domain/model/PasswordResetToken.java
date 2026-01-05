package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un TOKEN PARA RESTABLECER CONTRASEÑA.
 *
 * <p><b>Flujo de recuperación de contraseña:</b></p>
 * <ol>
 *   <li>Usuario olvida su contraseña y solicita reset</li>
 *   <li>Sistema genera token UUID y envía email con enlace:
 *       https://smartparking.com/reset-password?token=f47ac10b-...</li>
 *   <li>Usuario hace clic y ingresa nueva contraseña</li>
 *   <li>Sistema valida token (no expirado, no usado)</li>
 *   <li>Contraseña se actualiza y token se marca como "usado"</li>
 *   <li>Token solo se puede usar UNA VEZ</li>
 * </ol>
 *
 * <p><b>Características de Seguridad:</b></p>
 * <ul>
 *   <li>Expiración corta: 1 hora (seguridad crítica)</li>
 *   <li>Un solo uso: Se marca como "usado" después de cambiar contraseña</li>
 *   <li>Token UUID v4: 128 bits de entropía, imposible de adivinar</li>
 *   <li>Tracking: IP y User-Agent para auditoría</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class PasswordResetToken {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String token;                                 // UUID v4 en texto plano
   private Long userId;                                  // ID del usuario dueño

   // ========================= CAMPOS DE ESTADO Y VALIDEZ =========================

   private LocalDateTime expiresAt;                      // Expira en 1 hora (seguridad)
   private boolean used;                                 // ¿Ya fue usado?
   private LocalDateTime usedAt;                         // Cuándo se usó

   // ========================= CAMPOS DE SEGURIDAD (Tracking) =========================

   private String ipAddress;                             // IP desde donde se solicitó
   private String userAgent;                             // Navegador/dispositivo

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                      // Cuándo se creó
   private Long createdBy;                               // ID del usuario que creó
   private LocalDateTime updatedAt;                      // Última actualización
   private Long updatedBy;                               // ID del usuario que actualizó

   // ========================= CONSTANTES =========================

   private static final int EXPIRATION_HOURS = 1;        // 1 hora de validez

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    */
   public PasswordResetToken() {
      this.used = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
      this.expiresAt = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
   }

   /**
    * Constructor con token y usuario.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario que solicita reset
    */
   public PasswordResetToken(String token, Long userId) {
      this();
      this.token = token;
      this.userId = userId;
   }

   /**
    * Constructor completo con información de seguridad.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario
    * @param ipAddress IP desde donde se solicitó
    * @param userAgent User-Agent del navegador/dispositivo
    */
   public PasswordResetToken(String token, Long userId, String ipAddress, String userAgent) {
      this(token, userId);
      this.ipAddress = ipAddress;
      this.userAgent = userAgent;
   }

   // ========================= MÉTODOS DE NEGOCIO - VALIDACIÓN =========================

   /**
    * Verifica si el token ha expirado.
    * Los tokens de reset expiran en 1 hora por seguridad.
    *
    * @return true si la fecha actual es posterior a expiresAt
    */
   public boolean isExpired() {
      return LocalDateTime.now().isAfter(expiresAt);
   }

   /**
    * Verifica si el token es válido para usar.
    * Un token es válido si NO fue usado y NO expiró.
    *
    * @return true si puede usarse para resetear contraseña
    */
   public boolean isValid() {
      return !used && !isExpired();
   }

   /**
    * Verifica si el token ya fue usado.
    *
    * @return true si ya se usó para cambiar contraseña
    */
   public boolean isUsed() {
      return used;
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

   /**
    * Verifica si puede ser usado.
    * Alias de isValid() para mejor legibilidad.
    *
    * @return true si puede usarse
    */
   public boolean canBeUsed() {
      return isValid();
   }

   // ========================= MÉTODOS DE NEGOCIO - USO DEL TOKEN =========================

   /**
    * Marca el token como usado.
    * Se llama después de cambiar la contraseña exitosamente.
    * Un token usado no se puede volver a usar (seguridad).
    */
   public void markAsUsed() {
      if (this.used) {
         throw new IllegalStateException("El token ya fue usado previamente");
      }
      if (isExpired()) {
         throw new IllegalStateException("El token ha expirado y no puede ser usado");
      }
      this.used = true;
      this.usedAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el token puede marcarse como usado.
    *
    * @return true si no está usado y no ha expirado
    */
   public boolean canBeMarkedAsUsed() {
      return !used && !isExpired();
   }

   // ========================= MÉTODOS DE NEGOCIO - CÁLCULOS DE TIEMPO =========================

   /**
    * Calcula el tiempo restante antes de expirar (en minutos).
    *
    * @return minutos restantes (0 si ya expiró)
    */
   public long getMinutesUntilExpiration() {
      if (isExpired()) {
         return 0;
      }
      return java.time.Duration.between(LocalDateTime.now(), expiresAt).toMinutes();
   }

   /**
    * Verifica si el token fue creado recientemente (últimos 5 minutos).
    * Útil para prevenir múltiples solicitudes de reset en poco tiempo.
    *
    * @return true si fue creado hace menos de 5 minutos
    */
   public boolean isRecentlyCreated() {
      LocalDateTime fiveMinutesAgo = LocalDateTime.now().minusMinutes(5);
      return createdAt.isAfter(fiveMinutesAgo);
   }

   /**
    * Verifica si el token expira pronto (menos de 10 minutos).
    * Útil para mostrar advertencias en el frontend.
    *
    * @return true si expira en menos de 10 minutos
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getMinutesUntilExpiration() < 10;
   }

   /**
    * Calcula cuántos minutos han pasado desde la creación.
    *
    * @return minutos desde creación
    */
   public long getMinutesSinceCreation() {
      return java.time.Duration.between(createdAt, LocalDateTime.now()).toMinutes();
   }

   // ========================= MÉTODOS DE NEGOCIO - SEGURIDAD =========================

   /**
    * Valida que la IP coincida con la que solicitó el reset.
    * Seguridad adicional para prevenir uso de tokens robados.
    *
    * @param requestIp IP de la solicitud actual
    * @return true si coincide con la IP original
    */
   public boolean isValidIp(String requestIp) {
      return this.ipAddress != null && this.ipAddress.equals(requestIp);
   }

   /**
    * Verifica si tiene información de IP registrada.
    *
    * @return true si ipAddress está configurado
    */
   public boolean hasIpAddress() {
      return ipAddress != null && !ipAddress.isEmpty();
   }

   /**
    * Verifica si tiene información de User-Agent.
    *
    * @return true si userAgent está configurado
    */
   public boolean hasUserAgent() {
      return userAgent != null && !userAgent.isEmpty();
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

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public boolean getUsed() {
      return used;
   }

   // NO HAY setUsed() público - usar markAsUsed()

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
    * Dos tokens son iguales si tienen el mismo ID y token.
    * Solo se usan campos únicos/inmutables.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PasswordResetToken that = (PasswordResetToken) o;
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
    * NO muestra el token completo por seguridad.
    */
   @Override
   public String toString() {
      String maskedToken = token != null && token.length() > 8
            ? token.substring(0, 4) + "..." + token.substring(token.length() - 4)
            : "***";

      return "PasswordResetToken{" +
            "id=" + id +
            ", token='" + maskedToken + '\'' +
            ", userId=" + userId +
            ", expiresAt=" + expiresAt +
            ", used=" + used +
            ", usedAt=" + usedAt +
            ", ipAddress='" + ipAddress + '\'' +
            ", minutesLeft=" + getMinutesUntilExpiration() +
            '}';
   }
}