package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.mapper;

import com.winnersystems.smartparking.parking.domain.model.DocumentType;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.entity.DocumentTypeEntity;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypePersistenceMapper {

   public DocumentType toDomain(DocumentTypeEntity entity) {
      if (entity == null) return null;

      DocumentType documentType = new DocumentType(
            entity.getCode(),
            entity.getName()
      );

      documentType.setId(entity.getId());
      documentType.setDescription(entity.getDescription());

      if (Boolean.TRUE.equals(entity.getStatus())) {
         documentType.activate();
      } else {
         documentType.deactivate();
      }

      documentType.setCreatedAt(entity.getCreatedAt());
      documentType.setCreatedBy(entity.getCreatedBy());
      documentType.setUpdatedAt(entity.getUpdatedAt());
      documentType.setUpdatedBy(entity.getUpdatedBy());
      documentType.setDeletedAt(entity.getDeletedAt());
      documentType.setDeletedBy(entity.getDeletedBy());

      return documentType;
   }

   public DocumentTypeEntity toEntity(DocumentType domain) {
      if (domain == null) return null;

      return DocumentTypeEntity.builder()
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

   public void updateEntity(DocumentTypeEntity entity, DocumentType domain) {
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