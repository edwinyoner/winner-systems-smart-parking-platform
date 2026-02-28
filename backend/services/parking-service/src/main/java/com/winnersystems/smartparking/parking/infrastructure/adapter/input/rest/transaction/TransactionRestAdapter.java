package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction;

import com.winnersystems.smartparking.parking.application.dto.query.ActiveTransactionDto;
import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDetailDto;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDto;
import com.winnersystems.smartparking.parking.application.port.input.transaction.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.ProcessPaymentRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.RecordEntryRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.RecordExitRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.ActiveTransactionResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.TransactionDetailResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.TransactionResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.mapper.TransactionRestMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * REST Controller para operaciones de transacciones.
 *
 * Endpoints:
 * - POST   /api/v1/transactions/entry          - Registrar entrada
 * - POST   /api/v1/transactions/exit           - Registrar salida
 * - POST   /api/v1/transactions/{id}/payment   - Procesar pago
 * - GET    /api/v1/transactions/{id}           - Consultar por ID
 * - GET    /api/v1/transactions/active         - Listar activas
 * - GET    /api/v1/transactions                - Listar todas
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionRestAdapter {

   private final RecordEntryUseCase recordEntryUseCase;
   private final RecordExitUseCase recordExitUseCase;
   private final ProcessPaymentUseCase processPaymentUseCase;
   private final GetTransactionUseCase getTransactionUseCase;
   private final ListActiveTransactionsUseCase listActiveTransactionsUseCase;
   private final ListTransactionsUseCase listTransactionsUseCase;
   private final TransactionRestMapper mapper;

   // ========================= OPERACIONES DE TRANSACCIÓN =========================

   /**
    * Registra la entrada de un vehículo al estacionamiento.
    *
    * POST /api/v1/transactions/entry
    */
   @PostMapping("/entry")
   public ResponseEntity<TransactionDetailResponse> recordEntry(
         @Valid @RequestBody RecordEntryRequest request) {

      TransactionDetailDto dto = recordEntryUseCase.recordEntry(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toDetailResponse(dto));
   }

   /**
    * Registra la salida de un vehículo del estacionamiento.
    *
    * POST /api/v1/transactions/exit
    */
   @PostMapping("/exit")
   public ResponseEntity<TransactionDetailResponse> recordExit(
         @Valid @RequestBody RecordExitRequest request) {

      TransactionDetailDto dto = recordExitUseCase.recordExit(mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toDetailResponse(dto));
   }

   /**
    * Procesa el pago de una transacción.
    *
    * POST /api/v1/transactions/{id}/payment
    */
   @PostMapping("/{id}/payment")
   public ResponseEntity<TransactionDetailResponse> processPayment(
         @PathVariable Long id,
         @Valid @RequestBody ProcessPaymentRequest request) {

      request.setTransactionId(id);
      TransactionDetailDto dto = processPaymentUseCase.processPayment(mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toDetailResponse(dto));
   }

   // ========================= CONSULTAS =========================

   /**
    * Obtiene el detalle de una transacción por ID.
    *
    * GET /api/v1/transactions/{id}
    */
   @GetMapping("/{id}")
   public ResponseEntity<TransactionDetailResponse> getById(@PathVariable Long id) {
      TransactionDetailDto dto = getTransactionUseCase.getById(id);
      return ResponseEntity.ok(mapper.toDetailResponse(dto));
   }

   /**
    * Busca transacción activa por placa.
    *
    * GET /api/v1/transactions/active/plate/{plateNumber}
    */
   @GetMapping("/active/plate/{plateNumber}")
   public ResponseEntity<TransactionDetailResponse> getActiveByPlate(@PathVariable String plateNumber) {
      TransactionDetailDto dto = getTransactionUseCase.getActiveByPlate(plateNumber);

      if (dto == null) {
         return ResponseEntity.notFound().build();
      }

      return ResponseEntity.ok(mapper.toDetailResponse(dto));
   }

   // ========================= LISTADOS - TRANSACCIONES ACTIVAS =========================

   /**
    * Lista todas las transacciones activas (vehículos dentro).
    *
    * GET /api/v1/transactions/active
    */
   @GetMapping("/active")
   public ResponseEntity<PagedResponse<ActiveTransactionResponse>> listActive(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<ActiveTransactionDto> dtoPage = listActiveTransactionsUseCase.listAll(page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<ActiveTransactionResponse> responsePage = dtoPage.map(mapper::toActiveResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones activas de una zona.
    *
    * GET /api/v1/transactions/active/zone/{zoneId}
    */
   @GetMapping("/active/zone/{zoneId}")
   public ResponseEntity<PagedResponse<ActiveTransactionResponse>> listActiveByZone(
         @PathVariable Long zoneId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<ActiveTransactionDto> dtoPage =
            listActiveTransactionsUseCase.listActiveByZone(zoneId, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<ActiveTransactionResponse> responsePage = dtoPage.map(mapper::toActiveResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Busca transacciones activas por placa (búsqueda parcial).
    *
    * GET /api/v1/transactions/active/search?plateNumber=ABC
    */
   @GetMapping("/active/search")
   public ResponseEntity<PagedResponse<ActiveTransactionResponse>> searchActiveByPlate(
         @RequestParam String plateNumber,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<ActiveTransactionDto> dtoPage =
            listActiveTransactionsUseCase.searchActiveByPlate(plateNumber, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<ActiveTransactionResponse> responsePage = dtoPage.map(mapper::toActiveResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones que exceden tiempo recomendado (alertas).
    *
    * GET /api/v1/transactions/active/overdue
    */
   @GetMapping("/active/overdue")
   public ResponseEntity<PagedResponse<ActiveTransactionResponse>> listOverdue(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<ActiveTransactionDto> dtoPage =
            listActiveTransactionsUseCase.listOverdue(page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<ActiveTransactionResponse> responsePage = dtoPage.map(mapper::toActiveResponse);

      return ResponseEntity.ok(responsePage);
   }

   // ========================= LISTADOS - HISTORIAL COMPLETO =========================

   /**
    * Lista todas las transacciones con paginación y ordenamiento.
    *
    * GET /api/v1/transactions
    */
   @GetMapping
   public ResponseEntity<PagedResponse<TransactionResponse>> listAll(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size,
         @RequestParam(defaultValue = "createdAt") String sortBy,
         @RequestParam(defaultValue = "DESC") String sortDirection) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.listAll(page, size, sortBy, sortDirection);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones por rango de fechas.
    *
    * GET /api/v1/transactions/date-range?startDate=...&endDate=...
    */
   @GetMapping("/date-range")
   public ResponseEntity<PagedResponse<TransactionResponse>> listByDateRange(
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.listByDateRange(startDate, endDate, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones por estado.
    *
    * GET /api/v1/transactions/status/{status}
    */
   @GetMapping("/status/{status}")
   public ResponseEntity<PagedResponse<TransactionResponse>> listByStatus(
         @PathVariable String status,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.listByStatus(status, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones por estado de pago.
    *
    * GET /api/v1/transactions/payment-status/{paymentStatus}
    */
   @GetMapping("/payment-status/{paymentStatus}")
   public ResponseEntity<PagedResponse<TransactionResponse>> listByPaymentStatus(
         @PathVariable String paymentStatus,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.listByPaymentStatus(paymentStatus, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Lista transacciones de una zona (histórico).
    *
    * GET /api/v1/transactions/zone/{zoneId}
    */
   @GetMapping("/zone/{zoneId}")
   public ResponseEntity<PagedResponse<TransactionResponse>> listByZone(
         @PathVariable Long zoneId,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.listByZone(zoneId, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }

   /**
    * Busca transacciones por placa (histórico).
    *
    * GET /api/v1/transactions/search?plateNumber=ABC
    */
   @GetMapping("/search")
   public ResponseEntity<PagedResponse<TransactionResponse>> searchByPlate(
         @RequestParam String plateNumber,
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "20") int size) {

      PagedResponse<TransactionDto> dtoPage =
            listTransactionsUseCase.searchByPlate(plateNumber, page, size);

      // ✅ Usar .map() para convertir DTOs a Responses
      PagedResponse<TransactionResponse> responsePage = dtoPage.map(mapper::toResponse);

      return ResponseEntity.ok(responsePage);
   }
}