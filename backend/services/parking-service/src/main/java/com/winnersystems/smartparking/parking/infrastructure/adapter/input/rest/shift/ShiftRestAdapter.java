package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;
import com.winnersystems.smartparking.parking.application.port.input.shift.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request.CreateShiftRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request.UpdateShiftRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.response.ShiftResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.mapper.ShiftRestMapper;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Adapter para gestión de turnos (Shifts).
 * Implementa CRUD completo + operaciones de estado.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftRestAdapter {

   private final CreateShiftUseCase createShiftUseCase;
   private final UpdateShiftUseCase updateShiftUseCase;
   private final DeleteShiftUseCase deleteShiftUseCase;
   private final GetShiftUseCase getShiftUseCase;
   private final ListShiftsUseCase listShiftsUseCase;
   private final ToggleShiftStatusUseCase toggleShiftStatusUseCase;
   private final ShiftRestMapper mapper;

   @GetMapping
   public ResponseEntity<PagedResponse<ShiftResponse>> listAllShifts(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String status) {

      log.info("Obteniendo turnos - página: {}, tamaño: {}, search: {}, status: {}", page, size, search, status);

      // Convertir status String → Boolean (null si no viene)
      Boolean statusBool = (status != null && !status.isBlank()) ? Boolean.parseBoolean(status) : null;

      PagedResponse<ShiftDto> pagedDto = listShiftsUseCase.listAllShifts(page, size, search, statusBool);
      PagedResponse<ShiftResponse> response = pagedDto.map(mapper::toResponse);

      log.info("Se encontraron {} turnos", response.totalElements());
      return ResponseEntity.ok(response);
   }

   @GetMapping("/active")
   public ResponseEntity<List<ShiftResponse>> listActiveShifts() {
      log.info("📋 Obteniendo turnos activos");

      List<ShiftDto> activeShiftsDto = listShiftsUseCase.listAllActiveShifts();

      List<ShiftResponse> responses = activeShiftsDto.stream()
            .map(mapper::toResponse)
            .toList();  // Stream moderno

      log.info("Se encontraron {} turnos activos", responses.size());
      return ResponseEntity.ok(responses);
   }

   @GetMapping("/{id}")
   public ResponseEntity<ShiftResponse> getShiftById(@PathVariable Long id) {
      log.info("Buscando turno con ID: {}", id);

      ShiftDto dto = getShiftUseCase.getShiftById(id);  // Retorna directo (sin Optional)

      log.info("Turno encontrado: {}", dto.name());
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   @PostMapping
   public ResponseEntity<ShiftResponse> createShift(@Valid @RequestBody CreateShiftRequest request) {
      log.info("Creando nuevo turno: {} [{}]", request.getName(), request.getCode());

      ShiftDto createdDto = createShiftUseCase.createShift(mapper.toCommand(request));

      log.info("Turno creado exitosamente con ID: {}", createdDto.id());
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdDto));
   }

   @PutMapping("/{id}")
   public ResponseEntity<ShiftResponse> updateShift(
         @PathVariable Long id,
         @Valid @RequestBody UpdateShiftRequest request) {

      log.info("Actualizando turno con ID: {}", id);

      // id separado del Command
      ShiftDto updatedDto = updateShiftUseCase.updateShift(id, mapper.toCommand(request));

      log.info("Turno actualizado exitosamente");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteShift(@PathVariable Long id) {
      log.info("Eliminando turno con ID: {}", id);

      deleteShiftUseCase.deleteShift(id);

      log.info("Turno eliminado exitosamente");
      return ResponseEntity.noContent().build();
   }

   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<ShiftResponse> toggleStatus(@PathVariable Long id) {
      log.info("Cambiando estado de turno con ID: {}", id);

      ShiftDto updatedDto = toggleShiftStatusUseCase.toggleShiftStatus(id);

      log.info("Estado cambiado a: {}", updatedDto.status() ? "ACTIVO" : "INACTIVO");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @PatchMapping("/{id}/activate")
   public ResponseEntity<ShiftResponse> activateShift(@PathVariable Long id) {
      log.info("Activando turno con ID: {}", id);

      ShiftDto updatedDto = toggleShiftStatusUseCase.activateShift(id);

      log.info("Turno activado");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<ShiftResponse> deactivateShift(@PathVariable Long id) {
      log.info("Desactivando turno con ID: {}", id);

      ShiftDto updatedDto = toggleShiftStatusUseCase.deactivateShift(id);

      log.info("Turno desactivado");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }
}