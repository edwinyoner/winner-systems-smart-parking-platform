package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.CreateDocumentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateDocumentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.request.CreateDocumentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.request.UpdateDocumentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.response.DocumentTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class DocumentTypeRestMapper {

   public CreateDocumentTypeCommand toCommand(CreateDocumentTypeRequest request) {
      return CreateDocumentTypeCommand.builder()
            .code(request.getCode().toUpperCase().trim())
            .name(request.getName())
            .description(request.getDescription())
            .build();
   }

   public UpdateDocumentTypeCommand toCommand(UpdateDocumentTypeRequest request) {
      return UpdateDocumentTypeCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .status(request.getStatus())
            .build();
   }

   public DocumentTypeResponse toResponse(DocumentTypeDto dto) {
      return new DocumentTypeResponse(
            dto.id(),
            dto.code(),
            dto.name(),
            dto.description(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}