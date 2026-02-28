package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.mapper;

import com.winnersystems.smartparking.parking.domain.model.Rate;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.entity.RateEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Rate (dominio) y RateEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class RatePersistenceMapper {

   /**
    * Convierte de entidad JPA a modelo de dominio.
    *
    * @param entity entidad JPA
    * @return modelo de dominio
    */
   public Rate toDomain(RateEntity entity) {
      if (entity == null) {
         return null;
      }

      Rate rate = new Rate(
            entity.getName(),
            entity.getAmount(),
            entity.getDescription()
      );

      // Configurar ID
      rate.setId(entity.getId());
      rate.setCurrency(entity.getCurrency());

      // Establecer estado usando métodos de dominio
      if (Boolean.TRUE.equals(entity.getStatus())) {
         rate.activate();
      } else {
         rate.deactivate();
      }

      // Auditoría
      rate.setCreatedAt(entity.getCreatedAt());
      rate.setCreatedBy(entity.getCreatedBy());
      rate.setUpdatedAt(entity.getUpdatedAt());
      rate.setUpdatedBy(entity.getUpdatedBy());
      rate.setDeletedAt(entity.getDeletedAt());
      rate.setDeletedBy(entity.getDeletedBy());

      return rate;
   }

   /**
    * Convierte de modelo de dominio a entidad JPA.
    *
    * @param rate modelo de dominio
    * @return entidad JPA
    */
   public RateEntity toEntity(Rate rate) {
      if (rate == null) {
         return null;
      }

      return RateEntity.builder()
            .id(rate.getId())
            .name(rate.getName())
            .description(rate.getDescription())
            .amount(rate.getAmount())
            .currency(rate.getCurrency())
            .status(rate.getStatus())  // boolean → Boolean
            .createdAt(rate.getCreatedAt())
            .createdBy(rate.getCreatedBy())
            .updatedAt(rate.getUpdatedAt())
            .updatedBy(rate.getUpdatedBy())
            .deletedAt(rate.getDeletedAt())
            .deletedBy(rate.getDeletedBy())
            .build();
   }

   /**
    * Actualiza una entidad existente con datos del dominio.
    * Preserva el ID y las fechas de creación.
    *
    * @param entity entidad a actualizar
    * @param rate modelo de dominio con datos actualizados
    */
   public void updateEntity(RateEntity entity, Rate rate) {
      if (entity == null || rate == null) {
         return;
      }

      // Actualizar solo campos modificables
      entity.setName(rate.getName());
      entity.setDescription(rate.getDescription());
      entity.setAmount(rate.getAmount());
      entity.setCurrency(rate.getCurrency());
      entity.setStatus(rate.getStatus());

      // Actualizar metadata de modificación
      entity.setUpdatedAt(rate.getUpdatedAt());
      entity.setUpdatedBy(rate.getUpdatedBy());

      // Soft delete
      entity.setDeletedAt(rate.getDeletedAt());
      entity.setDeletedBy(rate.getDeletedBy());
   }
}