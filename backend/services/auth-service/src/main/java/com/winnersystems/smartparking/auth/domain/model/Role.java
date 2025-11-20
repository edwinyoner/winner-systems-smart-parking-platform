package com.winnersystems.smartparking.auth.domain.model;

import com.winnersystems.smartparking.auth.domain.enums.RoleType;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

/***
 * Entidad de dominio que representa un ROL en el sistema.
 * Un rol agrupa permisos y define lo que un usuario puede hacer
 */
public class Role {
   private Long id;
   private RoleType roleType;               // ADMIN, AUTORIDAD, OPERADOR, USER
   private  String description;
   private boolean status;                   // true = activo, false = inactivo
   private Set<Permission> permissions;      // Permisos asociados al rol
   private LocalDateTime createAt;
   private LocalDateTime updateAt;

   // ========================= CONSTRUCTORES =========================

   /***
    * Constructor vacío - Inicializa con valores por defecto
    */
   public Role() {
      this.permissions = new HashSet<>();
      this.status = true;                    // Por defecto activo
      this.createAt = LocalDateTime.now();
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Constructor con tipo de rol
    * @param roleType
    */
   public Role(RoleType roleType) {
      this();
      this.roleType = roleType;
      this.description = roleType.getDescription();
   }

   /***
    * Constructor completo
    * @param roleType
    * @param description
    */
   public Role(RoleType roleType, String description) {
      this();
      this.roleType = roleType;
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO =========================

   /***
    * Agrega un permiso al rol
    * solo agrega si el permiso no es nulo
    * @param permission
    */
   public void addPermission(Permission permission) {
      if (permission != null) {
         this.permissions.add(permission);
         this.updateAt = LocalDateTime.now();
      }
   }

   /***
    * Remueve un permiso del rol
    * @param permission
    */
   public void removePermission(Permission permission) {
      if (permission != null) {
         this.permissions.remove(permission);
         this.updateAt = LocalDateTime.now();
      }
   }

   /***
    * Verifica si el rol tiene un permiso específico por nombre
    * @param permissionName nombre del permiso (ejemplo: "users.create")
    * @return true si el rol tiene ese permiso
    */
   public boolean hasPermission(String permissionName) {
      return permissions.stream().anyMatch(p -> p.getName().equals(permissionName));
   }

   /***
    * Verifica si el rol tiene permisos de un módulo específico
    * @param moduleName nombre del módulo (ejemplo: "users")
    * @return true si tiene al menos un permiso del módulo
    */
   public boolean hasModuleAccess(String moduleName) {
      return permissions.stream().anyMatch(p-> p.belongsToModule(moduleName));
   }

   /***
    * Activa el rol (cambia status a true)
    */
   public void activate() {
      this.status = true;
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Desactiva el rol (cambia status a false)
    */
   public void desactivate() {
      this.status = false;
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Verifica si el rol está activo
    * @return true o false
    */
   public boolean isActive() {
      return this.status;
   }

   /***
    * Verifica si es un rol administrativo
    * @return true si es ADMIN o AUTORIDAD
    */
   public boolean isAdministrative() {
      return roleType != null && roleType.isAdministrative();
   }

   /***
    * Verifica si es un rol operativo
    * @return true si es OPERADOR o ADMIN
    */
   public boolean isOperational() {
      return roleType != null && roleType.isOperational();
   }

   /***
    * Obtiene el conteo de permisos
    */
   public int getPermissionsCount() {
      return permissions.size();
   }

   // ========================= GETTERS Y SETTERS =========================

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public RoleType getRoleType() {
      return roleType;
   }

   public void setRoleType(RoleType roleType) {
      this.roleType = roleType;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public boolean isStatus() {
      return status;
   }

   public void setStatus(boolean status) {
      this.status = status;
   }

   public Set<Permission> getPermissions() {
      return permissions;
   }

   public void setPermissions(Set<Permission> permissions) {
      this.permissions = permissions;
   }

   public LocalDateTime getCreateAt() {
      return createAt;
   }

   public void setCreateAt(LocalDateTime createAt) {
      this.createAt = createAt;
   }

   public LocalDateTime getUpdateAt() {
      return updateAt;
   }

   public void setUpdateAt(LocalDateTime updateAt) {
      this.updateAt = updateAt;
   }

   // ========================= EQUALS, HASHCODE Y TOSTRING =========================

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Role role = (Role) o;
      return status == role.status && Objects.equals(id, role.id) && roleType == role.roleType && Objects.equals(description, role.description) && Objects.equals(permissions, role.permissions) && Objects.equals(createAt, role.createAt) && Objects.equals(updateAt, role.updateAt);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, roleType, description, status, permissions, createAt, updateAt);
   }

   @Override
   public String toString() {
      return "Role{" +
            "createAt=" + createAt +
            ", id=" + id +
            ", roleType=" + roleType +
            ", description='" + description + '\'' +
            ", status=" + status +
            ", permissions=" + permissions +
            ", updateAt=" + updateAt +
            '}';
   }
}