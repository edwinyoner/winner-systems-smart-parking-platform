package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad de dominio que representa un REFRESH TOKEN.
 * 1. Access Token (JWT) -> dura 15 minutos.
 * 2. Refresh Token -> dura 30 días.
 *
 * Cuando el Access Token expira, usas el Refresh Token para obtener
 * uno nuevo SIN volver a hacer login.
 */
public class RefreshToken {
   private Long id;
   private String token;                              // Token único (UUID)
   private User user;                                 // Usuario dueño del token
   private LocalDateTime expiresAt;                   // Fecha de expiración
   private boolean revoked;                           // ¿Fue revocado? (al hacer logout)
   private LocalDateTime createAt;

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Genera token automáticamente
    */
   public RefreshToken() {
      this.token = UUID.randomUUID().toString();
      this.revoked = false;
      this.createAt = LocalDateTime.now();
   }

   /**
    * Constructor con usuario y validez en horas
    * @param user usuario dueño del token
    * @param validityHours horas de validez (30 días = 720 horas)
    */
   public RefreshToken(User user, int validityHours) {
      this();
      this.user = user;
      this.expiresAt = LocalDateTime.now().plusHours(validityHours);
   }

   // ========================= MÉTODOS DE NEGOCIO =========================

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
    * @return true si el token es de usuario
    */
   public boolean belongsTo(User user) {
      return this.user != null && this.user.equals(user);
   }

   /**
    * Extiende la validez del token (si está válido)
    * @param additionalHours horas adicionales de validez
    */
   public void extendValidity(int additionalHours) {
      if (!revoked && !isExpired()) {
         this.expiresAt = this.expiresAt.plusHours(additionalHours);
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

   public User getUser() {
      return user;
   }

   public void setUser(User user) {
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
      return createAt;
   }

   public void setCreatedAt(LocalDateTime createAt) {
      this.createAt = createAt;
   }

   // ========================= GETTERS Y SETTERS =========================


   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      RefreshToken that = (RefreshToken) o;
      return revoked == that.revoked && Objects.equals(id, that.id) && Objects.equals(token, that.token) && Objects.equals(user, that.user) && Objects.equals(expiresAt, that.expiresAt) && Objects.equals(createAt, that.createAt);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, token, user, expiresAt, revoked, createAt);
   }

   @Override
   public String toString() {
      return "RefreshToken{" +
            "createAt=" + createAt +
            ", id=" + id +
            ", token='***'" +                 // No mostrar token completo por seguridad
            ", user=" + (user != null ? user.getId() : null) +
            ", expiresAt=" + expiresAt +
            ", revoked=" + revoked +
            ", daysLeft=" + getDaysUntilExpiration() +
            '}';
   }
}