// domain/model/Space.java
package com.winnersystems.smartparking.parking.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad de dominio que representa un ESPACIO DE ESTACIONAMIENTO individual.
 *
 * Un espacio es la unidad básica donde se estaciona un vehículo. Pertenece a una zona
 * específica y tiene un tipo de espacio asignado.
 *
 * Ejemplos de identificación: "A-001", "B-015", "DISC-01"
 *
 * Estados posibles:
 * - AVAILABLE: libre para ser ocupado
 * - OCCUPIED: actualmente ocupado por un vehículo
 * - MAINTENANCE: en mantenimiento activo (trabajo en curso)
 * - OUT_OF_SERVICE: fuera de servicio (sin trabajo activo)
 *
 * Tipos de espacios según Ordenanza Municipal:
 * - PARALLEL: Estacionamiento paralelo (6.4m x 2.40m)
 * - DIAGONAL: Estacionamiento en diagonal (ángulo respecto a la vía)
 * - PERPENDICULAR: Estacionamiento perpendicular 90° (5.10m x 2.40m)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class Space {

   // ========================= CAMPOS DE IDENTIDAD =========================

   private Long id;
   private Long zoneId;                                   // FK a Zone
   private String type;                                   // PARALLEL, DIAGONAL, PERPENDICULAR
   private String code;                                   // Código del espacio (ej: "A-001")
   private String description;                            // Descripción de ubicación (opcional)

   // ========================= CAMPOS DE CARACTERÍSTICAS =========================

   private Double width;                                  // Ancho en metros (opcional)
   private Double length;                                 // Largo en metros (opcional)

   // ========================= CAMPOS DE EQUIPAMIENTO IoT =========================

   private Boolean hasSensor;                             // ¿Tiene sensor de ocupación?
   private String sensorId;                               // ID del sensor (futuro iot-service)
   private Boolean hasCameraCoverage;                     // ¿Está cubierto por cámara?

   // ========================= CAMPOS DE ESTADO =========================

   private String status;                                 // AVAILABLE, OCCUPIED, MAINTENANCE, OUT_OF_SERVICE

   // ========================= CAMPOS DE AUDITORÍA =========================

   private LocalDateTime createdAt;
   private Long createdBy;
   private LocalDateTime updatedAt;
   private Long updatedBy;
   private LocalDateTime deletedAt;                       // Soft delete
   private Long deletedBy;

   // ========================= CONSTANTES DE ESTADO =========================

   public static final String STATUS_AVAILABLE = "AVAILABLE";
   public static final String STATUS_OCCUPIED = "OCCUPIED";
   public static final String STATUS_MAINTENANCE = "MAINTENANCE";
   public static final String STATUS_OUT_OF_SERVICE = "OUT_OF_SERVICE";

   // ========================= CONSTANTES DE TIPO =========================

   public static final String TYPE_PARALLEL = "PARALLEL";
   public static final String TYPE_DIAGONAL = "DIAGONAL";
   public static final String TYPE_PERPENDICULAR = "PERPENDICULAR";

   // ========================= CONSTRUCTORES =========================

   /**
    * Constructor vacío - Inicializa con valores por defecto.
    * Espacio empieza DISPONIBLE.
    */
   public Space() {
      this.status = STATUS_AVAILABLE;
      this.hasSensor = false;
      this.hasCameraCoverage = false;
      this.createdAt = LocalDateTime.now();
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Constructor con campos básicos.
    *
    * @param zoneId ID de la zona a la que pertenece
    * @param type tipo de espacio (PARALLEL, DIAGONAL, PERPENDICULAR)
    * @param code código del espacio
    */
   public Space(Long zoneId, String type, String code) {
      this();
      this.zoneId = zoneId;
      this.type = type;
      this.code = code;
   }

   /**
    * Constructor completo.
    *
    * @param zoneId ID de la zona
    * @param type tipo de espacio
    * @param code código del espacio
    * @param description descripción de ubicación
    */
   public Space(Long zoneId, String type, String code, String description) {
      this(zoneId, type, code);
      this.description = description;
   }

   // ========================= MÉTODOS DE NEGOCIO - INFORMACIÓN =========================

   /**
    * Obtiene el identificador completo del espacio.
    *
    * @return código del espacio
    */
   public String getFullIdentifier() {
      return code;
   }

   /**
    * Calcula el área del espacio si tiene dimensiones.
    *
    * @return área en m² o null si no tiene dimensiones
    */
   public Double getArea() {
      if (width != null && length != null) {
         return width * length;
      }
      return null;
   }

   /**
    * Obtiene el nombre legible del tipo de espacio.
    *
    * @return nombre del tipo o "No especificado"
    */
   public String getTypeName() {
      if (type == null) {
         return "No especificado";
      }
      switch (type) {
         case TYPE_PARALLEL:
            return "Paralelo";
         case TYPE_DIAGONAL:
            return "Diagonal";
         case TYPE_PERPENDICULAR:
            return "Perpendicular";
         default:
            return type;
      }
   }

   // ========================= MÉTODOS DE NEGOCIO - GESTIÓN DE ESTADO =========================

   /**
    * Marca el espacio como OCUPADO.
    * Solo se puede ocupar si está DISPONIBLE.
    *
    * @return true si se ocupó exitosamente
    */
   public boolean markAsOccupied() {
      if (isAvailableForOccupation()) {
         this.status = STATUS_OCCUPIED;
         this.updatedAt = LocalDateTime.now();
         return true;
      }
      return false;
   }

   /**
    * Marca el espacio como DISPONIBLE.
    * Libera el espacio para ser usado nuevamente.
    */
   public void markAsAvailable() {
      this.status = STATUS_AVAILABLE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Pone el espacio en MANTENIMIENTO.
    * Indica que hay trabajo activo en curso.
    */
   public void setInMaintenance() {
      this.status = STATUS_MAINTENANCE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Pone el espacio FUERA DE SERVICIO.
    * Indica que está deshabilitado sin trabajo activo.
    */
   public void setOutOfService() {
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Reactiva el espacio.
    * Lo devuelve al estado DISPONIBLE.
    */
   public void activate() {
      this.status = STATUS_AVAILABLE;
      this.updatedAt = LocalDateTime.now();
   }

   // ========================= MÉTODOS DE NEGOCIO - CONSULTAS DE ESTADO =========================

   /**
    * Verifica si el espacio está disponible.
    *
    * @return true si status = AVAILABLE
    */
   public boolean isAvailable() {
      return STATUS_AVAILABLE.equals(status);
   }

   /**
    * Verifica si el espacio está ocupado.
    *
    * @return true si status = OCCUPIED
    */
   public boolean isOccupied() {
      return STATUS_OCCUPIED.equals(status);
   }

   /**
    * Verifica si el espacio está en mantenimiento.
    *
    * @return true si status = MAINTENANCE
    */
   public boolean isInMaintenance() {
      return STATUS_MAINTENANCE.equals(status);
   }

   /**
    * Verifica si el espacio está fuera de servicio.
    *
    * @return true si status = OUT_OF_SERVICE
    */
   public boolean isOutOfService() {
      return STATUS_OUT_OF_SERVICE.equals(status);
   }

   /**
    * Verifica si el espacio está disponible para ser ocupado.
    * Debe estar DISPONIBLE y no eliminado.
    *
    * @return true si puede ser ocupado ahora
    */
   public boolean isAvailableForOccupation() {
      return isAvailable() && !isDeleted();
   }

   /**
    * Verifica si el espacio es operacional.
    * Debe estar disponible u ocupado (no en mantenimiento ni fuera de servicio).
    *
    * @return true si puede operar
    */
   public boolean isOperational() {
      return (isAvailable() || isOccupied()) && !isDeleted();
   }

   /**
    * Verifica si el espacio NO está usable.
    * Está en mantenimiento o fuera de servicio.
    *
    * @return true si no se puede usar
    */
   public boolean isNotUsable() {
      return isInMaintenance() || isOutOfService();
   }

   // ========================= MÉTODOS DE NEGOCIO - CONSULTAS DE TIPO =========================

   /**
    * Verifica si el tipo de espacio es paralelo.
    *
    * @return true si type = PARALLEL
    */
   public boolean isParallel() {
      return TYPE_PARALLEL.equals(type);
   }

   /**
    * Verifica si el tipo de espacio es diagonal.
    *
    * @return true si type = DIAGONAL
    */
   public boolean isDiagonal() {
      return TYPE_DIAGONAL.equals(type);
   }

   /**
    * Verifica si el tipo de espacio es perpendicular.
    *
    * @return true si type = PERPENDICULAR
    */
   public boolean isPerpendicular() {
      return TYPE_PERPENDICULAR.equals(type);
   }

   // ========================= MÉTODOS DE NEGOCIO - CARACTERÍSTICAS IoT =========================

   /**
    * Configura el sensor de ocupación.
    *
    * @param sensorId ID del sensor (iot-service)
    */
   public void configureSensor(String sensorId) {
      this.hasSensor = true;
      this.sensorId = sensorId;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Remueve la configuración del sensor.
    */
   public void removeSensor() {
      this.hasSensor = false;
      this.sensorId = null;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Marca el espacio como cubierto por cámara.
    */
   public void enableCameraCoverage() {
      this.hasCameraCoverage = true;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Marca el espacio como no cubierto por cámara.
    */
   public void disableCameraCoverage() {
      this.hasCameraCoverage = false;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si tiene sensor activo.
    *
    * @return true si hasSensor = true y tiene sensorId
    */
   public boolean hasActiveSensor() {
      return Boolean.TRUE.equals(hasSensor) && sensorId != null && !sensorId.isEmpty();
   }

   /**
    * Verifica si tiene cobertura de cámara.
    *
    * @return true si hasCameraCoverage = true
    */
   public boolean hasCameraMonitoring() {
      return Boolean.TRUE.equals(hasCameraCoverage);
   }

   // ========================= MÉTODOS DE NEGOCIO - SOFT DELETE =========================

   /**
    * Marca el espacio como eliminado (soft delete).
    * También lo pone fuera de servicio automáticamente.
    *
    * @param deletedByUserId ID del usuario que elimina
    */
   public void markAsDeleted(Long deletedByUserId) {
      this.deletedAt = LocalDateTime.now();
      this.deletedBy = deletedByUserId;
      this.status = STATUS_OUT_OF_SERVICE;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Restaura un espacio previamente eliminado.
    * Queda fuera de servicio, debe ser activado manualmente.
    */
   public void restore() {
      this.deletedAt = null;
      this.deletedBy = null;
      this.status = STATUS_OUT_OF_SERVICE;               // Requiere activación manual
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Verifica si el espacio está eliminado.
    *
    * @return true si deletedAt no es null
    */
   public boolean isDeleted() {
      return deletedAt != null;
   }

   // ========================= MÉTODOS DE NEGOCIO - ACTUALIZACIÓN DE DATOS =========================

   /**
    * Actualiza el tipo de espacio.
    *
    * @param newType nuevo tipo (PARALLEL, DIAGONAL, PERPENDICULAR)
    */
   public void updateSpaceType(String newType) {
      this.type = newType;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza las dimensiones del espacio.
    *
    * @param width ancho en metros
    * @param length largo en metros
    */
   public void updateDimensions(Double width, Double length) {
      this.width = width;
      this.length = length;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza la descripción de ubicación.
    *
    * @param description descripción de ubicación
    */
   public void updateDescription(String description) {
      this.description = description;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza el código del espacio.
    *
    * @param code nuevo código
    */
   public void updateCode(String code) {
      this.code = code;
      this.updatedAt = LocalDateTime.now();
   }

   /**
    * Actualiza información básica del espacio.
    *
    * @param code código del espacio
    * @param description descripción
    * @param width ancho en metros
    * @param length largo en metros
    */
   public void updateBasicInfo(String code, String description, Double width, Double length) {
      this.code = code;
      this.description = description;
      this.width = width;
      this.length = length;
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

   /**
    * Valida que el espacio tenga datos consistentes.
    */
   public void validate() {
      if (code == null || code.trim().isEmpty()) {
         throw new IllegalArgumentException("El código del espacio es obligatorio");
      }
      if (zoneId == null) {
         throw new IllegalArgumentException("El espacio debe pertenecer a una zona");
      }
      if (type == null || type.trim().isEmpty()) {
         throw new IllegalArgumentException("El tipo de espacio es obligatorio");
      }
      if (!isValidType(type)) {
         throw new IllegalArgumentException("El tipo de espacio debe ser PARALLEL, DIAGONAL o PERPENDICULAR");
      }
   }

   /**
    * Verifica si el tipo de espacio es válido.
    *
    * @param type tipo a validar
    * @return true si es un tipo válido
    */
   private boolean isValidType(String type) {
      return TYPE_PARALLEL.equals(type) ||
            TYPE_DIAGONAL.equals(type) ||
            TYPE_PERPENDICULAR.equals(type);
   }

   // ========================= GETTERS Y SETTERS =========================

   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public Long getZoneId() {
      return zoneId;
   }

   public void setZoneId(Long zoneId) {
      this.zoneId = zoneId;
   }

   public String getType() {
      return type;
   }

   public void setType(String type) {
      this.type = type;
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

   public Double getWidth() {
      return width;
   }

   public void setWidth(Double width) {
      this.width = width;
   }

   public Double getLength() {
      return length;
   }

   public void setLength(Double length) {
      this.length = length;
   }

   public Boolean getHasSensor() {
      return hasSensor;
   }

   public void setHasSensor(Boolean hasSensor) {
      this.hasSensor = hasSensor;
   }

   public String getSensorId() {
      return sensorId;
   }

   public void setSensorId(String sensorId) {
      this.sensorId = sensorId;
   }

   public Boolean getHasCameraCoverage() {
      return hasCameraCoverage;
   }

   public void setHasCameraCoverage(Boolean hasCameraCoverage) {
      this.hasCameraCoverage = hasCameraCoverage;
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
    * Dos espacios son iguales si tienen el mismo ID o misma zona + código.
    */
   @Override
   public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Space that = (Space) o;
      return Objects.equals(id, that.id) ||
            (Objects.equals(zoneId, that.zoneId) && Objects.equals(code, that.code));
   }

   /**
    * HashCode basado en ID, zoneId y code.
    */
   @Override
   public int hashCode() {
      return Objects.hash(id, zoneId, code);
   }

   /**
    * ToString para logging y debugging.
    */
   @Override
   public String toString() {
      return "Space{" +
            "id=" + id +
            ", zoneId=" + zoneId +
            ", type='" + type + '\'' +
            ", code='" + code + '\'' +
            ", status='" + status + '\'' +
            ", hasSensor=" + hasSensor +
            ", hasCameraCoverage=" + hasCameraCoverage +
            ", deletedAt=" + deletedAt +
            '}';
   }
}