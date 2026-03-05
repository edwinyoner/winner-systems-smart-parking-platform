package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.mapper;

import com.winnersystems.smartparking.parking.domain.model.CustomerVehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.entity.CustomerVehicleEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper bidireccional entre CustomerVehicle (dominio) y CustomerVehicleEntity (persistencia).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class CustomerVehiclePersistenceMapper {

   /**
    * Convierte CustomerVehicle (dominio) → CustomerVehicleEntity (JPA).
    */
   public CustomerVehicleEntity toEntity(CustomerVehicle customerVehicle) {
      if (customerVehicle == null) {
         return null;
      }

      CustomerVehicleEntity entity = new CustomerVehicleEntity();

      // IDs
      entity.setId(customerVehicle.getId());
      entity.setCustomerId(customerVehicle.getCustomerId());
      entity.setVehicleId(customerVehicle.getVehicleId());

      // Tracking
      // Nota: usageCount está en el dominio pero no en todos los constructores
      // Se puede agregar getter/setter en el dominio si es necesario

      // Auditoría (solo creación)
      entity.setCreatedAt(customerVehicle.getCreatedAt());
      entity.setCreatedBy(customerVehicle.getCreatedBy());

      return entity;
   }

   /**
    * Convierte CustomerVehicleEntity (JPA) → CustomerVehicle (dominio).
    */
   public CustomerVehicle toDomain(CustomerVehicleEntity entity) {
      if (entity == null) {
         return null;
      }

      CustomerVehicle customerVehicle = new CustomerVehicle();

      // IDs
      customerVehicle.setId(entity.getId());
      customerVehicle.setCustomerId(entity.getCustomerId());
      customerVehicle.setVehicleId(entity.getVehicleId());

      // Tracking
      // Nota: usageCount se maneja en el dominio con incrementUsage()
      // Se puede agregar getter/setter si es necesario

      // Auditoría (solo creación)
      customerVehicle.setCreatedAt(entity.getCreatedAt());
      customerVehicle.setCreatedBy(entity.getCreatedBy());

      return customerVehicle;
   }
}