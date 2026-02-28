package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un TURNO horario de operación.
 *
 * Un turno define un periodo del día con un horario específico de inicio y fin.
 * Los turnos se usan para configurar tarifas diferenciadas según la hora del día.
 *
 * Ejemplos típicos:
 * - MAÑANA:  06:00 - 13:59 (horario comercial matutino)
 * - TARDE:   14:00 - 19:59 (horario pico comercial)
 * - NOCHE:   20:00 - 05:59 (horario nocturno)
 *
 * Cada zona de estacionamiento puede tener tarifas diferentes según el turno,
 * reflejando la demanda y el contexto operativo de cada periodo del día.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Shift {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                                   // Nombre (ej: "Mañana", "Tarde")
   private String code;                                   // Código único (ej: "MORNING")
   private String description;                            // Descripción detallada (opcional)

   // ========================= CAMPOS DE HORARIO =========================

   private LocalTime startTime;                           // Hora de inicio (ej: 06:00)
   private LocalTime endTime;                             // Hora de fin (ej: 13:59)

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
    * Turno empieza INACTIVO hasta ser configurado completamente.
    */
   public Shift() {
      this.status = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param name nombre del turno
    * @param code código único
    * @param startTime hora de inicio
    * @param endTime hora de fin
    */
   public Shift(String name, String code, LocalTime startTime, LocalTime endTime) {
      this();
      this.name = name;
      this.code = code;
      this.startTime = startTime;
      this.endTime = endTime;
   }

   /**
    * Constructor completo.
    *
    * @param name nombre del turno
    * @param code código único
    * @param startTime hora de inicio
    * @param endTime hora de fin
    * @param description descripción
    */
   public Shift(String name, String code, LocalTime startTime, LocalTime endTime, String description) {
      this(name, code, startTime, endTime);
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el identificador completo del turno.
    *
    * @return identificador formateado (ej: "[MORNING] Mañana")
    */
   public String getFullName() {
      return "[" + code + "] " + name;
   }

   /**
    * Obtiene el rango horario formateado.
    *
    * @return rango horario (ej: "06:00 - 13:59")
    */
   public String getTimeRange() {
      return startTime + " - " + endTime;
   }

   /**
    * Calcula la duración del turno en horas.
    * Maneja correctamente turnos que cruzan medianoche.
    *
    * @return duración en horas
    */
   public double getDurationHours() {
      if (startTime == null || endTime == null) {
         return 0.0;
      }

      int startMinutes = startTime.getHour() * 60 + startTime.getMinute();
      int endMinutes = endTime.getHour() * 60 + endTime.getMinute();

      // Si el turno cruza medianoche (ej: 20:00 - 05:59)
      if (endMinutes <= startMinutes) {
         endMinutes += 24 * 60; // Sumar 24 horas
      }

      int durationMinutes = endMinutes - startMinutes;
      return durationMinutes / 60.0;
   }

   // ========================= MÉTODOS DE NEGOCIO - VALIDACIÓN HORARIA =========================

   /**
    * Verifica si una hora específica está dentro de este turno.
    * Maneja correctamente turnos que cruzan medianoche.
    *
    * @param time hora a verificar
    * @return true si la hora está dentro del turno
    */
   public boolean isWithinShift(LocalTime time) {
      if (time == null || startTime == null || endTime == null) {
         return false;
      }

      // Si el turno NO cruza medianoche (ej: 06:00 - 13:59)
      if (startTime.isBefore(endTime) || startTime.equals(endTime)) {
         return !time.isBefore(startTime) && !time.isAfter(endTime);
      }

      // Si el turno cruza medianoche (ej: 20:00 - 05:59)
      return !time.isBefore(startTime) || !time.isAfter(endTime);
   }

   /**
    * Verifica si el turno cruza la medianoche.
    *
    * @return true si endTime es antes que startTime
    */
   public boolean crossesMidnight() {
      if (startTime == null || endTime == null) {
         return false;
      }
      return endTime.isBefore(startTime);
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa el turno.
    * Permite que sea usado en la configuración de tarifas.
    */
   public void activate() {
      this.status = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva el turno.
    * No se podrán crear nuevas tarifas con este turno.
    * Las tarifas existentes NO se ven afectadas.
    */
   public void deactivate() {
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el turno está activo.
    *
    * @return true si status = true
    */
   public boolean isActive() {
      return status;
   }

   /**
    * Verifica si el turno está inactivo.
    *
    * @return true si status = false
    */
   public boolean isInactive() {
      return !status;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el turno como eliminado (soft delete).
    * También lo desactiva automáticamente.
    * Las tarifas existentes con este turno NO se eliminan.
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
    * Restaura un turno previamente eliminado.
    * Queda inactivo, debe ser activado manualmente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el turno está eliminado.
    *
    * @return true si deletedAt no es null
    */
   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================

   /**
    * Actualiza la información básica del turno.
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
    * Actualiza el horario del turno.
    *
    * @param startTime nueva hora de inicio
    * @param endTime nueva hora de fin
    */
   public void updateSchedule(LocalTime startTime, LocalTime endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
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
    * Verifica si el turno tiene horario configurado.
    *
    * @return true si tiene startTime y endTime
    */
   public boolean hasSchedule() {
      return startTime != null && endTime != null;
   }

   /**
    * Verifica si el turno puede ser usado.
    * Debe estar activo, no eliminado y con horario configurado.
    *
    * @return true si puede ser usado
    */
   public boolean isUsable() {
      return isActive() && !isDeleted() && hasSchedule();
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

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public LocalTime getStartTime() {
      return startTime;
   }

   public void setStartTime(LocalTime startTime) {
      this.startTime = startTime;
   }

   public LocalTime getEndTime() {
      return endTime;
   }

   public void setEndTime(LocalTime endTime) {
      this.endTime = endTime;
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
    * Dos turnos son iguales si tienen el mismo ID o el mismo código.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Shift shift = (Shift) o;
      return Objects.equals(id, shift.id) || Objects.equals(code, shift.code);
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
      return "Shift{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", timeRange='" + getTimeRange() + '\'' +
            ", status=" + (status ? "ACTIVO" : "INACTIVO") +
            ", durationHours=" + String.format("%.1f", getDurationHours()) +
            ", crossesMidnight=" + crossesMidnight() +
            '}';
   }
}