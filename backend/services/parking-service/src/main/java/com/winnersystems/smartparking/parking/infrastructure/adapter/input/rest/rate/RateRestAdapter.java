package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;
import com.winnersystems.smartparking.parking.application.port.input.rate.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.request.CreateRateRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.request.UpdateRateRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.response.RateResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.mapper.RateRestMapper;

import jakarta.validation.Valid;
import java.util.List;

/**
 * REST Adapter para gestión de tarifas (Rates).
 * Implementa CRUD completo + operaciones de estado.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/rates")
@RequiredArgsConstructor
public class RateRestAdapter {

   private final CreateRateUseCase createRateUseCase;
   private final UpdateRateUseCase updateRateUseCase;
   private final DeleteRateUseCase deleteRateUseCase;
   private final GetRateUseCase getRateUseCase;
   private final ListRatesUseCase listRatesUseCase;
   private final ToggleRateStatusUseCase toggleRateStatusUseCase;
   private final RateRestMapper mapper;

   @GetMapping
   public ResponseEntity<PagedResponse<RateResponse>> listAllRates(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) String status) {

      log.info("Obteniendo tarifas - página: {}, tamaño: {}, search: {}, status: {}", page, size, search, status);

      // Convertir status String → Boolean (null si no viene)
      Boolean statusBool = (status != null && !status.isBlank()) ? Boolean.parseBoolean(status) : null;

      PagedResponse<RateDto> pagedDto = listRatesUseCase.listAllRates(page, size, search, statusBool);
      PagedResponse<RateResponse> response = pagedDto.map(mapper::toResponse);

      log.info("Se encontraron {} tarifas", response.totalElements());
      return ResponseEntity.ok(response);
   }

   @GetMapping("/active")
   public ResponseEntity<List<RateResponse>> listActiveRates() {
      log.info("Obteniendo tarifas activas");

      List<RateDto> activeRatesDto = listRatesUseCase.listAllActiveRates();

      List<RateResponse> responses = activeRatesDto.stream()
            .map(mapper::toResponse)
            .toList();  // Stream moderno

      log.info("Se encontraron {} tarifas activas", responses.size());
      return ResponseEntity.ok(responses);
   }

   @GetMapping("/{id}")
   public ResponseEntity<RateResponse> getRateById(@PathVariable Long id) {
      log.info("Buscando tarifa con ID: {}", id);

      RateDto dto = getRateUseCase.getRateById(id);  // Retorna directo (sin Optional)

      log.info("Tarifa encontrada: {}", dto.name());
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   @PostMapping
   public ResponseEntity<RateResponse> createRate(@Valid @RequestBody CreateRateRequest request) {
      log.info("Creando nueva tarifa: {}", request.getName());

      RateDto createdDto = createRateUseCase.createRate(mapper.toCommand(request));

      log.info("Tarifa creada exitosamente con ID: {}", createdDto.id());
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(createdDto));
   }

   @PutMapping("/{id}")
   public ResponseEntity<RateResponse> updateRate(
         @PathVariable Long id,
         @Valid @RequestBody UpdateRateRequest request) {

      log.info("Actualizando tarifa con ID: {}", id);

      // id separado del Command
      RateDto updatedDto = updateRateUseCase.updateRate(id, mapper.toCommand(request));

      log.info("Tarifa actualizada exitosamente");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteRate(@PathVariable Long id) {
      log.info("Eliminando tarifa con ID: {}", id);

      deleteRateUseCase.deleteRate(id);

      log.info("Tarifa eliminada exitosamente");
      return ResponseEntity.noContent().build();
   }

   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<RateResponse> toggleStatus(@PathVariable Long id) {
      log.info("Cambiando estado de tarifa con ID: {}", id);

      RateDto updatedDto = toggleRateStatusUseCase.toggleRateStatus(id);

      log.info("Estado cambiado a: {}", updatedDto.status() ? "ACTIVA" : "INACTIVA");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @PatchMapping("/{id}/activate")
   public ResponseEntity<RateResponse> activateRate(@PathVariable Long id) {
      log.info("Activando tarifa con ID: {}", id);

      RateDto updatedDto = toggleRateStatusUseCase.activateRate(id);

      log.info("Tarifa activada");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }

   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<RateResponse> deactivateRate(@PathVariable Long id) {
      log.info("Desactivando tarifa con ID: {}", id);

      RateDto updatedDto = toggleRateStatusUseCase.deactivateRate(id);

      log.info("Tarifa desactivada");
      return ResponseEntity.ok(mapper.toResponse(updatedDto));
   }
}