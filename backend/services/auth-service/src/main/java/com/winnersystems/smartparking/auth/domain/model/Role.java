package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad de dominio que representa un ROL en el sistema Smart Parking.
 *
 * <p>Los roles agrupan permisos y definen las capacidades de los usuarios.
 * El sistema maneja 3 roles internos: ADMIN, AUTORIDAD, OPERADOR.</p>
 *
 * <p><b>Características:</b></p>
 * <ul>
 *   <li>Relación Many-to-Many con Permission</li>
 *   <li>Soft delete para auditoría</li>
 *   <li>Roles de sistema (ADMIN) no pueden desactivarse</li>
 * </ul>
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Role {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                                  // Nombre legible del rol
   private String description;                           // Descripción del rol

   // ========================= CAMPO DE ESTADO =========================

   private boolean status;                               // true = activo, false = inactivo

   // ========================= RELACIONES =========================

   private Set<Permission> permissions;                  // Permisos asociados al rol (M:N)

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                      // Cuándo se creó
   private Long createdBy;                               // ID del usuario que creó
   private LocalDateTime updatedAt;                      // Última actualización
   private Long updatedBy;                               // ID del usuario que actualizó
   private LocalDateTime deletedAt;                      // Cuándo se eliminó (soft delete)
   private Long deletedBy;                               // ID del usuario que eliminó

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    * Rol empieza activo con conjunto de permisos vacío.
    */
   public Role() {
      this.permissions = new HashSet<>();
      this.status = true;                               // Por defecto activo
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con nombre y descripción.
    *
    * @param name nombre del rol
    * @param description descripción del rol
    */
   public Role(String name, String description) {
      this();
      this.name = name;
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - PERMISOS =========================

   /**
    * Agrega un permiso al rol si el permiso está activo.
    *
    * @param permission permiso a agregar
    */
   public void addPermission(Permission permission) {
      if (permission != null && permission.isActive()) {
         this.permissions.add(permission);
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Remueve un permiso del rol.
    *
    * @param permission permiso a remover
    */
   public void removePermission(Permission permission) {
      if (permission != null) {
         this.permissions.remove(permission);
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Limpia todos los permisos del rol.
    * Útil para reconfigurar permisos desde cero.
    */
   public void clearPermissions() {
      this.permissions.clear();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el rol tiene un permiso específico por nombre.
    *
    * @param permissionName nombre del permiso (ejemplo: "USERS_CREATE")
    * @return true si el rol tiene ese permiso
    */
   public boolean hasPermission(String permissionName) {
      return permissions.stream()
            .anyMatch(p -> p.getName().equalsIgnoreCase(permissionName));
   }


   /**
    * Obtiene todos los permisos activos del rol.
    * Filtra permisos inactivos o eliminados.
    *
    * @return conjunto de permisos activos
    */
   public Set<Permission> getActivePermissions() {
      Set<Permission> activePerms = new HashSet<>();
      for (Permission p : permissions) {
         if (p.isActive()) {
            activePerms.add(p);
         }
      }
      return activePerms;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTIVACIÓN =========================

   /**
    * Activa el rol (cambia status a true).
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva el rol (cambia status a false).
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el rol está activo.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return this.status;
   }

   /**
    * Verifica si el rol está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !this.status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el rol como eliminado (soft delete).
    * También desactiva el rol automáticamente.
    *
    * @param deletedByUserId ID del usuario que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = false;                              // Desactivar al eliminar
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un rol previamente eliminado.
    * Reactiva el rol automáticamente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= MÉTODOS DE NEGOCIO - VALIDACIONES =========================

   /**
    * Verifica si el rol es de sistema (no modificable).
    *
    * @return true si es ADMIN
    */
   public boolean isSystemRole() {
      return name != null && name.equalsIgnoreCase("ADMIN");
   }

   /**
    * Verifica si el rol puede ser modificado.
    *
    * @return true si puede modificarse
    */
   public boolean canBeModified() {
      return !isSystemRole();
   }

   /**
    * Obtiene el conteo total de permisos.
    *
    * @return cantidad de permisos
    */
   public int getPermissionsCount() {
      return permissions.size();
   }

   /**
    * Obtiene el conteo de permisos activos.
    *
    * @return cantidad de permisos activos
    */
   public int getActivePermissionsCount() {
      return (int) permissions.stream()
            .filter(Permission::isActive)
            .count();
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN =========================

   /**
    * Actualiza el nombre y descripción del rol.
    * No permite modificar roles de sistema.
    *
    * @param name nuevo nombre
    * @param description nueva descripción
    */
   public void updateDetails(String name, String description) {
      if (isSystemRole()) {
         throw new IllegalStateException("Los roles de sistema no pueden modificar su nombre o descripción");
      }
      this.name = name;
      this.description = description;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza el usuario que modificó este registro.
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

   public Set<Permission> getPermissions() {
      return permissions;
   }

   public void setPermissions(Set<Permission> permissions) {
      this.permissions = permissions;
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
    * Dos roles son iguales si tienen el mismo ID y nombre.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Role role = (Role) o;
      return Objects.equals(id, role.id) &&
            Objects.equals(name, role.name);
   }

   /**
    * HashCode basado en ID y nombre.
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
      return "Role{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            ", permissionsCount=" + permissions.size() +
            ", isSystemRole=" + isSystemRole() +
            '}';
   }
}