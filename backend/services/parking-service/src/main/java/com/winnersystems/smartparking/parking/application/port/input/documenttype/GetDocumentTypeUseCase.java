package com.winnersystems.smartparking.parking.application.port.input.documenttype;

import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;

public interface GetDocumentTypeUseCase {
   DocumentTypeDto getDocumentTypeById(Long documentTypeId);
}