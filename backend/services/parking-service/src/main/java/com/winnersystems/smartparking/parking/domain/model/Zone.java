package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Entidad de dominio que representa una ZONA DE ESTACIONAMIENTO.
 *
 * Una zona es un área física delimitada que contiene múltiples espacios de estacionamiento.
 * Puede ser una calle, plaza, o complejo completo. La municipalidad gestiona varias zonas
 * en diferentes ubicaciones de Huaraz.
 *
 * Ejemplos: "Centro Histórico Huaraz", "Plaza de Armas", "Zona Comercial Norte"
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Zone {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private String name;                                   // Nombre único de la zona
   private String code;                                   // Código único (ej: "CHU-01", "PDA-02")
   private String address;                                // Dirección física
   private String description;                            // Descripción detallada (opcional)

   private Long parkingId;  // FK a Parking

   // ========================= CAMPOS DE CAPACIDAD =========================

   private Integer totalSpaces;                           // Total de espacios en esta zona
   private Integer availableSpaces;                       // Espacios actualmente disponibles

   // ========================= CAMPOS DE GEOLOCALIZACIÓN =========================

   private Double latitude;                           // Latitud GPS (ej: -9.5277)
   private Double longitude;                          // Longitud GPS (ej: -77.5278)

   // ========================= CAMPOS DE EQUIPAMIENTO IoT =========================

   private Boolean hasCamera;                             // ¿Tiene cámaras de vigilancia?
   private String cameraIds;                              // IDs de cámaras (separadas por coma)

   // ========================= CAMPOS DE ESTADO =========================

   private String status;                                 // ACTIVE, INACTIVE, MAINTENANCE, OUT_OF_SERVICE

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;
   private Long createdBy;                                // ID del usuario que creó (auth-service)
   private LocalDateTime updatedAt;
   private Long updatedBy;                                // ID del usuario que actualizó
   private LocalDateTime deletedAt;                       // Soft delete
   private Long deletedBy;

   // ========================= CONSTANTES DE ESTADO =========================

   public static final String STATUS_ACTIVE = "ACTIVE";
   public static final String STATUS_INACTIVE = "INACTIVE";
   public static final String STATUS_MAINTENANCE = "MAINTENANCE";
   public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    * Zona empieza INACTIVA hasta ser configurada completamente.
    */
   public Zone() {
      this.status = STATUS_ACTIVE;
      this.totalSpaces = 0;
      this.availableSpaces = 0;
      this.hasCamera = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param name nombre de la zona (único)
    * @param code código identificador (único)
    * @param address dirección física
    */
   public Zone(String name, String code, String address) {
      this();
      this.name = name;
      this.code = code;
      this.address = address;
   }

   /**
    * Constructor completo.
    *
    * @param name nombre de la zona
    * @param code código único
    * @param address dirección
    * @param totalSpaces capacidad total
    */
   public Zone(String name, String code, String address, Integer totalSpaces) {
      this(name, code, address);
      this.totalSpaces = totalSpaces;
      this.availableSpaces = totalSpaces;               // Inicialmente todos disponibles
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el nombre completo de la zona incluyendo el código.
    *
    * @return nombre formateado (ej: "[CHU-01] Centro Histórico Huaraz")
    */
   public String getFullName() {
      return "[" + code + "] " + name;
   }

   /**
    * Calcula el porcentaje de ocupación de la zona.
    *
    * @return porcentaje de 0.0 a 100.0
    */
   public double getOccupancyPercentage() {
      if (totalSpaces == null || totalSpaces == 0) {
         return 0.0;
      }
      int occupied = totalSpaces - (availableSpaces != null ? availableSpaces : 0);
      return (occupied * 100.0) / totalSpaces;
   }

   /**
    * Verifica si la zona tiene capacidad disponible.
    *
    * @return true si hay al menos un espacio disponible
    */
   public boolean hasAvailableSpaces() {
      return availableSpaces != null && availableSpaces > 0;
   }

   /**
    * Verifica si la zona está completamente llena.
    *
    * @return true si no hay espacios disponibles
    */
   public boolean isFull() {
      return availableSpaces != null && availableSpaces == 0;
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Activa la zona.
    * La zona puede empezar a operar y recibir vehículos.
    */
   public void activate() {
      this.status = STATUS_ACTIVE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Desactiva la zona temporalmente.
    * No se pueden registrar nuevos ingresos.
    * Uso: eventos cortos, días festivos, limpiezas.
    */
   public void deactivate() {
      this.status = STATUS_INACTIVE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Pone la zona en mantenimiento.
    * Indica que hay trabajo activo en curso.
    */
   public void setInMaintenance() {
      this.status = STATUS_MAINTENANCE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Pone la zona fuera de servicio.
    * Indica cierre largo plazo o permanente por decisión administrativa.
    * Uso: conversión a otro uso, venta, proyectos largos.
    */
   public void setOutOfService() {
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si la zona está activa.
    *
    * @return true si status = ACTIVE
    */
   public boolean isActive() {
      return STATUS_ACTIVE.equals(status);
   }

   /**
    * Verifica si la zona está inactiva.
    *
    * @return true si status = INACTIVE
    */
   public boolean isInactive() {
      return STATUS_INACTIVE.equals(status);
   }

   /**
    * Verifica si la zona está en mantenimiento.
    *
    * @return true si status = MAINTENANCE
    */
   public boolean isInMaintenance() {
      return STATUS_MAINTENANCE.equals(status);
   }

   /**
    * Verifica si la zona está fuera de servicio.
    *
    * @return true si status = OUT_OF_SERVICE
    */
   public boolean isOutOfService() {
      return STATUS_OUT_OF_SERVICE.equals(status);
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca la zona como eliminada (soft delete).
    * También la pone fuera de servicio automáticamente.
    * Uso: errores de registro, zonas creadas por error.
    *
    * @param deletedByUserId ID del usuario administrador que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = STATUS_OUT_OF_SERVICE;               // Fuera de servicio al eliminar
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura una zona previamente eliminada.
    * La zona queda inactiva, debe ser activada manualmente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = STATUS_INACTIVE;                     // Inactiva, requiere activación manual
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si la zona está eliminada (soft delete).
    *
    * @return true si deletedAt no es null
    */
   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================

   /**
    * Actualiza la información básica de la zona.
    *
    * @param name nuevo nombre
    * @param address nueva dirección
    * @param description nueva descripción
    */
   public void updateBasicInfo(String name, String address, String description) {
      this.name = name;
      this.address = address;
      this.description = description;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza la geolocalización de la zona.
    *
    * @param latitude latitud GPS
    * @param longitude longitud GPS
    */
   public void updateLocation(Double latitude, Double longitude) {
      this.latitude = latitude;
      this.longitude = longitude;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza la capacidad total de la zona.
    * Recalcula los espacios disponibles proporcionalmente.
    *
    * @param newTotalSpaces nueva capacidad total
    */
   public void updateTotalSpaces(Integer newTotalSpaces) {
      if (newTotalSpaces != null && newTotalSpaces >= 0) {
         this.totalSpaces = newTotalSpaces;
         // Ajustar availableSpaces si excede el nuevo total
         if (this.availableSpaces != null && this.availableSpaces > newTotalSpaces) {
            this.availableSpaces = newTotalSpaces;
         }
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Incrementa los espacios disponibles (cuando un vehículo sale).
    */
   public void incrementAvailableSpaces() {
      if (this.availableSpaces == null) {
         this.availableSpaces = 0;
      }
      if (this.availableSpaces < this.totalSpaces) {
         this.availableSpaces++;
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Decrementa los espacios disponibles (cuando un vehículo entra).
    */
   public void decrementAvailableSpaces() {
      if (this.availableSpaces == null) {
         this.availableSpaces = this.totalSpaces;
      }
      if (this.availableSpaces > 0) {
         this.availableSpaces--;
         this.updatedAt = LocalDateTime.now();
      }
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

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE CÁMARAS =========================

   /**
    * Asigna una cámara a la zona.
    *
    * @param cameraId ID de la cámara (iot-service o ai-service)
    */
   public void addCamera(String cameraId) {
      if (cameraId == null || cameraId.trim().isEmpty()) {
         return;
      }

      List<String> currentCameras = getCameraIdsList();
      if (!currentCameras.contains(cameraId.trim())) {
         currentCameras.add(cameraId.trim());
         this.cameraIds = String.join(",", currentCameras);
         this.hasCamera = true;
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Remueve una cámara de la zona.
    *
    * @param cameraId ID de la cámara a remover
    */
   public void removeCamera(String cameraId) {
      if (cameraId == null || cameraId.trim().isEmpty()) {
         return;
      }

      List<String> currentCameras = getCameraIdsList();
      currentCameras.remove(cameraId.trim());

      if (currentCameras.isEmpty()) {
         this.cameraIds = null;
         this.hasCamera = false;
      } else {
         this.cameraIds = String.join(",", currentCameras);
      }
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Remueve todas las cámaras de la zona.
    */
   public void removeAllCameras() {
      this.cameraIds = null;
      this.hasCamera = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Configura múltiples cámaras a la vez.
    *
    * @param cameraIdsList lista de IDs de cámaras
    */
   public void configureCameras(List<String> cameraIdsList) {
      if (cameraIdsList == null || cameraIdsList.isEmpty()) {
         removeAllCameras();
         return;
      }

      List<String> validIds = cameraIdsList.stream()
            .filter(id -> id != null && !id.trim().isEmpty())
            .map(String::trim)
            .distinct()
            .collect(Collectors.toList());

      if (validIds.isEmpty()) {
         removeAllCameras();
      } else {
         this.cameraIds = String.join(",", validIds);
         this.hasCamera = true;
         this.updatedAt = LocalDateTime.now();
      }
   }

   /**
    * Obtiene la lista de IDs de cámaras asignadas.
    *
    * @return lista de IDs de cámaras (nunca null, puede estar vacía)
    */
   public List<String> getCameraIdsList() {
      if (cameraIds == null || cameraIds.trim().isEmpty()) {
         return new ArrayList<>();
      }
      return new ArrayList<>(Arrays.asList(cameraIds.split(",")))
            .stream()
            .map(String::trim)
            .filter(id -> !id.isEmpty())
            .collect(Collectors.toList());
   }

   /**
    * Verifica si la zona tiene cámaras configuradas.
    *
    * @return true si tiene al menos una cámara
    */
   public boolean hasCameraMonitoring() {
      return Boolean.TRUE.equals(hasCamera) && cameraIds != null && !cameraIds.trim().isEmpty();
   }

   /**
    * Obtiene la cantidad de cámaras asignadas a la zona.
    *
    * @return número de cámaras
    */
   public int getCameraCount() {
      return getCameraIdsList().size();
   }

   /**
    * Verifica si una cámara específica está asignada a esta zona.
    *
    * @param cameraId ID de la cámara a verificar
    * @return true si la cámara está asignada
    */
   public boolean hasCamera(String cameraId) {
      if (cameraId == null || cameraId.trim().isEmpty()) {
         return false;
      }
      return getCameraIdsList().contains(cameraId.trim());
   }

   // ========================= MÉTODOS DE VALIDACIÓN =========================

   /**
    * Verifica si la zona tiene geolocalización configurada.
    *
    * @return true si tiene lat/long
    */
   public boolean hasLocation() {
      return latitude != null && longitude != null;
   }

   /**
    * Verifica si la zona está operativa (activa y sin eliminar).
    *
    * @return true si puede operar
    */
   public boolean isOperational() {
      return isActive() && !isDeleted();
   }

   public void validate() {
      if (name == null || name.trim().isEmpty())
         throw new IllegalArgumentException("El nombre de la zona es obligatorio");
      if (code == null || code.trim().isEmpty())
         throw new IllegalArgumentException("El código de la zona es obligatorio");
      if (parkingId == null)
         throw new IllegalArgumentException("La zona debe pertenecer a un parqueo");
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

   public String getAddress() {
      return address;
   }

   public void setAddress(String address) {
      this.address = address;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Long getParkingId() {
      return parkingId;
   }

   public void setParkingId(Long parkingId) {
      this.parkingId = parkingId;
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

   public Boolean getHasCamera() {
      return hasCamera;
   }

   public void setHasCamera(Boolean hasCamera) {
      this.hasCamera = hasCamera;
   }

   public String getCameraIds() {
      return cameraIds;
   }

   public void setCameraIds(String cameraIds) {
      this.cameraIds = cameraIds;
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

   /**
    * Dos zonas son iguales si tienen el mismo ID o el mismo código.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Zone that = (Zone) o;
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
      return "ParkingZone{" +
            "id=" + id +
            ", code='" + code + '\'' +
            ", name='" + name + '\'' +
            ", status='" + status + '\'' +
            ", totalSpaces=" + totalSpaces +
            ", availableSpaces=" + availableSpaces +
            ", occupancy=" + String.format("%.1f%%", getOccupancyPercentage()) +
            ", cameras=" + getCameraCount() +
            '}';
   }
}