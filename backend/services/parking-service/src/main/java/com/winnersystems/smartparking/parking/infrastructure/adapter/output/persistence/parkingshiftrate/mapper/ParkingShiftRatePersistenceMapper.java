package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.mapper;

import com.winnersystems.smartparking.parking.domain.model.ParkingShiftRate;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.entity.ParkingShiftRateEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre ParkingShiftRate (dominio) y ParkingShiftRateEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ParkingShiftRatePersistenceMapper {

   /**
    * Convierte de entidad JPA a modelo de dominio.
    *
    * @param entity entidad JPA
    * @return modelo de dominio
    */
   public ParkingShiftRate toDomain(ParkingShiftRateEntity entity) {
      if (entity == null) {
         return null;
      }

      ParkingShiftRate config = new ParkingShiftRate(
            entity.getParkingId(),
            entity.getShiftId(),
            entity.getRateId(),
            entity.getStatus()
      );

      config.setId(entity.getId());
      config.setCreatedAt(entity.getCreatedAt());
      config.setCreatedBy(entity.getCreatedBy());
      config.setUpdatedAt(entity.getUpdatedAt());
      config.setUpdatedBy(entity.getUpdatedBy());

      return config;
   }

   /**
    * Convierte de modelo de dominio a entidad JPA.
    *
    * @param config modelo de dominio
    * @return entidad JPA
    */
   public ParkingShiftRateEntity toEntity(ParkingShiftRate config) {
      if (config == null) {
         return null;
      }

      return ParkingShiftRateEntity.builder()
            .id(config.getId())
            .parkingId(config.getParkingId())
            .shiftId(config.getShiftId())
            .rateId(config.getRateId())
            .status(config.getStatus())
            .createdAt(config.getCreatedAt())
            .createdBy(config.getCreatedBy())
            .updatedAt(config.getUpdatedAt())
            .updatedBy(config.getUpdatedBy())
            .build();
   }

   /**
    * Actualiza una entidad existente con datos del dominio.
    * Solo actualiza campos modificables (rate, status, audit).
    *
    * @param entity entidad a actualizar
    * @param config modelo con nuevos datos
    */
   public void updateEntity(ParkingShiftRateEntity entity, ParkingShiftRate config) {
      if (entity == null || config == null) {
         return;
      }

      entity.setRateId(config.getRateId());
      entity.setStatus(config.getStatus());
      entity.setUpdatedAt(config.getUpdatedAt());
      entity.setUpdatedBy(config.getUpdatedBy());
   }
}