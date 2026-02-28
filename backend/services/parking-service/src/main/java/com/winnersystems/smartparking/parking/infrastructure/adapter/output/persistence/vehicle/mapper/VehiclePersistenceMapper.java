package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.mapper;

import com.winnersystems.smartparking.parking.domain.model.Vehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Vehicle (dominio) y VehicleEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class VehiclePersistenceMapper {

   public VehicleEntity toEntity(Vehicle vehicle) {
      if (vehicle == null) {
         return null;
      }

      return VehicleEntity.builder()
            .id(vehicle.getId())
            .licensePlate(vehicle.getLicensePlate())
            .color(vehicle.getColor())
            .brand(vehicle.getBrand())
            .firstSeenDate(vehicle.getFirstSeenDate())
            .lastSeenDate(vehicle.getLastSeenDate())
            .totalVisits(vehicle.getTotalVisits())
            .createdAt(vehicle.getCreatedAt())
            .createdBy(vehicle.getCreatedBy())
            .updatedAt(vehicle.getUpdatedAt())
            .updatedBy(vehicle.getUpdatedBy())
            .deletedAt(vehicle.getDeletedAt())
            .deletedBy(vehicle.getDeletedBy())
            .build();
   }

   public Vehicle toDomain(VehicleEntity entity) {
      if (entity == null) {
         return null;
      }

      Vehicle vehicle = new Vehicle();
      vehicle.setId(entity.getId());
      vehicle.setLicensePlate(entity.getLicensePlate());
      vehicle.setColor(entity.getColor());
      vehicle.setBrand(entity.getBrand());
      vehicle.setFirstSeenDate(entity.getFirstSeenDate());
      vehicle.setLastSeenDate(entity.getLastSeenDate());
      vehicle.setTotalVisits(entity.getTotalVisits());
      vehicle.setCreatedAt(entity.getCreatedAt());
      vehicle.setCreatedBy(entity.getCreatedBy());
      vehicle.setUpdatedAt(entity.getUpdatedAt());
      vehicle.setUpdatedBy(entity.getUpdatedBy());
      vehicle.setDeletedAt(entity.getDeletedAt());
      vehicle.setDeletedBy(entity.getDeletedBy());

      return vehicle;
   }
}