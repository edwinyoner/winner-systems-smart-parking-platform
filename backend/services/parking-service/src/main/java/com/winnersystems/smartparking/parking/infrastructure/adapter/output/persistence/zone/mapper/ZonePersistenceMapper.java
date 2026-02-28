
package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.mapper;

import com.winnersystems.smartparking.parking.domain.model.Zone;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.entity.ZoneEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Zone (dominio) y ZoneEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ZonePersistenceMapper {

   public Zone toDomain(ZoneEntity entity) {
      if (entity == null) return null;

      Zone zone = new Zone(entity.getName(), entity.getCode(), entity.getAddress());
      zone.setId(entity.getId());
      zone.setParkingId(entity.getParkingId());          // ← AGREGADO
      zone.setDescription(entity.getDescription());
      zone.setTotalSpaces(entity.getTotalSpaces());
      zone.setAvailableSpaces(entity.getAvailableSpaces());
      zone.setLatitude(entity.getLatitude());
      zone.setLongitude(entity.getLongitude());
      zone.setHasCamera(entity.getHasCamera());
      zone.setCameraIds(entity.getCameraIds());
      zone.setStatus(entity.getStatus());
      zone.setCreatedAt(entity.getCreatedAt());
      zone.setCreatedBy(entity.getCreatedBy());
      zone.setUpdatedAt(entity.getUpdatedAt());
      zone.setUpdatedBy(entity.getUpdatedBy());
      zone.setDeletedAt(entity.getDeletedAt());
      zone.setDeletedBy(entity.getDeletedBy());
      return zone;
   }

   public ZoneEntity toEntity(Zone zone) {
      if (zone == null) return null;

      return ZoneEntity.builder()
            .id(zone.getId())
            .parkingId(zone.getParkingId())              // ← AGREGADO
            .name(zone.getName())
            .code(zone.getCode())
            .address(zone.getAddress())
            .description(zone.getDescription())
            .totalSpaces(zone.getTotalSpaces())
            .availableSpaces(zone.getAvailableSpaces())
            .latitude(zone.getLatitude())
            .longitude(zone.getLongitude())
            .hasCamera(zone.getHasCamera())
            .cameraIds(zone.getCameraIds())
            .status(zone.getStatus())
            .createdAt(zone.getCreatedAt())
            .createdBy(zone.getCreatedBy())
            .updatedAt(zone.getUpdatedAt())
            .updatedBy(zone.getUpdatedBy())
            .deletedAt(zone.getDeletedAt())
            .deletedBy(zone.getDeletedBy())
            .build();
   }

   public void updateEntity(ZoneEntity entity, Zone zone) {
      if (entity == null || zone == null) return;

      // parkingId NO se actualiza — una zona no cambia de parking
      entity.setName(zone.getName());
      entity.setAddress(zone.getAddress());
      entity.setDescription(zone.getDescription());
      entity.setTotalSpaces(zone.getTotalSpaces());
      entity.setAvailableSpaces(zone.getAvailableSpaces());
      entity.setLatitude(zone.getLatitude());
      entity.setLongitude(zone.getLongitude());
      entity.setHasCamera(zone.getHasCamera());
      entity.setCameraIds(zone.getCameraIds());
      entity.setStatus(zone.getStatus());
      entity.setUpdatedAt(zone.getUpdatedAt());
      entity.setUpdatedBy(zone.getUpdatedBy());
      entity.setDeletedAt(zone.getDeletedAt());
      entity.setDeletedBy(zone.getDeletedBy());
   }
}