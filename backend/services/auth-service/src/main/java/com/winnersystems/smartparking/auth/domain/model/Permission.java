package com.winnersystems.smartparking.auth.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/***
 * Entidad de dominio que representa un PERMISO en el sistema.
 */
public class Permission {
   private Long id;
   private String name;                   // Ejemplo: "users:create"
   private String description;            // Ejemplo: "Crear usuarios"
   private String module;                 // Ejemplo: "users", "parking", "reportes"
   private LocalDateTime createAt;
   private LocalDateTime updateAt;

   // ========================= CONSTRUCTORES =========================

   /***
    * Constructor vacío - Inicializa fechas automáticamente
    */
   public Permission() {
      this.createAt = LocalDateTime.now();
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Constructor completo para crear un nuevo permiso
    * @param name
    * @param description
    * @param module
    */
   public Permission (String name, String description, String module) {
      this();
      this.name = name;
      this.description = description;
      this.module = module;
   }

   // ========================= MÉTODOS DE NEGOCIO =========================

   /***
    * Verifica si este permiso pertenece a un módulo específico
    * @param moduleName nombre del módulo a verificar
    * @return true sí pertenece al módulo
    */
   public boolean belongsToModule(String moduleName) {
      return this.module != null && this.module.equalsIgnoreCase(moduleName);
   }

   /***
    * Actualiza los datos del permiso
    * Este método actualiza la fecha de modificación automáticamente
    * @param name
    * @param description
    * @param module
    */
   public void update(String name, String description, String module) {
      this.name = name;
      this.description = description;
      this.module = module;
      this.updateAt = LocalDateTime.now();
   }

   /***
    * Verifica si el permiso es de tipo administrativo
    * @return true si el módulo es "admin" o "system"
    */
   public boolean isAdministrative() {
      return module != null && (module.equalsIgnoreCase("admin") || module.equalsIgnoreCase("system"));
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

   public void setDescription(String description){
      this.description = description;
   }

   public String getModule() {
      return module;
   }

   public void setModule(String module) {
      this.module = module;
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
      Permission that = (Permission) o;
      return Objects.equals(id, that.id) && Objects.equals(name, that.name);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, name);
   }

   @Override
   public String toString() {
      return "Permission{" +
            "id=" + id +
            ", name=" + name + '\'' +
            ", description=" + description + '\'' +
            ", module=" + module + '\'' +
            "}";
   }
}
