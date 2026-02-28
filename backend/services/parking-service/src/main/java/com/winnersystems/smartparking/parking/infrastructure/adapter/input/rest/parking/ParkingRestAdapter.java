package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;
import com.winnersystems.smartparking.parking.application.port.input.parking.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.request.CreateParkingRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.request.UpdateParkingRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.response.ParkingResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.mapper.ParkingRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Adapter para gestión de parkings.
 * Implementa CRUD completo + estado.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/parkings")
public class ParkingRestAdapter {

   private final CreateParkingUseCase createParkingUseCase;
   private final UpdateParkingUseCase updateParkingUseCase;
   private final DeleteParkingUseCase deleteParkingUseCase;
   private final GetParkingUseCase getParkingUseCase;
   private final ListParkingsUseCase listParkingsUseCase;
   private final ToggleParkingStatusUseCase toggleParkingStatusUseCase;
   private final ParkingRestMapper mapper;

   public ParkingRestAdapter(
         CreateParkingUseCase createParkingUseCase,
         UpdateParkingUseCase updateParkingUseCase,
         DeleteParkingUseCase deleteParkingUseCase,
         GetParkingUseCase getParkingUseCase,
         ListParkingsUseCase listParkingsUseCase,
         ToggleParkingStatusUseCase toggleParkingStatusUseCase,
         ParkingRestMapper mapper) {
      this.createParkingUseCase = createParkingUseCase;
      this.updateParkingUseCase = updateParkingUseCase;
      this.deleteParkingUseCase = deleteParkingUseCase;
      this.getParkingUseCase = getParkingUseCase;
      this.listParkingsUseCase = listParkingsUseCase;
      this.toggleParkingStatusUseCase = toggleParkingStatusUseCase;
      this.mapper = mapper;
   }

   @PostMapping
   public ResponseEntity<ParkingResponse> createParking(@Valid @RequestBody CreateParkingRequest request) {
      ParkingDto parkingDto = createParkingUseCase.createParking(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(parkingDto));
   }

   @GetMapping("/{id}")
   public ResponseEntity<ParkingResponse> getParkingById(@PathVariable Long id) {
      ParkingDto parkingDto = getParkingUseCase.getParkingById(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @GetMapping
   public ResponseEntity<PagedResponse<ParkingResponse>> listAllParkings(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<ParkingDto> pagedDto = listParkingsUseCase.listAllParkings(page, size, null, null);

      PagedResponse<ParkingResponse> response = pagedDto.map(mapper::toResponse);

      return ResponseEntity.ok(response);
   }

   @GetMapping("/active")
   public ResponseEntity<List<ParkingResponse>> listActiveParkings() {
      List<ParkingResponse> response = listParkingsUseCase.listAllActiveParkings()
            .stream()
            .map(mapper::toResponse)
            .toList();
      return ResponseEntity.ok(response);
   }

   @PutMapping("/{id}")
   public ResponseEntity<ParkingResponse> updateParking(
         @PathVariable Long id,
         @Valid @RequestBody UpdateParkingRequest request) {

      ParkingDto parkingDto = updateParkingUseCase.updateParking(id, mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<ParkingResponse> toggleStatus(@PathVariable Long id) {
      ParkingDto parkingDto = toggleParkingStatusUseCase.toggleParkingStatus(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @PatchMapping("/{id}/activate")
   public ResponseEntity<ParkingResponse> activateParking(@PathVariable Long id) {
      ParkingDto parkingDto = toggleParkingStatusUseCase.activateParking(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<ParkingResponse> deactivateParking(@PathVariable Long id) {
      ParkingDto parkingDto = toggleParkingStatusUseCase.deactivateParking(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @PatchMapping("/{id}/maintenance")
   public ResponseEntity<ParkingResponse> setInMaintenance(@PathVariable Long id) {
      ParkingDto parkingDto = toggleParkingStatusUseCase.setInMaintenance(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @PatchMapping("/{id}/out-of-service")
   public ResponseEntity<ParkingResponse> setOutOfService(@PathVariable Long id) {
      ParkingDto parkingDto = toggleParkingStatusUseCase.setOutOfService(id);
      return ResponseEntity.ok(mapper.toResponse(parkingDto));
   }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteParking(@PathVariable Long id) {
      deleteParkingUseCase.deleteParking(id);
      return ResponseEntity.noContent().build();
   }
}