// domain/model/Parking.java
package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un PARQUEO o ESTACIONAMIENTO GENERAL.
 *
 * Un parqueo es un contenedor lógico que agrupa múltiples zonas de estacionamiento.
 * Representa una ubicación física completa (ej: "Parqueo Simón Bolívar").
 *
 * Ejemplos:
 * - "Parqueo Simón Bolívar" (tiene 4 zonas alrededor)
 * - "Parqueo Mercado Central" (tiene 2 zonas)
 * - "Parqueo Plaza de Armas" (tiene 4 zonas)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Parking {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                        // "Parqueo Simón Bolívar"
   private String code;                        // "PSB"
   private String description;                 // Descripción general

   // ========================= CAMPOS DE UBICACIÓN =========================

   private String address;                     // Dirección general (ej: "Distrito de Huaraz")
   private Double latitude;                    // Coordenadas GPS del centro
   private Double longitude;

   // ========================= CAMPOS DE GESTIÓN =========================

   private Long managerId;                     // FK a auth-service (responsable del parqueo)
   private String managerName;                 // Nombre del responsable (desnormalizado)

   // ========================= CAMPOS DE CAPACIDAD (Calculados) =========================

   private Integer totalZones;                 // Cantidad de zonas
   private Integer totalSpaces;                // Total de espacios (suma de todas las zonas)
   private Integer availableSpaces;            // Espacios disponibles (calculado)

   // ========================= CAMPOS DE ESTADO =========================

   private String status;                      // ACTIVE, INACTIVE, MAINTENANCE, OUT_OF_SERVICE

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;
   private Long createdBy;
   private LocalDateTime updatedAt;
   private Long updatedBy;
   private LocalDateTime deletedAt;
   private Long deletedBy;

   // ========================= CONSTANTES DE ESTADO =========================

   public static final String STATUS_ACTIVE = "ACTIVE";
   public static final String STATUS_INACTIVE = "INACTIVE";
   public static final String STATUS_MAINTENANCE = "MAINTENANCE";
   public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";

   // ========================= CONSTRUCTORES =========================

   public Parking() {
      this.status = STATUS_ACTIVE;
      this.totalZones = 0;
      this.totalSpaces = 0;
      this.availableSpaces = 0;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   public Parking(String name, String code, String address) {
      this();
      this.name = name;
      this.code = code;
      this.address = address;
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   public void activate() {
      this.status = STATUS_ACTIVE;
      this.updatedAt = LocalDateTime.now();
   }

   public void deactivate() {
      this.status = STATUS_INACTIVE;
      this.updatedAt = LocalDateTime.now();
   }

   public void setInMaintenance() {
      this.status = STATUS_MAINTENANCE;
      this.updatedAt = LocalDateTime.now();
   }

   public void setOutOfService() {
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= MÉTODOS DE NEGOCIO - CONSULTAS =========================

   public boolean isActive() {
      return STATUS_ACTIVE.equals(status);
   }

   public boolean isInactive() {
      return STATUS_INACTIVE.equals(status);
   }

   public boolean isOperational() {
      return isActive() && !isDeleted();
   }

   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - CAPACIDAD =========================

   public void updateTotalZones(Integer zones) {
      this.totalZones = zones;
      this.updatedAt = LocalDateTime.now();
   }

   public void updateCapacity(Integer totalSpaces, Integer availableSpaces) {
      this.totalSpaces = totalSpaces;
      this.availableSpaces = availableSpaces;
      this.updatedAt = LocalDateTime.now();
   }

   public Double getOccupancyPercentage() {
      if (totalSpaces == null || totalSpaces == 0) {
         return 0.0;
      }
      int occupied = totalSpaces - (availableSpaces != null ? availableSpaces : 0);
      return (occupied * 100.0) / totalSpaces;
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN =========================

   public void updateBasicInfo(String name, String address, String description) {
      this.name = name;
      this.address = address;
      this.description = description;
      this.updatedAt = LocalDateTime.now();
   }

   public void updateLocation(Double latitude, Double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.updatedAt = LocalDateTime.now();
   }

   public void assignManager(Long managerId, String managerName) {
      this.managerId = managerId;
      this.managerName = managerName;
      this.updatedAt = LocalDateTime.now();
   }

   public void validate() {
      if (name == null || name.trim().isEmpty()) {
         throw new IllegalArgumentException("El nombre del parqueo es obligatorio");
      }
      if (code == null || code.trim().isEmpty()) {
         throw new IllegalArgumentException("El código del parqueo es obligatorio");
      }
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

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public Double getLatitude() {
      return latitude;
   }

   public void setLatitude(Double latitude) {
      this.latitude = latitude;
   }

   public Double getLongitude() {
      return longitude;
   }

   public void setLongitude(Double longitude) {
      this.longitude = longitude;
   }

   public Long getManagerId() {
      return managerId;
   }

   public void setManagerId(Long managerId) {
      this.managerId = managerId;
   }

   public String getManagerName() {
      return managerName;
   }

   public void setManagerName(String managerName) {
      this.managerName = managerName;
   }

   public Integer getTotalZones() {
      return totalZones;
   }

   public void setTotalZones(Integer totalZones) {
      this.totalZones = totalZones;
   }

   public Integer getTotalSpaces() {
      return totalSpaces;
   }

   public void setTotalSpaces(Integer totalSpaces) {
      this.totalSpaces = totalSpaces;
   }

   public Integer getAvailableSpaces() {
      return availableSpaces;
   }

   public void setAvailableSpaces(Integer availableSpaces) {
      this.availableSpaces = availableSpaces;
   }

   public String getStatus() {
      return status;
   }

   public void setStatus(String status) {
      this.status = status;
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

   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Parking parking = (Parking) o;
      return Objects.equals(id, parking.id) || Objects.equals(code, parking.code);
   }

   @Override
   public int hashCode() {
      return Objects.hash(id, code);
   }

   @Override
   public String toString() {
      return "Parking{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", code='" + code + '\'' +
            ", totalZones=" + totalZones +
            ", totalSpaces=" + totalSpaces +
            ", status='" + status + '\'' +
            '}';
   }
}