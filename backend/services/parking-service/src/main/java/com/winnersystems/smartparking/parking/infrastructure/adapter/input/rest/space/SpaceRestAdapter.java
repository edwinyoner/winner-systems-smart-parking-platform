package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;
import com.winnersystems.smartparking.parking.application.port.input.space.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.request.CreateSpaceRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.request.UpdateSpaceRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.response.SpaceResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.mapper.SpaceRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Adapter para gestión de espacios de estacionamiento.
 * Implementa CRUD completo + operaciones de estado.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/spaces")
public class SpaceRestAdapter {

   private final CreateSpaceUseCase createSpaceUseCase;
   private final UpdateSpaceUseCase updateSpaceUseCase;
   private final DeleteSpaceUseCase deleteSpaceUseCase;
   private final GetSpaceUseCase getSpaceUseCase;
   private final ListSpacesUseCase listSpacesUseCase;
   private final ToggleSpaceStatusUseCase toggleSpaceStatusUseCase;
   private final SpaceRestMapper mapper;

   public SpaceRestAdapter(
         CreateSpaceUseCase createSpaceUseCase,
         UpdateSpaceUseCase updateSpaceUseCase,
         DeleteSpaceUseCase deleteSpaceUseCase,
         GetSpaceUseCase getSpaceUseCase,
         ListSpacesUseCase listSpacesUseCase,
         ToggleSpaceStatusUseCase toggleSpaceStatusUseCase,
         SpaceRestMapper mapper) {
      this.createSpaceUseCase = createSpaceUseCase;
      this.updateSpaceUseCase = updateSpaceUseCase;
      this.deleteSpaceUseCase = deleteSpaceUseCase;
      this.getSpaceUseCase = getSpaceUseCase;
      this.listSpacesUseCase = listSpacesUseCase;
      this.toggleSpaceStatusUseCase = toggleSpaceStatusUseCase;
      this.mapper = mapper;
   }

   // ========================= CREATE =========================

   @PostMapping
   public ResponseEntity<SpaceResponse> createSpace(@Valid @RequestBody CreateSpaceRequest request) {
      SpaceDto spaceDto = createSpaceUseCase.createSpace(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(spaceDto));
   }

   /**
    * Crea múltiples espacios en batch (Paso 3 del stepper).
    *
    * @param requests lista de espacios a crear
    * @return lista de espacios creados
    */
   @PostMapping("/batch")
   public ResponseEntity<List<SpaceResponse>> createBatch(
         @Valid @RequestBody List<CreateSpaceRequest> requests
   ) {
      // Mapear requests a commands
      List<SpaceDto> spaceDtos = requests.stream()
            .map(req -> createSpaceUseCase.createSpace(mapper.toCommand(req)))
            .toList();

      // Mapear DTOs a responses
      List<SpaceResponse> responses = spaceDtos.stream()
            .map(mapper::toResponse)
            .toList();

      return ResponseEntity.status(HttpStatus.CREATED).body(responses);
   }

   // ========================= GET =========================

   @GetMapping("/{id}")
   public ResponseEntity<SpaceResponse> getSpaceById(@PathVariable Long id) {
      SpaceDto spaceDto = getSpaceUseCase.getSpaceById(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   // ========================= LIST =========================

   @GetMapping
   public ResponseEntity<PagedResponse<SpaceResponse>> listAllSpaces(
         @RequestParam(defaultValue = "0") int pageNumber,
         @RequestParam(defaultValue = "10") int pageSize,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String status) {

      PagedResponse<SpaceDto> pagedDto = listSpacesUseCase.listAllSpaces(pageNumber, pageSize, search, status);
      return ResponseEntity.ok(pagedDto.map(mapper::toResponse));
   }

   @GetMapping("/active")
   public ResponseEntity<List<SpaceResponse>> listAllActiveSpaces() {
      List<SpaceResponse> response = listSpacesUseCase.listAllActiveSpaces()
            .stream()
            .map(mapper::toResponse)
            .toList();
      return ResponseEntity.ok(response);
   }

   @GetMapping("/zone/{zoneId}")
   public ResponseEntity<List<SpaceResponse>> listSpacesByZone(@PathVariable Long zoneId) {
      List<SpaceResponse> response = listSpacesUseCase.listSpacesByZone(zoneId)
            .stream()
            .map(mapper::toResponse)
            .toList();
      return ResponseEntity.ok(response);
   }

   // ========================= UPDATE =========================

   @PutMapping("/{id}")
   public ResponseEntity<SpaceResponse> updateSpace(
         @PathVariable Long id,
         @Valid @RequestBody UpdateSpaceRequest request) {

      SpaceDto spaceDto = updateSpaceUseCase.updateSpace(id, mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   // ========================= TOGGLE STATUS =========================

   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<SpaceResponse> toggleSpaceStatus(@PathVariable Long id) {
      SpaceDto spaceDto = toggleSpaceStatusUseCase.toggleSpaceStatus(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   @PatchMapping("/{id}/occupy")
   public ResponseEntity<SpaceResponse> markAsOccupied(@PathVariable Long id) {
      SpaceDto spaceDto = toggleSpaceStatusUseCase.markAsOccupied(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   @PatchMapping("/{id}/release")
   public ResponseEntity<SpaceResponse> markAsAvailable(@PathVariable Long id) {
      SpaceDto spaceDto = toggleSpaceStatusUseCase.markAsAvailable(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   @PatchMapping("/{id}/maintenance")
   public ResponseEntity<SpaceResponse> setInMaintenance(@PathVariable Long id) {
      SpaceDto spaceDto = toggleSpaceStatusUseCase.setInMaintenance(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   @PatchMapping("/{id}/out-of-service")
   public ResponseEntity<SpaceResponse> setOutOfService(@PathVariable Long id) {
      SpaceDto spaceDto = toggleSpaceStatusUseCase.setOutOfService(id);
      return ResponseEntity.ok(mapper.toResponse(spaceDto));
   }

   // ========================= DELETE =========================

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteSpace(@PathVariable Long id) {
      deleteSpaceUseCase.deleteSpace(id);
      return ResponseEntity.noContent().build();
   }
}