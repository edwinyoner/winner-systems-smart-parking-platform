package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.mapper;

import com.winnersystems.smartparking.parking.domain.model.Space;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.entity.SpaceEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Space (dominio) y SpaceEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class SpacePersistenceMapper {

   public Space toDomain(SpaceEntity entity) {
      if (entity == null) return null;

      Space space = new Space(entity.getZoneId(), entity.getType(), entity.getCode());
      space.setId(entity.getId());
      space.setDescription(entity.getDescription());
      space.setWidth(entity.getWidth());
      space.setLength(entity.getLength());
      space.setHasSensor(entity.getHasSensor());
      space.setSensorId(entity.getSensorId());
      space.setHasCameraCoverage(entity.getHasCameraCoverage());
      space.setStatus(entity.getStatus());
      space.setCreatedAt(entity.getCreatedAt());
      space.setCreatedBy(entity.getCreatedBy());
      space.setUpdatedAt(entity.getUpdatedAt());
      space.setUpdatedBy(entity.getUpdatedBy());
      space.setDeletedAt(entity.getDeletedAt());
      space.setDeletedBy(entity.getDeletedBy());
      return space;
   }

   public SpaceEntity toEntity(Space space) {
      if (space == null) return null;

      return SpaceEntity.builder()
            .id(space.getId())
            .zoneId(space.getZoneId())
            .type(space.getType())
            .code(space.getCode())
            .description(space.getDescription())
            .width(space.getWidth())
            .length(space.getLength())
            .hasSensor(space.getHasSensor())
            .sensorId(space.getSensorId())
            .hasCameraCoverage(space.getHasCameraCoverage())
            .status(space.getStatus())
            .createdAt(space.getCreatedAt())
            .createdBy(space.getCreatedBy())
            .updatedAt(space.getUpdatedAt())
            .updatedBy(space.getUpdatedBy())
            .deletedAt(space.getDeletedAt())
            .deletedBy(space.getDeletedBy())
            .build();
   }

   public void updateEntity(SpaceEntity entity, Space space) {
      if (entity == null || space == null) return;

      // zoneId NO se actualiza — un espacio no cambia de zona
      entity.setType(space.getType());
      entity.setDescription(space.getDescription());
      entity.setWidth(space.getWidth());
      entity.setLength(space.getLength());
      entity.setHasSensor(space.getHasSensor());
      entity.setSensorId(space.getSensorId());
      entity.setHasCameraCoverage(space.getHasCameraCoverage());
      entity.setStatus(space.getStatus());
      entity.setUpdatedAt(space.getUpdatedAt());
      entity.setUpdatedBy(space.getUpdatedBy());
      entity.setDeletedAt(space.getDeletedAt());
      entity.setDeletedBy(space.getDeletedBy());
   }
}