package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.mapper;

import com.winnersystems.smartparking.parking.domain.model.Shift;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.entity.ShiftEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Shift (dominio) y ShiftEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ShiftPersistenceMapper {

   /**
    * Convierte de entidad JPA a modelo de dominio.
    *
    * @param entity entidad JPA
    * @return modelo de dominio
    */
   public Shift toDomain(ShiftEntity entity) {
      if (entity == null) {
         return null;
      }

      Shift shift = new Shift(
            entity.getName(),
            entity.getCode(),
            entity.getStartTime(),
            entity.getEndTime(),
            entity.getDescription()
      );

      // Configurar ID
      shift.setId(entity.getId());

      // Establecer estado usando métodos de dominio
      if (Boolean.TRUE.equals(entity.getStatus())) {
         shift.activate();
      } else {
         shift.deactivate();
      }

      // Auditoría
      shift.setCreatedAt(entity.getCreatedAt());
      shift.setCreatedBy(entity.getCreatedBy());
      shift.setUpdatedAt(entity.getUpdatedAt());
      shift.setUpdatedBy(entity.getUpdatedBy());
      shift.setDeletedAt(entity.getDeletedAt());
      shift.setDeletedBy(entity.getDeletedBy());

      return shift;
   }

   /**
    * Convierte de modelo de dominio a entidad JPA.
    *
    * @param shift modelo de dominio
    * @return entidad JPA
    */
   public ShiftEntity toEntity(Shift shift) {
      if (shift == null) {
         return null;
      }

      return ShiftEntity.builder()
            .id(shift.getId())
            .name(shift.getName())
            .code(shift.getCode())
            .description(shift.getDescription())
            .startTime(shift.getStartTime())
            .endTime(shift.getEndTime())
            .status(shift.getStatus())  // boolean → Boolean
            .createdAt(shift.getCreatedAt())
            .createdBy(shift.getCreatedBy())
            .updatedAt(shift.getUpdatedAt())
            .updatedBy(shift.getUpdatedBy())
            .deletedAt(shift.getDeletedAt())
            .deletedBy(shift.getDeletedBy())
            .build();
   }

   /**
    * Actualiza una entidad existente con datos del dominio.
    * Preserva el ID y las fechas de creación.
    * NO actualiza el código (inmutable después de creación).
    *
    * @param entity entidad a actualizar
    * @param shift modelo de dominio con datos actualizados
    */
   public void updateEntity(ShiftEntity entity, Shift shift) {
      if (entity == null || shift == null) {
         return;
      }

      // Actualizar solo campos modificables
      entity.setName(shift.getName());
      entity.setDescription(shift.getDescription());
      entity.setStartTime(shift.getStartTime());
      entity.setEndTime(shift.getEndTime());
      entity.setStatus(shift.getStatus());

      // Actualizar metadata de modificación
      entity.setUpdatedAt(shift.getUpdatedAt());
      entity.setUpdatedBy(shift.getUpdatedBy());

      // Soft delete
      entity.setDeletedAt(shift.getDeletedAt());
      entity.setDeletedBy(shift.getDeletedBy());
   }
}