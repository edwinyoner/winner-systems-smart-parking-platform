package com.winnersystems.smartparking.parking.application.port.input.documenttype;

import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;

public interface ToggleDocumentTypeStatusUseCase {
   DocumentTypeDto toggleDocumentTypeStatus(Long documentTypeId);
   DocumentTypeDto activateDocumentTypeDto(Long documentTypeId);
   DocumentTypeDto deactivateDocumentTypeDto(Long documentTypeId);
}