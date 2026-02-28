package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype;

import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.port.input.documenttype.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.request.CreateDocumentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.request.UpdateDocumentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.response.DocumentTypeResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.mapper.DocumentTypeRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/document-types")
public class DocumentTypeRestAdapter {

   private final CreateDocumentTypeUseCase createUseCase;
   private final UpdateDocumentTypeUseCase updateUseCase;
   private final DeleteDocumentTypeUseCase deleteUseCase;
   private final GetDocumentTypeUseCase getUseCase;
   private final ListDocumentTypesUseCase listUseCase;
   private final ToggleDocumentTypeStatusUseCase toggleUseCase;
   private final DocumentTypeRestMapper mapper;

   public DocumentTypeRestAdapter(
         CreateDocumentTypeUseCase createUseCase,
         UpdateDocumentTypeUseCase updateUseCase,
         DeleteDocumentTypeUseCase deleteUseCase,
         GetDocumentTypeUseCase getUseCase,
         ListDocumentTypesUseCase listUseCase,
         ToggleDocumentTypeStatusUseCase toggleUseCase,
         DocumentTypeRestMapper mapper) {
      this.createUseCase = createUseCase;
      this.updateUseCase = updateUseCase;
      this.deleteUseCase = deleteUseCase;
      this.getUseCase = getUseCase;
      this.listUseCase = listUseCase;
      this.toggleUseCase = toggleUseCase;
      this.mapper = mapper;
   }

   // POST /api/v1/document-types
   @PostMapping
   public ResponseEntity<DocumentTypeResponse> create(
         @Valid @RequestBody CreateDocumentTypeRequest request) {
      DocumentTypeDto dto = createUseCase.createDocumentType(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(dto));
   }

   // PUT /api/v1/document-types/{id}
   @PutMapping("/{id}")
   public ResponseEntity<DocumentTypeResponse> update(
         @PathVariable Long id,
         @Valid @RequestBody UpdateDocumentTypeRequest request) {
      DocumentTypeDto dto = updateUseCase.updateDocumentType(id, mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // DELETE /api/v1/document-types/{id}
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {
      deleteUseCase.deleteDocumentType(id);
      return ResponseEntity.noContent().build();
   }

   // GET /api/v1/document-types/{id}
   @GetMapping("/{id}")
   public ResponseEntity<DocumentTypeResponse> getById(@PathVariable Long id) {
      DocumentTypeDto dto = getUseCase.getDocumentTypeById(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // GET /api/v1/document-types?page=0&size=10&search=dni&status=true
   @GetMapping
   public ResponseEntity<PagedResponse<DocumentTypeResponse>> list(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) Boolean status) {
      PagedResponse<DocumentTypeDto> result = listUseCase.listAllDocumentTypes(page, size, search, status);
      return ResponseEntity.ok(result.map(mapper::toResponse));
   }

   // GET /api/v1/document-types/active
   @GetMapping("/active")
   public ResponseEntity<List<DocumentTypeResponse>> listActive() {
      List<DocumentTypeResponse> response = listUseCase.listAllActiveDocumentTypes().stream()
            .map(mapper::toResponse)
            .toList();
      return ResponseEntity.ok(response);
   }

   // PATCH /api/v1/document-types/{id}/toggle-status
   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<DocumentTypeResponse> toggleStatus(@PathVariable Long id) {
      DocumentTypeDto dto = toggleUseCase.toggleDocumentTypeStatus(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // PATCH /api/v1/document-types/{id}/activate
   @PatchMapping("/{id}/activate")
   public ResponseEntity<DocumentTypeResponse> activate(@PathVariable Long id) {
      DocumentTypeDto dto = toggleUseCase.activateDocumentTypeDto(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // PATCH /api/v1/document-types/{id}/deactivate
   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<DocumentTypeResponse> deactivate(@PathVariable Long id) {
      DocumentTypeDto dto = toggleUseCase.deactivateDocumentTypeDto(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }
}