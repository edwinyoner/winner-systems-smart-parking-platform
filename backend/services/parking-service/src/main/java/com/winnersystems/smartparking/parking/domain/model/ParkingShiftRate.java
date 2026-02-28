package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa la CONFIGURACIÓN de Turno y Tarifa para un Parqueo.
 *
 * Esta tabla intermedia implementa una relación ternaria entre Parking, Shift y Rate,
 * permitiendo configurar qué tarifa se aplica en cada turno para todo el parqueo.
 * La configuración se aplica uniformemente a TODAS las zonas del parqueo.
 *
 * Características:
 * - Un parqueo puede tener múltiples configuraciones (una por turno × tarifa)
 * - Cada configuración puede activarse/desactivarse mediante el campo status
 * - Permite cambiar tarifas sin afectar el histórico en Transaction
 * - Todas las zonas del parqueo heredan la configuración del parqueo
 *
 * Ejemplo de uso:
 * Parqueo "Simón Bolívar":
 * - Turno Mañana + Tarifa Auto (S/. 2.00) → status: true
 * - Turno Mañana + Tarifa Moto (S/. 1.00) → status: true
 * - Turno Tarde + Tarifa Auto (S/. 1.50) → status: true
 * - Turno Noche → Sin configurar (no opera en ese horario)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class ParkingShiftRate {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private Long parkingId;                                // FK a Parking (NOT NULL)
   private Long shiftId;                                  // FK a Shift (NOT NULL)
   private Long rateId;                                   // FK a Rate (NOT NULL)

   // ========================= CAMPOS DE ESTADO =========================

   private boolean status;                                // true = activa, false = inactiva

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;
   private Long createdBy;                                // ID del admin que creó
   private LocalDateTime updatedAt;
   private Long updatedBy;                                // ID del admin que actualizó

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    * Configuración empieza ACTIVA.
    */
   public ParkingShiftRate() {
      this.status = true;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param parkingId ID del parqueo
    * @param shiftId ID del turno
    * @param rateId ID de la tarifa
    */
   public ParkingShiftRate(Long parkingId, Long shiftId, Long rateId) {
      this();
      this.parkingId = parkingId;
      this.shiftId = shiftId;
      this.rateId = rateId;
   }

   /**
    * Constructor completo con estado.
    *
    * @param parkingId ID del parqueo
    * @param shiftId ID del turno
    * @param rateId ID de la tarifa
    * @param status si la configuración está activa
    */
   public ParkingShiftRate(Long parkingId, Long shiftId, Long rateId, boolean status) {
      this(parkingId, shiftId, rateId);
      this.status = status;
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa la configuración.
    * El parqueo operará en este turno con esta tarifa en todas sus zonas.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva la configuración.
    * El parqueo NO operará en este turno (como si cerrara en ese horario).
    * Los datos NO se eliminan, solo se desactivan.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Alterna el estado activo/inactivo.
    * Útil para el toggle del dashboard.
    */
   public void toggleStatus() {
      this.status = !this.status;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si la configuración está activa.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si la configuración está inactiva.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN =========================

   /**
    * Cambia la tarifa de esta configuración.
    * Mantiene el parqueo y el turno, solo actualiza la tarifa.
    *
    * @param newRateId ID de la nueva tarifa
    */
   public void updateRate(Long newRateId) {
      this.rateId = newRateId;
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
    * Verifica si la configuración es válida.
    * Debe tener parqueo, turno y tarifa asignados.
    *
    * @return true si tiene todos los IDs necesarios
    */
   public boolean isValid() {
      return parkingId != null && shiftId != null && rateId != null;
   }

   /**
    * Verifica si la configuración puede ser usada operativamente.
    * Debe estar activa y ser válida.
    *
    * @return true si puede ser usada
    */
   public boolean isUsable() {
      return isActive() && isValid();
   }

   // ========================= MÉTODOS DE INFORMACIÓN =========================

   /**
    * Obtiene una descripción legible de la configuración.
    *
    * @return descripción formateada
    */
   public String getDescription() {
      return "Parking:" + parkingId + " Shift:" + shiftId + " Rate:" + rateId +
            " [" + (status ? "ACTIVA" : "INACTIVA") + "]";
   }

   // ========================= GETTERS Y SETTERS =========================

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getParkingId() {
      return parkingId;
   }

   public void setParkingId(Long parkingId) {
      this.parkingId = parkingId;
   }

   public Long getShiftId() {
      return shiftId;
   }

   public void setShiftId(Long shiftId) {
      this.shiftId = shiftId;
   }

   public Long getRateId() {
      return rateId;
   }

   public void setRateId(Long rateId) {
      this.rateId = rateId;
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

   // ========================= EQUALS, HASHCODE Y TOSTRING =========================

   /**
    * Dos configuraciones son iguales si tienen el mismo ID o misma combinación parking+turno+rate.
    * Puede haber múltiples configuraciones para el mismo parking+turno (diferentes tarifas).
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      ParkingShiftRate that = (ParkingShiftRate) o;
      return Objects.equals(id, that.id) ||
            (Objects.equals(parkingId, that.parkingId) &&
                  Objects.equals(shiftId, that.shiftId) &&
                  Objects.equals(rateId, that.rateId));
   }

   /**
    * HashCode basado en ID, parkingId, shiftId y rateId.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, parkingId, shiftId, rateId);
   }

   /**
    * ToString para logging y debugging.
    */
   @Override
   public String toString() {
      return "ParkingShiftRate{" +
            "id=" + id +
            ", parkingId=" + parkingId +
            ", shiftId=" + shiftId +
            ", rateId=" + rateId +
            ", status=" + (status ? "ACTIVA" : "INACTIVA") +
            ", isUsable=" + isUsable() +
            '}';
   }
}