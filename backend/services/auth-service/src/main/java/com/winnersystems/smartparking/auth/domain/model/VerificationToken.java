package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un TOKEN DE VERIFICACIÓN DE EMAIL.
 *
 * <p><b>Flujo de verificación de email:</b></p>
 * <ol>
 *   <li>Usuario se registra con email (o ADMIN crea usuario)</li>
 *   <li>Sistema genera token UUID y envía email con enlace:
 *       https://smartparking.com/verify?token=abc123...</li>
 *   <li>Usuario hace clic en el link</li>
 *   <li>Sistema valida token (no expirado, no verificado)</li>
 *   <li>Email se marca como verificado en User</li>
 *   <li>Token se marca como "verificado"</li>
 * </ol>
 *
 * <p><b>Características:</b></p>
 * <ul>
 *   <li>Expiración: 24 horas (ventana razonable)</li>
 *   <li>Un solo uso: Se marca como "verificado" después de usar</li>
 *   <li>Regenerable: Si expira, se puede reenviar nuevo token</li>
 *   <li>Token UUID v4: 128 bits de entropía, imposible de adivinar</li>
 * </ul>
 *
 * @author Edwin Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class VerificationToken {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String token;                                 // UUID v4 en texto plano
   private Long userId;                                  // ID del usuario dueño

   // ========================= CAMPOS DE ESTADO Y VALIDEZ =========================

   private LocalDateTime expiresAt;                      // Expira en 24 horas
   private LocalDateTime verifiedAt;                     // Cuándo se verificó (null si no verificado)

   // ========================= CAMPOS DE TRACKING (Opcional) =========================

   private String ipAddress;                             // IP desde donde se verificó

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                      // Cuándo se creó
   private Long createdBy;                               // ID del usuario que creó (ADMIN)
   private LocalDateTime updatedAt;                      // Última actualización
   private Long updatedBy;                               // ID del usuario que actualizó

   // ========================= CONSTANTES =========================

   private static final int EXPIRATION_HOURS = 24;       // 24 horas de validez

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    */
   public VerificationToken() {
      this.verifiedAt = null;                           // No verificado aún
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
      this.expiresAt = LocalDateTime.now().plusHours(EXPIRATION_HOURS);
   }

   /**
    * Constructor con token y usuario.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario que necesita verificar email
    */
   public VerificationToken(String token, Long userId) {
      this();
      this.token = token;
      this.userId = userId;
   }

   /**
    * Constructor completo con createdBy.
    *
    * @param token UUID v4 en texto plano
    * @param userId ID del usuario
    * @param createdBy ID del usuario que crea el token (ADMIN o sistema)
    */
   public VerificationToken(String token, Long userId, Long createdBy) {
      this(token, userId);
      this.createdBy = createdBy;
   }

   // ========================= MÉTODOS DE NEGOCIO - VALIDACIÓN =========================

   /**
    * Verifica si el token ha expirado.
    * Los tokens de verificación expiran en 24 horas.
    *
    * @return true si la fecha actual es posterior a expiresAt
    */
   public boolean isExpired() {
      return LocalDateTime.now().isAfter(expiresAt);
   }

   /**
    * Verifica si el token es válido para usar.
    * Un token es válido si NO fue verificado y NO expiró.
    *
    * @return true si puede usarse para verificar email
    */
   public boolean isValid() {
      return !isVerified() && !isExpired();
   }

   /**
    * Verifica si el email ya fue verificado con este token.
    *
    * @return true si ya se usó para verificar
    */
   public boolean isVerified() {
      return verifiedAt != null;
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

   // ========================= MÉTODOS DE NEGOCIO - VERIFICACIÓN =========================

   /**
    * Marca el token como verificado.
    * Se llama después de validar el token y verificar el email.
    *
    * @param ipAddress IP desde donde se verificó (opcional)
    */
   public void markAsVerified(String ipAddress) {
      if (isVerified()) {
         throw new IllegalStateException("El token ya fue usado para verificar el email");
      }
      if (isExpired()) {
         throw new IllegalStateException("El token ha expirado y no puede ser usado");
      }
      this.verifiedAt = LocalDateTime.now();
      this.ipAddress = ipAddress;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Marca el token como verificado sin IP.
    */
   public void markAsVerified() {
      markAsVerified(null);
   }

   /**
    * Verifica si el token puede marcarse como verificado.
    *
    * @return true si no está verificado y no ha expirado
    */
   public boolean canBeMarkedAsVerified() {
      return !isVerified() && !isExpired();
   }

   // ========================= MÉTODOS DE NEGOCIO - CÁLCULOS DE TIEMPO =========================

   /**
    * Calcula el tiempo restante antes de expirar (en horas).
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
    * Verifica si el token expira pronto (menos de 1 hora).
    * Útil para mostrar advertencias al usuario.
    *
    * @return true si expira en menos de 1 hora
    */
   public boolean isExpiringSoon() {
      return !isExpired() && getHoursUntilExpiration() < 1;
   }

   /**
    * Calcula cuánto tiempo ha pasado desde la creación.
    *
    * @return horas desde creación
    */
   public long getHoursSinceCreation() {
      return java.time.Duration.between(createdAt, LocalDateTime.now()).toHours();
   }

   /**
    * Verifica si el token fue creado recientemente (últimas 2 horas).
    * Útil para prevenir múltiples envíos de verificación.
    *
    * @return true si fue creado hace menos de 2 horas
    */
   public boolean isRecentlyCreated() {
      return getHoursSinceCreation() < 2;
   }

   // ========================= MÉTODOS DE NEGOCIO - TRACKING =========================

   /**
    * Verifica si tiene información de IP registrada.
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

   public LocalDateTime getExpiresAt() {
      return expiresAt;
   }

   public void setExpiresAt(LocalDateTime expiresAt) {
      this.expiresAt = expiresAt;
   }

   public LocalDateTime getVerifiedAt() {
      return verifiedAt;
   }

   public void setVerifiedAt(LocalDateTime verifiedAt) {
      this.verifiedAt = verifiedAt;
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
    * Dos tokens son iguales si tienen el mismo ID y token.
    * Solo se usan campos únicos/inmutables.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      VerificationToken that = (VerificationToken) o;
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

      return "VerificationToken{" +
            "id=" + id +
            ", token='" + maskedToken + '\'' +
            ", userId=" + userId +
            ", expiresAt=" + expiresAt +
            ", isVerified=" + isVerified() +
            ", verifiedAt=" + verifiedAt +
            ", hoursLeft=" + getHoursUntilExpiration() +
            '}';
   }
}