package com.winnersystems.smartparking.auth.domain.model;

import com.winnersystems.smartparking.auth.domain.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/***
 * Entidad de dominio que representa un USUARIO del sistema Smart Parking.
 * Contiene toda la información del usuario y sus relaciones con roles.
 */
public class User {
   private Long id;
   private String firstName;
   private String lastName;
   private String email;
   private String password;                              // Encriptado con (BCrypt)
   private String phoneNumber;
   private String profilePicture;                        // URL de la foto de perfil
   private UserStatus status;                            // ACTIVE, INACTIVE, PENDING, SUSPENDED
   private boolean emailVerified;                        // ¿Email verificado?
   private boolean deleted;                              // Soft delete
   private Set<Role> roles;                              // Roles asignado al usuario
   private LocalDateTime lastLoginAt;                    // Último login
   private LocalDateTime createAt;                       // Cuándo se creó
   private LocalDateTime updateAt;                       // Cuándo se actualizó
   private LocalDateTime deletedAt;                      // Cuándo se eliminó

   // ========================= CONSTRUCTORES =========================

   /***
    * Constructor vacío - Inicializa con valores por defecto
    */
   public User() {
      this.roles = new HashSet<>();
      this.emailVerified = false;
      this.status = UserStatus.PENDING;   // Por defecto pendiente hasta verificar email
      this.deleted = false;
      this.createAt = LocalDateTime.now();
      this.updateAt = LocalDateTime.now();
   }

   public User(String firstName, String lastName, String email, String password) {
      this();
      this.firstName = firstName;
      this.lastName = lastName;
      this.email = email;
      this.password = password;
   }

   // ========================= MÉTODOS DE NEGOCIO =========================

   /***
    * Obtiene el nombre completo del usuario
    * @return nombre completo (firstName + lastName)
    */
   public String getFullName() {
      return firstName + " " + lastName;
   }

   /***
    * Asigna un rol al usuario
    * @param role
    */
   public void assignRole(Role role) {
      if (role != null && role.isActive()) {
         this.roles.add(role);
         this.updateAt = LocalDateTime.now();
      }
   }

   /***
    * Remueve un rol del usuario
    * @param role
    */
   public void removeRole(Role role) {
      if (role != null) {
         this.roles.remove(role);
         this.updateAt = LocalDateTime.now();
      }
   }

   /***
    * Verifica si el usuario tiene un rol específico por nombre
    * @param roleName nombre del rol (ejemplo: "ADMIN")
    * @return true si tiene el rol
    */
   public boolean hasRole(String roleName) {
      return roles.stream().anyMatch(r -> r.getRoleType().name().equals(roleName));
   }

   /***
    * Verifica si el usuario tiene un permiso específico
    * @param permissionName nombre del permiso (ejemplo: "users.create")
    * @return true si tiene el permiso
    */
   public boolean hasPermission(String permissionName) {
      return roles.stream().anyMatch(role -> role.hasPermission(permissionName));
   }

   /***
    * Verifica si el usuario es administrador
    * @return true si tiene rol ADMIN
    */
   public boolean isAdmin() {
      return roles.stream().anyMatch(Role::isAdministrative);
   }

