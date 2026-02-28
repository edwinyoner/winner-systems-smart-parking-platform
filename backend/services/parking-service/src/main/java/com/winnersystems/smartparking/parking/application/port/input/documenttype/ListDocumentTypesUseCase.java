package com.winnersystems.smartparking.parking.application.port.input.documenttype;

import com.winnersystems.smartparking.parking.application.dto.query.*;

import java.util.List;

public interface ListDocumentTypesUseCase {
   PagedResponse<DocumentTypeDto> listAllDocumentTypes(int page, int size, String search, Boolean status);
   List<DocumentTypeDto> listAllActiveDocumentTypes();
}