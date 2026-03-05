package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction;

import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.port.input.infraction.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.AssignFineRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.CreateInfractionRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.RecordFinePaymentRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.UpdateInfractionRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.response.InfractionResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.mapper.InfractionRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Controlador REST para gestión de infracciones.
 *
 * Endpoints:
 * - POST   /api/v1/infractions                    → Crear infracción
 * - GET    /api/v1/infractions/{id}               → Obtener por ID
 * - GET    /api/v1/infractions/code/{code}        → Obtener por código
 * - PUT    /api/v1/infractions/{id}               → Actualizar infracción
 * - GET    /api/v1/infractions                    → Listar con filtros
 * - GET    /api/v1/infractions/vehicle/{id}       → Listar por vehículo
 * - GET    /api/v1/infractions/customer/{id}      → Listar por cliente
 * - GET    /api/v1/infractions/transaction/{id}   → Listar por transacción
 * - GET    /api/v1/infractions/pending-fines      → Listar multas pendientes
 * - GET    /api/v1/infractions/overdue-fines      → Listar multas vencidas
 * - POST   /api/v1/infractions/{id}/assign-fine   → Asignar multa
 * - POST   /api/v1/infractions/{id}/record-payment → Registrar pago de multa
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/infractions")
@RequiredArgsConstructor
public class InfractionRestAdapter {

   private final CreateInfractionUseCase createInfractionUseCase;
   private final GetInfractionUseCase getInfractionUseCase;
   private final UpdateInfractionUseCase updateInfractionUseCase;
   private final ListInfractionsUseCase listInfractionsUseCase;
   private final AssignFineUseCase assignFineUseCase;
   private final RecordFinePaymentUseCase recordFinePaymentUseCase;
   private final InfractionRestMapper mapper;

   // ========================= CREATE =========================

   /**
    * POST /api/v1/infractions
    * Crea una nueva infracción.
    */
   @PostMapping
   public ResponseEntity<InfractionResponse> createInfraction(
         @Valid @RequestBody CreateInfractionRequest request) {

      InfractionDto dto = createInfractionUseCase.createInfraction(
            mapper.toCreateCommand(request)
      );

      return ResponseEntity.status(HttpStatus.CREATED)
            .body(mapper.toResponse(dto));
   }

   // ========================= READ =========================

