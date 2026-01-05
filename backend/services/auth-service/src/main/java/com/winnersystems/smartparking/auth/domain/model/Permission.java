package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un PERMISO en el sistema Smart Parking.
 *
 * <p>Los permisos implementan control de acceso granular (PBAC - Permission-Based Access Control).
 * Se asignan a roles y definen acciones específicas que los usuarios pueden realizar.</p>
 *
 * <p><b>Estructura simplificada:</b></p>
 * <ul>
 *   <li>name: Identificador único del permiso (ej: "users.create", "parking.update")</li>
 *   <li>description: Descripción legible para interfaces de usuario</li>
 * </ul>
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Permission {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                                  // "users.create" (único, inmutable)
   private String description;                           // "Crear Usuarios" (legible)

   // ========================= CAMPO DE ESTADO =========================

   private boolean status;                               // true = activo, false = inactivo

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                      // Cuándo se creó
   private Long createdBy;                               // ID del usuario que creó
   private LocalDateTime updatedAt;                      // Última actualización
   private Long updatedBy;                               // ID del usuario que actualizó
   private LocalDateTime deletedAt;                      // Cuándo se eliminó
   private Long deletedBy;                               // ID del usuario que eliminó

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    */
   public Permission() {
      this.status = true;                               // Activo por defecto
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor completo para crear un nuevo permiso.
    *
    * @param name nombre único del permiso (ej: "users.create")
    * @param description descripción legible
    */
   public Permission(String name, String description) {
      this();
      this.name = name;
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN =========================

   /**
    * Actualiza la descripción del permiso.
    *
    * @param description nueva descripción
    */
   public void updateDescription(String description, String name) {
      this.name = name;
      this.description = description;
      this.updatedAt = LocalDateTime.now();
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

   // ========================= MÉTODOS DE NEGOCIO - ACTIVACIÓN =========================

   /**
    * Activa el permiso.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva el permiso.
    * Los permisos inactivos no se consideran en validaciones de roles.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el permiso está activo.
    *
    * @return true si status = true y no está eliminado
    */
   public boolean isActive() {
      return this.status;
   }

   /**
    * Verifica si el permiso está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !this.status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el permiso como eliminado (soft delete).
    * También desactiva el permiso automáticamente.
    *
    * @param deletedByUserId ID del usuario administrador que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = false;                              // Desactivar al eliminar
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un permiso previamente eliminado.
    * Reactiva el permiso automáticamente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = true;                           // Reactivar al restaurar
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= GETTERS Y SETTERS =========================

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public boolean getStatus() {
      return status;
   }

   // NO HAY setStatus() público - usar activate() o deactivate()

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

   public LocalDateTime getDeletedAt() {
      return deletedAt;
   }

   public void setDeletedAt(LocalDateTime deletedAt) {
      this.deletedAt = deletedAt;
   }

   public Long getDeletedBy() {
      return deletedBy;
   }

   public void setDeletedBy(Long deletedBy) {
      this.deletedBy = deletedBy;
   }

   // ========================= EQUALS, HASHCODE Y TOSTRING =========================

   /**
    * Dos permisos son iguales si tienen el mismo ID y name.
    * Solo se usan campos únicos/inmutables.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Permission that = (Permission) o;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name);
   }

   /**
    * HashCode basado en ID y name únicamente.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, name);
   }

   /**
    * ToString simplificado para logging y debugging.
    */
   @Override
   public String toString() {
      return "Permission{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            '}';
   }
}