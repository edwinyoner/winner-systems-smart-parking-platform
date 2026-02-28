package com.winnersystems.smartparking.parking.application.port.input.documenttype;

import com.winnersystems.smartparking.parking.application.dto.command.CreateDocumentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;

public interface CreateDocumentTypeUseCase {
   DocumentTypeDto createDocumentType(CreateDocumentTypeCommand command);
}