package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;
import com.winnersystems.smartparking.parking.application.port.input.zone.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request.CreateZoneRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request.UpdateZoneRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.response.ZoneResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.mapper.ZoneRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Adapter para gestión de zonas de estacionamiento.
 * Implementa CRUD completo + búsqueda y estado.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/zones")
@RequiredArgsConstructor
public class ZoneRestAdapter {

   private final CreateZoneUseCase createZoneUseCase;
   private final UpdateZoneUseCase updateZoneUseCase;
   private final DeleteZoneUseCase deleteZoneUseCase;
   private final GetZoneUseCase getZoneUseCase;
   private final ListZonesUseCase listZonesUseCase;
   private final ToggleZoneStatusUseCase toggleZoneStatusUseCase;
   private final ZoneRestMapper mapper;

   // ========================= CRUD BÁSICO =========================

   @PostMapping
   public ResponseEntity<ZoneResponse> createZone(@Valid @RequestBody CreateZoneRequest request) {
      ZoneDto zoneDto = createZoneUseCase.createZone(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(zoneDto));
   }

   @GetMapping("/{id}")
   public ResponseEntity<ZoneResponse> getZoneById(@PathVariable Long id) {
      ZoneDto zoneDto = getZoneUseCase.getZoneById(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @GetMapping
   public ResponseEntity<PagedResponse<ZoneResponse>> listAllZones(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String status
   ) {
      PagedResponse<ZoneDto> pagedDto = listZonesUseCase.listAllZones(page, size, search, status);
      return ResponseEntity.ok(pagedDto.map(mapper::toResponse));
   }

   @GetMapping("/active")
   public ResponseEntity<List<ZoneResponse>> listActiveZones() {
      List<ZoneDto> activeZones = listZonesUseCase.listAllActiveZones();

      List<ZoneResponse> response = activeZones.stream()
            .map(mapper::toResponse)
            .toList();

      return ResponseEntity.ok(response);
   }

   @PutMapping("/{id}")
   public ResponseEntity<ZoneResponse> updateZone(
         @PathVariable Long id,
         @Valid @RequestBody UpdateZoneRequest request
   ) {
      ZoneDto zoneDto = updateZoneUseCase.updateZone(id, mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
      deleteZoneUseCase.deleteZone(id);
      return ResponseEntity.noContent().build();
   }

   // ========================= GESTIÓN DE ESTADO =========================

   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<ZoneResponse> toggleStatus(@PathVariable Long id) {
      ZoneDto zoneDto = toggleZoneStatusUseCase.toggleZoneStatus(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @PatchMapping("/{id}/activate")
   public ResponseEntity<ZoneResponse> activateZone(@PathVariable Long id) {
      ZoneDto zoneDto = toggleZoneStatusUseCase.activateZone(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<ZoneResponse> deactivateZone(@PathVariable Long id) {
      ZoneDto zoneDto = toggleZoneStatusUseCase.deactivateZone(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @PatchMapping("/{id}/maintenance")
   public ResponseEntity<ZoneResponse> setInMaintenance(@PathVariable Long id) {
      ZoneDto zoneDto = toggleZoneStatusUseCase.setInMaintenance(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }

   @PatchMapping("/{id}/out-of-service")
   public ResponseEntity<ZoneResponse> setOutOfService(@PathVariable Long id) {
      ZoneDto zoneDto = toggleZoneStatusUseCase.setOutOfService(id);
      return ResponseEntity.ok(mapper.toResponse(zoneDto));
   }
}