package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.mapper;

import com.winnersystems.smartparking.parking.domain.model.PaymentType;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.entity.PaymentTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypePersistenceMapper {

   public PaymentType toDomain(PaymentTypeEntity entity) {
      if (entity == null) return null;

      PaymentType paymentType = new PaymentType(
            entity.getCode(),
            entity.getName()
      );

      paymentType.setId(entity.getId());
      paymentType.setDescription(entity.getDescription());

      if (Boolean.TRUE.equals(entity.getStatus())) {
         paymentType.activate();
      } else {
         paymentType.deactivate();
      }

      paymentType.setCreatedAt(entity.getCreatedAt());
      paymentType.setCreatedBy(entity.getCreatedBy());
      paymentType.setUpdatedAt(entity.getUpdatedAt());
      paymentType.setUpdatedBy(entity.getUpdatedBy());
      paymentType.setDeletedAt(entity.getDeletedAt());
      paymentType.setDeletedBy(entity.getDeletedBy());

      return paymentType;
   }

   public PaymentTypeEntity toEntity(PaymentType domain) {
      if (domain == null) return null;

      return PaymentTypeEntity.builder()
            .id(domain.getId())
            .code(domain.getCode())
            .name(domain.getName())
            .description(domain.getDescription())
            .status(domain.getStatus())
            .createdAt(domain.getCreatedAt())
            .createdBy(domain.getCreatedBy())
            .updatedAt(domain.getUpdatedAt())
            .updatedBy(domain.getUpdatedBy())
            .deletedAt(domain.getDeletedAt())
            .deletedBy(domain.getDeletedBy())
            .build();
   }

   public void updateEntity(PaymentTypeEntity entity, PaymentType domain) {
      if (entity == null || domain == null) return;

      // code es inmutable — no se actualiza
      entity.setName(domain.getName());
      entity.setDescription(domain.getDescription());
      entity.setStatus(domain.getStatus());
      entity.setUpdatedAt(domain.getUpdatedAt());
      entity.setUpdatedBy(domain.getUpdatedBy());
      entity.setDeletedAt(domain.getDeletedAt());
      entity.setDeletedBy(domain.getDeletedBy());
   }
}