   /***
    * Verifica el email (cuando hace click en el link de verificación)
    * Cambia el status a ACTIVE automáticamente
    */
   public void verifyEmail() {
      this.emailVerified = true;
      this.status = UserStatus.ACTIVE;     // Al verificar, se activa
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Activa la cuenta del usuario
    */
   public void activate() {
      this.status = UserStatus.ACTIVE;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Desactiva la cuenta del usuario
    */
   public void desactivate() {
      this.status = UserStatus.INACTIVE;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Suspende la cuenta del usuario (por violación de políticas)
    */
   public void suspend() {
      this.status = UserStatus.SUSPENDED;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Marca el usuario como eliminado (soft delete)
    * También desactiva la cuenta
    */
   public void markAsDeleted() {
      this.deleted = true;
      this.deletedAt = LocalDateTime.now();
      this.status = UserStatus.INACTIVE;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Restaura un usuario eliminado
    */
   public void restore() {
      if (this.deleted) {
         this.deleted = false;
         this.deletedAt = null;
         this.status = UserStatus.ACTIVE;
         this.updateAt = LocalDateTime.now();
      }
   }

   /**
    * Actualiza la fecha del último login
    */
   public void updateLastLogin() {
      this.lastLoginAt = LocalDateTime.now();
   }

   /**
    * Cambia la contraseña del usuario
    * @param newPassword nueva contraseña (ya debe venir encriptada)
    */
   public void changePassword(String newPassword) {
      this.password = newPassword;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Actualiza el perfil del usuario
    * @param firstName
    * @param lastName
    * @param phoneNumber
    */
   public void updateProfile(String firstName, String lastName, String phoneNumber) {
      this.firstName = firstName;
      this.lastName = lastName;
      this.phoneNumber = phoneNumber;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Actualiza la foto de perfil
    * @param pictureUrl URL de la nueva foto
    */
   public void updateProfilePicture(String pictureUrl) {
      this.profilePicture = pictureUrl;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Elimina la foto del perfil
    */
   public void removeProfilePicture() {
      this.profilePicture = null;
      this.updateAt = LocalDateTime.now();
   }

   /**
    * Verifica si tiene foto de perfil
    * @return
    */
   public boolean hasProfilePicture() {
      return profilePicture != null && !profilePicture.isEmpty();
   }

   /**
    * Verifica si la cuenta está completamente activa
    * (activo, email verificado y NO eliminado)
    * @return true si puede operar normalmente
    */
   public boolean isFullyActive() {
      return status == UserStatus.ACTIVE && emailVerified && !deleted;
   }

   /**
    * Verifica si el usuario puede hacer login
    * @return true si puede hacer login
    */
   public boolean canLogin() {
      return status.canOperate() && emailVerified && !deleted;
   }

   /**
    * Verifica si el usaurio fue eliminado
    * @return
    */
   public boolean isDeleted() {
      return deleted;
   }

   // ========================= GETTERS Y SETTERS =========================


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

   public UserStatus getStatus() {
      return status;
   }

   public void setStatus(UserStatus status) {
      this.status = status;
   }

   public boolean isEmailVerified() {
      return emailVerified;
   }

   public void setEmailVerified(boolean emailVerified) {
      this.emailVerified = emailVerified;
   }

   public void setDeleted(boolean deleted) {
      this.deleted = deleted;
   }

   public LocalDateTime getDeletedAt() {
      return deletedAt;
   }

   public void setDeletedAt(LocalDateTime deletedAt) {
      this.deletedAt = deletedAt;
   }

   public Set<Role> getRoles() {
      return roles;
   }

   public void setRoles(Set<Role> roles) {
      this.roles = roles;
   }

   public LocalDateTime getLastLoginAt() {
      return lastLoginAt;
   }

   public void setLastLoginAt(LocalDateTime lastLoginAt) {
      this.lastLoginAt = lastLoginAt;
   }

   public LocalDateTime getCreatedAt() {
      return createAt;
   }

   public void setCreatedAt(LocalDateTime createAt) {
      this.createAt = createAt;
   }

   public LocalDateTime getUpdatedAt() {
      return updateAt;
   }

   public void setUpdatedAt(LocalDateTime updateAt) {
      this.updateAt = updateAt;
   }

   // ========================= EQUALS, HASHCODE Y TOSTRING =========================

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      User user = (User) o;
      return emailVerified == user.emailVerified && deleted == user.deleted && Objects.equals(id, user.id) && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName) && Objects.equals(email, user.email) && Objects.equals(password, user.password) && Objects.equals(phoneNumber, user.phoneNumber) && Objects.equals(profilePicture, user.profilePicture) && status == user.status && Objects.equals(roles, user.roles) && Objects.equals(lastLoginAt, user.lastLoginAt) && Objects.equals(createAt, user.createAt) && Objects.equals(updateAt, user.updateAt) && Objects.equals(deletedAt, user.deletedAt);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, firstName, lastName, email, password, phoneNumber, profilePicture, status, emailVerified, deleted, roles, lastLoginAt, createAt, updateAt, deletedAt);
   }

   @Override
   public String toString() {
      return "User{" +
            "createAt=" + createAt +
            ", id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", status=" + status +
            ", emailVerified=" + emailVerified +
            ", deleted=" + deleted +
            ", rolesCount=" + roles.size() +
            '}';
   }
}