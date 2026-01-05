package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Entidad de dominio que representa un USUARIO interno del sistema Smart Parking.
 *
 * Los usuarios internos son personal de TI, administrativo y operativo de la municipalidad:
 * ADMIN, AUTORIDAD, OPERADOR. Los ciudadanos (usuarios móviles) se gestionan en
 * un servicio separado con autenticación Google.
 *
 * @author Edwin - Yoner Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class User {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String firstName;
   private String lastName;
   private String email;                                  // Único en el sistema
   private String password;                               // Hasheado con BCrypt
   private String phoneNumber;
   private String profilePicture;                         // URL de foto de perfil (opcional)

   // ========================= CAMPOS DE ESTADO =========================

   private boolean status;                                // true = activo, false = inactivo
   private boolean emailVerified;                         // ¿Email verificado?

   // ========================= RELACIONES =========================

   private Set<Role> roles;                               // Roles asignados (M:N)

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;                       // Cuándo se creó
   private Long createdBy;                                // ID del usuario que creó
   private LocalDateTime updatedAt;                       // Última actualización
   private Long updatedBy;                                // ID del usuario que actualizó
   private LocalDateTime deletedAt;                       // Cuándo se eliminó (soft delete)
   private Long deletedBy;                                // ID del usuario que eliminó

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto seguros.
    * Usuario empieza inactivo hasta verificar email.
    */
   public User() {
      this.roles = new HashSet<>();
      this.emailVerified = false;
      this.status = false;                               // Inactivo hasta verificar email
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param firstName nombre del usuario
    * @param lastName apellido del usuario
    * @param email email único (será validado)
    * @param password contraseña (debe venir YA hasheada con BCrypt)
    */
   public User(String firstName, String lastName, String email, String password) {
      this();
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.password = password;
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el nombre completo del usuario.
    *
    * @return nombre completo (firstName + lastName)
    */
   public String getFullName() {
      return firstName + " " + lastName;
   }

   /**
    * Obtiene las iniciales del usuario para avatares.
    *
    * @return iniciales (ej: "ED" para Edwin Yoner)
    */
   public String getInitials() {
      String first = firstName != null && !firstName.isEmpty()
            ? firstName.substring(0, 1).toUpperCase()
            : "";
      String last = lastName != null && !lastName.isEmpty()
            ? lastName.substring(0, 1).toUpperCase()
            : "";
      return first + last;
   }

   // ========================= MÉTODOS DE NEGOCIO - ROLES Y PERMISOS =========================

   /**
    * Asigna un rol al usuario si el rol está activo.
    *
    * @param role rol a asignar
    */
   public void assignRole(Role role) {
      if (role != null && role.isActive()) {
         this.roles.add(role);
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Remueve un rol del usuario.
    *
    * @param role rol a remover
    */
   public void removeRole(Role role) {
      if (role != null) {
         this.roles.remove(role);
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Verifica si el usuario tiene un rol específico por nombre.
    *
    * @param roleName nombre del rol (ejemplo: "ADMIN")
    * @return true si tiene el rol
    */
   public boolean hasRole(String roleName) {
      return roles.stream().anyMatch(r -> r.getName().equals(roleName));
   }

   /**
    * Verifica si el usuario tiene un permiso específico a través de sus roles.
    *
    * @param permissionName código del permiso (ejemplo: "users.create")
    * @return true si tiene el permiso
    */
   public boolean hasPermission(String permissionName) {
      return roles.stream().anyMatch(role -> role.hasPermission(permissionName));
   }

   // ========================= MÉTODOS DE NEGOCIO - VERIFICACIÓN Y ACTIVACIÓN =========================

   /**
    * Verifica el email del usuario.
    * Automáticamente activa la cuenta al verificar.
    */
   public void verifyEmail() {
      this.emailVerified = true;
      this.status = true;                                // Activar al verificar email
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Activa la cuenta del usuario manualmente (solo ADMIN).
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva la cuenta del usuario.
    * Usuario no podrá hacer login hasta ser reactivado.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el usuario está activo.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si el usuario está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el usuario como eliminado (soft delete).
    * También desactiva la cuenta automáticamente.
    *
    * @param deletedByUserId ID del usuario administrador que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = false;                               // Desactivar al eliminar
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un usuario previamente eliminado.
    * Reactiva la cuenta pero mantiene emailVerified en su estado actual.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = true;                            // Reactivar al restaurar
      this.updatedAt = LocalDateTime.now();

   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================


   /**
    * Cambia la contraseña del usuario.
    *
    * @param newPassword nueva contraseña (debe venir YA hasheada con BCrypt)
    */
   public void changePassword(String newPassword) {
      this.password = newPassword;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza el perfil del usuario.
    *
    * @param firstName nuevo nombre
    * @param lastName nuevo apellido
    * @param phoneNumber nuevo teléfono
    */
   public void updateProfile(String firstName, String lastName, String phoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.phoneNumber = phoneNumber;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza la foto de perfil.
    *
    * @param pictureUrl URL de la nueva foto
    */
   public void updateProfilePicture(String pictureUrl) {
      this.profilePicture = pictureUrl;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Elimina la foto del perfil.
    */
   public void removeProfilePicture() {
      this.profilePicture = null;
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

   // ========================= MÉTODOS DE VALIDACIÓN DE ESTADO =========================

   /**
    * Verifica si tiene foto de perfil configurada.
    *
    * @return true si tiene foto
    */
   public boolean hasProfilePicture() {
      return profilePicture != null && !profilePicture.isEmpty();
   }

   /**
    * Verifica si la cuenta está completamente activa.
    * Debe estar: activa, email verificado, no eliminada, no bloqueada.
    *
    * @return true si puede operar normalmente
    */
   public boolean isFullyActive() {
      return status
            && emailVerified;
   }

   /**
    * Verifica si el email está verificado.
    *
    * @return true si verificó su email
    */
   public boolean isEmailVerified() {
      return emailVerified;
   }

   /**
    * Verifica si la cuenta está pendiente de activación.
    * Usuario no activo y email no verificado = pendiente.
    *
    * @return true si está pendiente
    */
   public boolean isPending() {
      return !status && !emailVerified;
   }

   // ========================= GETTERS Y SETTERS =========================
   // Nota: Algunos setters se eliminaron para forzar el uso
   // de métodos de negocio (ej: no hay setStatus, usar activate()/deactivate())

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String firstName) {
      this.firstName = firstName;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String lastName) {
      this.lastName = lastName;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public String getPassword() {
      return password;
   }

   public void setPassword(String password) {
      this.password = password;
   }

   public String getPhoneNumber() {
      return phoneNumber;
   }

   public void setPhoneNumber(String phoneNumber) {
      this.phoneNumber = phoneNumber;
   }

   public String getProfilePicture() {
      return profilePicture;
   }

   public void setProfilePicture(String profilePicture) {
      this.profilePicture = profilePicture;
   }

   public boolean getStatus() {
      return status;
   }

   // NO HAY setStatus() público - usar activate() o deactivate()

   // NO HAY setEmailVerified() público - usar verifyEmail()

   // NO HAY setDeleted() público - usar markAsDeleted() o restore()

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

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
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
    * Dos usuarios son iguales si tienen el mismo ID y email.
    * Solo se usan campos inmutables/únicos para equals.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return Objects.equals(id, user.id) && Objects.equals(email, user.email);
   }

   /**
    * HashCode basado en ID y email únicamente.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, email);
   }

   /**
    * ToString simplificado para logging y debugging.
    * NO incluye password por seguridad.
    */
   @Override
   public String toString() {
      return "User{" +
            "id=" + id +
            ", email='" + email + '\'' +
            ", fullName='" + getFullName() + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            ", emailVerified=" + emailVerified +
            ", rolesCount=" + roles.size() +
            '}';
   }
}