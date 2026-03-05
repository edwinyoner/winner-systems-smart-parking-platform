package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.mapper;

import com.winnersystems.smartparking.parking.domain.model.Vehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper bidireccional entre Vehicle (dominio) y VehicleEntity (persistencia).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class VehiclePersistenceMapper {

   /**
    * Convierte Vehicle (dominio) → VehicleEntity (JPA).
    */
   public VehicleEntity toEntity(Vehicle vehicle) {
      if (vehicle == null) {
         return null;
      }

      VehicleEntity entity = new VehicleEntity();

      // ID
      entity.setId(vehicle.getId());
      entity.setLicensePlate(vehicle.getLicensePlate());

      // Tracking
      entity.setFirstSeenDate(vehicle.getFirstSeenDate());
      entity.setLastSeenDate(vehicle.getLastSeenDate());
      entity.setTotalVisits(vehicle.getTotalVisits());

      // Auditoría
      entity.setCreatedAt(vehicle.getCreatedAt());
      entity.setCreatedBy(vehicle.getCreatedBy());
      entity.setUpdatedAt(vehicle.getUpdatedAt());
      entity.setUpdatedBy(vehicle.getUpdatedBy());
      entity.setDeletedAt(vehicle.getDeletedAt());
      entity.setDeletedBy(vehicle.getDeletedBy());

      return entity;
   }

   /**
    * Convierte VehicleEntity (JPA) → Vehicle (dominio).
    */
   public Vehicle toDomain(VehicleEntity entity) {
      if (entity == null) {
         return null;
      }

      Vehicle vehicle = new Vehicle();

      // ID
      vehicle.setId(entity.getId());
      vehicle.setLicensePlate(entity.getLicensePlate());

      // Tracking
      vehicle.setFirstSeenDate(entity.getFirstSeenDate());
      vehicle.setLastSeenDate(entity.getLastSeenDate());
      vehicle.setTotalVisits(entity.getTotalVisits());

      // Auditoría
      vehicle.setCreatedAt(entity.getCreatedAt());
      vehicle.setCreatedBy(entity.getCreatedBy());
      vehicle.setUpdatedAt(entity.getUpdatedAt());
      vehicle.setUpdatedBy(entity.getUpdatedBy());
      vehicle.setDeletedAt(entity.getDeletedAt());
      vehicle.setDeletedBy(entity.getDeletedBy());

      return vehicle;
   }
}