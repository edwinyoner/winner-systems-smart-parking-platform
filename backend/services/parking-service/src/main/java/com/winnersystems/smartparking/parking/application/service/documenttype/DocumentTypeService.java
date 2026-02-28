package com.winnersystems.smartparking.parking.application.service.documenttype;

import com.winnersystems.smartparking.parking.application.dto.command.CreateDocumentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateDocumentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.documenttype.*;
import com.winnersystems.smartparking.parking.application.port.output.DocumentTypePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentTypeService implements
      CreateDocumentTypeUseCase,
      UpdateDocumentTypeUseCase,
      DeleteDocumentTypeUseCase,
      GetDocumentTypeUseCase,
      ListDocumentTypesUseCase,
      ToggleDocumentTypeStatusUseCase {

   private final DocumentTypePersistencePort persistencePort;

   public DocumentTypeService(DocumentTypePersistencePort persistencePort) {
      this.persistencePort = persistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public DocumentTypeDto createDocumentType(CreateDocumentTypeCommand command) {
      if (persistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException("Ya existe un tipo de documento con el código: " + command.getCode());
      }

      DocumentType documentType = new DocumentType(
            command.getCode().toUpperCase().trim(),
            command.getName()
      );
      documentType.setDescription(command.getDescription());

      return toDto(persistencePort.save(documentType));
   }

   // ========================= UPDATE =========================

   @Override
   public DocumentTypeDto updateDocumentType(Long documentTypeId, UpdateDocumentTypeCommand command) {
      DocumentType documentType = findOrThrow(documentTypeId);

      documentType.updateBasicInfo(command.getName(), command.getDescription());

      if (command.getStatus() != null) {
         if (command.getStatus()) {
            documentType.activate();
         } else {
            documentType.deactivate();
         }
      }

      return toDto(persistencePort.save(documentType));
   }

   // ========================= DELETE =========================

   @Override
   public void deleteDocumentType(Long documentTypeId) {
      findOrThrow(documentTypeId);
      persistencePort.delete(documentTypeId);
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public DocumentTypeDto getDocumentTypeById(Long documentTypeId) {
      return toDto(findOrThrow(documentTypeId));
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<DocumentTypeDto> listAllDocumentTypes(int page, int size, String search, Boolean status) {
      PageRequest pageRequest = PageRequest.of(page, size, "id", "ASC");
      PageResult<DocumentType> result = persistencePort.findAll(pageRequest, search, status);
      return result.toPagedResponse(this::toDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<DocumentTypeDto> listAllActiveDocumentTypes() {
      return persistencePort.findAllActive().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
   }

   // ========================= TOGGLE =========================

   @Override
   public DocumentTypeDto toggleDocumentTypeStatus(Long documentTypeId) {
      DocumentType documentType = findOrThrow(documentTypeId);
      if (documentType.isActive()) {
         documentType.deactivate();
      } else {
         documentType.activate();
      }
      return toDto(persistencePort.save(documentType));
   }

   @Override
   public DocumentTypeDto activateDocumentTypeDto(Long documentTypeId) {
      DocumentType documentType = findOrThrow(documentTypeId);
      documentType.activate();
      return toDto(persistencePort.save(documentType));
   }

   @Override
   public DocumentTypeDto deactivateDocumentTypeDto(Long documentTypeId) {
      DocumentType documentType = findOrThrow(documentTypeId);
      documentType.deactivate();
      return toDto(persistencePort.save(documentType));
   }

   // ========================= HELPERS =========================

   private DocumentType findOrThrow(Long id) {
      return persistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tipo de documento no encontrado con ID: " + id));
   }

   private DocumentTypeDto toDto(DocumentType d) {
      return new DocumentTypeDto(
            d.getId(),
            d.getCode(),
            d.getName(),
            d.getDescription(),
            d.getStatus(),
            d.getCreatedAt(),
            d.getUpdatedAt()
      );
   }
}