   /**
    * GET /api/v1/infractions/{id}
    * Obtiene una infracción por ID.
    */
   @GetMapping("/{id}")
   public ResponseEntity<InfractionResponse> getInfractionById(@PathVariable Long id) {
      InfractionDto dto = getInfractionUseCase.getInfractionById(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   /**
    * GET /api/v1/infractions/code/{code}
    * Obtiene una infracción por código.
    */
   @GetMapping("/code/{code}")
   public ResponseEntity<InfractionResponse> getInfractionByCode(@PathVariable String code) {
      InfractionDto dto = getInfractionUseCase.getInfractionByCode(code);
      if (dto == null) {
         return ResponseEntity.notFound().build();
      }
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // ========================= UPDATE =========================

   /**
    * PUT /api/v1/infractions/{id}
    * Actualiza una infracción existente.
    */
   @PutMapping("/{id}")
   public ResponseEntity<InfractionResponse> updateInfraction(
         @PathVariable Long id,
         @Valid @RequestBody UpdateInfractionRequest request) {

      InfractionDto dto = updateInfractionUseCase.updateInfraction(
            id,
            mapper.toUpdateCommand(request)
      );

      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // ========================= LIST =========================

   /**
    * GET /api/v1/infractions
    * Lista todas las infracciones con filtros opcionales.
    *
    * @param page número de página (default: 0)
    * @param size tamaño de página (default: 10)
    * @param search búsqueda en código de infracción
    * @param status filtro de estado: PENDING, IN_REVIEW, RESOLVED, ESCALATED, ALL
    */
   @GetMapping
   public ResponseEntity<PagedResponse<InfractionResponse>> listAllInfractions(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(required = false) String search,
         @RequestParam(defaultValue = "ALL") String status) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase.listAllInfractions(
            page, size, search, status
      );

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/vehicle/{vehicleId}
    * Lista infracciones por vehículo.
    */
   @GetMapping("/vehicle/{vehicleId}")
   public ResponseEntity<PagedResponse<InfractionResponse>> listInfractionsByVehicle(
         @PathVariable Long vehicleId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase
            .listInfractionsByVehicle(vehicleId, page, size);

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/customer/{customerId}
    * Lista infracciones por cliente.
    */
   @GetMapping("/customer/{customerId}")
   public ResponseEntity<PagedResponse<InfractionResponse>> listInfractionsByCustomer(
         @PathVariable Long customerId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase
            .listInfractionsByCustomer(customerId, page, size);

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/transaction/{transactionId}
    * Lista infracciones por transacción.
    */
   @GetMapping("/transaction/{transactionId}")
   public ResponseEntity<List<InfractionResponse>> listInfractionsByTransaction(
         @PathVariable Long transactionId) {

      List<InfractionDto> dtos = listInfractionsUseCase
            .listInfractionsByTransaction(transactionId);

      List<InfractionResponse> responses = dtos.stream()
            .map(mapper::toResponse)
            .toList();

      return ResponseEntity.ok(responses);
   }

   /**
    * GET /api/v1/infractions/pending-fines
    * Lista infracciones con multas pendientes de pago.
    */
   @GetMapping("/pending-fines")
   public ResponseEntity<PagedResponse<InfractionResponse>> listInfractionsWithPendingFines(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase
            .listInfractionsWithPendingFines(page, size);

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/overdue-fines
    * Lista infracciones con multas vencidas.
    */
   @GetMapping("/overdue-fines")
   public ResponseEntity<PagedResponse<InfractionResponse>> listInfractionsWithOverdueFines(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase
            .listInfractionsWithOverdueFines(page, size);

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/date-range
    * Lista infracciones por rango de fechas.
    *
    * @param startDate fecha inicio (formato ISO: 2026-01-01T00:00:00)
    * @param endDate fecha fin (formato ISO: 2026-01-31T23:59:59)
    */
   @GetMapping("/date-range")
   public ResponseEntity<PagedResponse<InfractionResponse>> listInfractionsByDateRange(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size) {

      PagedResponse<InfractionDto> result = listInfractionsUseCase
            .listInfractionsByDateRange(startDate, endDate, page, size);

      PagedResponse<InfractionResponse> response = result.map(mapper::toResponse);
      return ResponseEntity.ok(response);
   }

   /**
    * GET /api/v1/infractions/pending
    * Lista todas las infracciones pendientes (sin paginación).
    */
   @GetMapping("/pending")
   public ResponseEntity<List<InfractionResponse>> listAllPendingInfractions() {
      List<InfractionDto> dtos = listInfractionsUseCase.listAllPendingInfractions();

      List<InfractionResponse> responses = dtos.stream()
            .map(mapper::toResponse)
            .toList();

      return ResponseEntity.ok(responses);
   }

   // ========================= FINE OPERATIONS =========================

   /**
    * POST /api/v1/infractions/{id}/assign-fine
    * Asigna una multa a la infracción.
    */
   @PostMapping("/{id}/assign-fine")
   public ResponseEntity<InfractionResponse> assignFine(
         @PathVariable Long id,
         @Valid @RequestBody AssignFineRequest request) {

      InfractionDto dto = assignFineUseCase.assignFine(
            id,
            mapper.toAssignFineCommand(request)
      );

      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   /**
    * POST /api/v1/infractions/{id}/record-payment
    * Registra el pago de una multa.
    */
   @PostMapping("/{id}/record-payment")
   public ResponseEntity<InfractionResponse> recordFinePayment(
         @PathVariable Long id,
         @Valid @RequestBody RecordFinePaymentRequest request) {

      InfractionDto dto = recordFinePaymentUseCase.recordFinePayment(
            id,
            mapper.toRecordFinePaymentCommand(request)
      );

      return ResponseEntity.ok(mapper.toResponse(dto));
   }
}