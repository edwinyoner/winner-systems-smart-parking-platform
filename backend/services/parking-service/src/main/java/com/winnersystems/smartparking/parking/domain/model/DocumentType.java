package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un TIPO DE DOCUMENTO DE IDENTIFICACIÓN.
 *
 * Define los tipos de documentos válidos para identificar conductores y clientes.
 * Incluye documentos nacionales (DNI, RUC) y extranjeros (CE, Pasaporte, etc.).
 *
 * Cada tipo puede tener reglas de validación específicas como formato, longitud
 * y expresiones regulares para verificar la validez del número de documento.
 *
 * Ejemplos:
 * - DNI: Documento Nacional de Identidad (8 dígitos, peruanos)
 * - CE: Carné de Extranjería (9 dígitos, extranjeros residentes)
 * - PASSPORT: Pasaporte (formato variable, turistas)
 * - RUC: Registro Único de Contribuyentes (11 dígitos, empresas)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class DocumentType {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String code;                                   // Código único (ej: "DNI", "CE")
   private String name;                                   // Nombre completo
   private String description;                            // Descripción detallada (opcional)

   // ========================= CAMPOS DE ESTADO =========================

   private boolean status;                                // true = activo, false = inactivo

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;
   private Long createdBy;
   private LocalDateTime updatedAt;
   private Long updatedBy;
   private LocalDateTime deletedAt;                       // Soft delete
   private Long deletedBy;

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    * Tipo empieza INACTIVO hasta ser configurado completamente.
    */
   public DocumentType() {
      this.status = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param code código único del tipo (ej: "DNI")
    * @param name nombre completo del tipo
    */
   public DocumentType(String code, String name) {
      this();
      this.code = code;
      this.name = name;
   }


   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el identificador completo del tipo.
    *
    * @return identificador formateado (ej: "[DNI] Documento Nacional de Identidad")
    */
   public String getFullName() {
      return "[" + code + "] " + name;
   }



   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa el tipo de documento.
    * Permite que sea usado en el sistema.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva el tipo de documento.
    * No se podrán crear nuevos clientes con este tipo.
    * Los clientes existentes NO se ven afectados.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el tipo está activo.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si el tipo está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el tipo como eliminado (soft delete).
    * También lo desactiva automáticamente.
    * Los clientes existentes con este tipo NO se eliminan.
    *
    * @param deletedByUserId ID del usuario administrador que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un tipo previamente eliminado.
    * Queda inactivo, debe ser activado manualmente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el tipo está eliminado.
    *
    * @return true si deletedAt no es null
    */
   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================

   /**
    * Actualiza la información básica del tipo.
    *
    * @param name nuevo nombre
    * @param description nueva descripción
    */
   public void updateBasicInfo(String name, String description) {
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

   // ========================= MÉTODOS DE VALIDACIÓN =========================

   /**
    * Verifica si el tipo puede ser usado para crear clientes.
    * Debe estar activo y no eliminado.
    *
    * @return true si puede ser usado
    */
   public boolean isUsable() {
      return isActive() && !isDeleted();
   }

   // ========================= GETTERS Y SETTERS =========================

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
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
    * Dos tipos son iguales si tienen el mismo ID o el mismo código.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      DocumentType that = (DocumentType) o;
      return Objects.equals(id, that.id) || Objects.equals(code, that.code);
   }

   /**
    * HashCode basado en ID y código únicamente.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, code);
   }

   /**
    * ToString para logging y debugging.
    */
   @Override
   public String toString() {
      return "DocumentType{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            '}';
   }
}