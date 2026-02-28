package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.mapper;

import com.winnersystems.smartparking.parking.domain.model.Parking;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.entity.ParkingEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Parking (dominio) y ParkingEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ParkingPersistenceMapper {

   public Parking toDomain(ParkingEntity entity) {
      if (entity == null) return null;

      Parking parking = new Parking(entity.getName(), entity.getCode(), entity.getAddress());
      parking.setId(entity.getId());
      parking.setDescription(entity.getDescription());
      parking.setLatitude(entity.getLatitude());
      parking.setLongitude(entity.getLongitude());
      parking.setManagerId(entity.getManagerId());
      parking.setManagerName(entity.getManagerName());
      parking.setTotalZones(entity.getTotalZones());
      parking.setTotalSpaces(entity.getTotalSpaces());
      parking.setAvailableSpaces(entity.getAvailableSpaces());
      parking.setStatus(entity.getStatus());
      parking.setCreatedAt(entity.getCreatedAt());
      parking.setCreatedBy(entity.getCreatedBy());
      parking.setUpdatedAt(entity.getUpdatedAt());
      parking.setUpdatedBy(entity.getUpdatedBy());
      parking.setDeletedAt(entity.getDeletedAt());
      parking.setDeletedBy(entity.getDeletedBy());
      return parking;
   }

   public ParkingEntity toEntity(Parking parking) {
      if (parking == null) return null;

      return ParkingEntity.builder()
            .id(parking.getId())
            .name(parking.getName())
            .code(parking.getCode())
            .description(parking.getDescription())
            .address(parking.getAddress())
            .latitude(parking.getLatitude())
            .longitude(parking.getLongitude())
            .managerId(parking.getManagerId())
            .managerName(parking.getManagerName())
            .totalZones(parking.getTotalZones())
            .totalSpaces(parking.getTotalSpaces())
            .availableSpaces(parking.getAvailableSpaces())
            .status(parking.getStatus())
            .createdAt(parking.getCreatedAt())
            .createdBy(parking.getCreatedBy())
            .updatedAt(parking.getUpdatedAt())
            .updatedBy(parking.getUpdatedBy())
            .deletedAt(parking.getDeletedAt())
            .deletedBy(parking.getDeletedBy())
            .build();
   }

   public void updateEntity(ParkingEntity entity, Parking parking) {
      if (entity == null || parking == null) return;

      entity.setName(parking.getName());
      entity.setDescription(parking.getDescription());
      entity.setAddress(parking.getAddress());
      entity.setLatitude(parking.getLatitude());
      entity.setLongitude(parking.getLongitude());
      entity.setManagerId(parking.getManagerId());
      entity.setManagerName(parking.getManagerName());
      entity.setTotalZones(parking.getTotalZones());
      entity.setTotalSpaces(parking.getTotalSpaces());
      entity.setAvailableSpaces(parking.getAvailableSpaces());
      entity.setStatus(parking.getStatus());
      entity.setUpdatedAt(parking.getUpdatedAt());
      entity.setUpdatedBy(parking.getUpdatedBy());
      entity.setDeletedAt(parking.getDeletedAt());
      entity.setDeletedBy(parking.getDeletedBy());
   }
}
