package com.winnersystems.smartparking.parking.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa una TARIFA de estacionamiento.
 *
 * Una tarifa define el precio por hora que se cobra por estacionar en un turno específico.
 * Las tarifas están asociadas a un turno (mañana, tarde o noche) y pueden ser reutilizadas
 * por múltiples zonas de estacionamiento.
 *
 * Ejemplos:
 * - "Mañana Económica" - S/. 2.00/hora para turno Mañana
 * - "Tarde Premium" - S/. 5.00/hora para turno Tarde
 * - "Noche Estándar" - S/. 3.00/hora para turno Noche
 *
 * El sistema mantiene histórico de tarifas mediante los campos validFrom y validTo,
 * permitiendo ver qué tarifa se aplicó en cada momento histórico sin perder información.
 *
 * Las zonas seleccionan qué tarifa usar para cada turno mediante la configuración
 * en ZoneShiftRate.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Rate {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                                   // Nombre de la tarifa
   private String description;                            // Descripción adicional (opcional)

   // ========================= CAMPOS DE PRECIO =========================

   private BigDecimal amount;                             // Monto por hora
   private String currency;                               // Moneda (default: "PEN")

   // ========================= CAMPOS DE ESTADO =========================

   private boolean status;                                // true = activa, false = inactiva

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
    * Tarifa empieza INACTIVA hasta ser configurada completamente.
    */
   public Rate() {
      this.status = false;
      this.currency = "PEN";
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param name nombre de la tarifa
    * @param amount monto por hora
    */
   public Rate(String name, BigDecimal amount) {
      this();
      this.name = name;
      this.amount = amount;
   }

   /**
    * Constructor completo.
    *
    * @param name nombre de la tarifa
    * @param amount monto por hora
    * @param description descripción
    */
   public Rate(String name, BigDecimal amount, String description) {
      this(name, amount);
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene un identificador legible de la tarifa.
    *
    * @return descripción formateada (ej: "Mañana Económica - S/. 2.00/hora")
    */
   public String getDisplayName() {
      return name + " - " + currency + " " + amount + "/hora";
   }

   /**
    * Calcula el costo para una duración dada en minutos.
    *
    * @param durationMinutes duración en minutos
    * @return costo total calculado
    */
   public BigDecimal calculateCost(int durationMinutes) {
      if (amount == null || durationMinutes <= 0) {
         return BigDecimal.ZERO;
      }

      double hours = durationMinutes / 60.0;
      return amount.multiply(BigDecimal.valueOf(hours))
            .setScale(2, BigDecimal.ROUND_HALF_UP);
   }

   /**
    * Calcula el costo por hora fraccionada (redondeo hacia arriba).
    * Si usa 1 minuto, cobra la hora completa.
    *
    * @param durationMinutes duración en minutos
    * @return costo total calculado
    */
   public BigDecimal calculateCostRoundedUp(int durationMinutes) {
      if (amount == null || durationMinutes <= 0) {
         return BigDecimal.ZERO;
      }

      int hours = (int) Math.ceil(durationMinutes / 60.0);
      return amount.multiply(BigDecimal.valueOf(hours));
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa la tarifa.
    * Permite que sea usada en la configuración de zonas.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva la tarifa.
    * No se podrá configurar en nuevas zonas.
    * Las configuraciones existentes NO se ven afectadas.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si la tarifa está activa.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si la tarifa está inactiva.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca la tarifa como eliminada (soft delete).
    * También la desactiva automáticamente.
    * Las configuraciones existentes en ZoneShiftRate NO se eliminan.
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
    * Restaura una tarifa previamente eliminada.
    * Queda inactiva, debe ser activada manualmente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si la tarifa está eliminada.
    *
    * @return true si deletedAt no es null
    */
   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================

   /**
    * Actualiza la información básica de la tarifa.
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
    * Actualiza el monto de la tarifa.
    *
    * @param amount nuevo monto
    */
   public void updateAmount(BigDecimal amount) {
      this.amount = amount;
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
    * Verifica si la tarifa tiene precio configurado.
    *
    * @return true si amount no es null y es mayor a cero
    */
   public boolean hasValidAmount() {
      return amount != null && amount.compareTo(BigDecimal.ZERO) > 0;
   }

   /**
    * Verifica si la tarifa puede ser usada.
    * Debe estar activa, no eliminada y con monto válido.
    *
    * @return true si puede ser usada
    */
   public boolean isUsable() {
      return isActive() && !isDeleted() && hasValidAmount();
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

   public BigDecimal getAmount() {
      return amount;
   }

   public void setAmount(BigDecimal amount) {
      this.amount = amount;
   }

   public String getCurrency() {
      return currency;
   }

   public void setCurrency(String currency) {
      this.currency = currency;
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
    * Dos tarifas son iguales si tienen el mismo ID.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Rate rate = (Rate) o;
      return Objects.equals(id, rate.id);
   }

   /**
    * HashCode basado en ID únicamente.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id);
   }

   /**
    * ToString para logging y debugging.
    */
   @Override
   public String toString() {
      return "Rate{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", amount=" + amount +
            ", currency='" + currency + '\'' +
            ", status=" + (status ? "ACTIVA" : "INACTIVA") +
            '}';
   }
}