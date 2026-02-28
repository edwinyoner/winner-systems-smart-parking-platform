package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un TIPO/MÉTODO DE PAGO.
 *
 * Define los métodos de pago soportados por el sistema de estacionamiento.
 * Es un catálogo maestro configurable desde el dashboard administrativo.
 *
 * Métodos típicos:
 * - CASH: Efectivo
 * - CARD: Tarjeta débito/crédito
 * - QR: Código QR (Yape, Plin, BIM)
 * - ONLINE: Pago online desde app móvil (Fase 2)
 *
 * Algunos métodos requieren número de referencia (requiresReference):
 * - CASH: false (no necesita)
 * - CARD: true (número de operación bancaria)
 * - QR: true (código de transacción)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class PaymentType {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String code;                                   // Código único (CASH, CARD, QR, ONLINE)
   private String name;                                   // Nombre descriptivo
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

   // ========================= CONSTANTES =========================

   public static final String CODE_CASH = "CASH";
   public static final String CODE_CARD = "CARD";
   public static final String CODE_QR = "QR";
   public static final String CODE_ONLINE = "ONLINE";

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    */
   public PaymentType() {
      this.status = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param code código único
    * @param name nombre del método
    */
   public PaymentType(String code, String name) {
      this();
      this.code = code;
      this.name = name;
   }

   /**
    * Constructor completo.
    *
    * @param code código único
    * @param name nombre del método
    * @param description descripción
    */
   public PaymentType(String code, String name, String description) {
      this(code, name);
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el identificador completo del tipo de pago.
    *
    * @return identificador formateado
    */
   public String getFullName() {
      return "[" + code + "] " + name;
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa el método de pago.
    * Permite que sea usado en transacciones.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva el método de pago.
    * No se podrá usar en nuevas transacciones.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el método está activo.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si el método está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el tipo de pago como eliminado (soft delete).
    *
    * @param deletedByUserId ID del usuario que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un tipo previamente eliminado.
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

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN =========================

   /**
    * Actualiza la información básica.
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
    * Verifica si el tipo puede ser usado.
    *
    * @return true si está activo y no eliminado
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
    * Dos tipos son iguales si tienen el mismo ID o código.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      PaymentType that = (PaymentType) o;
      return Objects.equals(id, that.id) || Objects.equals(code, that.code);
   }

   /**
    * HashCode basado en ID y código.
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
      return "PaymentType{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            '}';
   }
}