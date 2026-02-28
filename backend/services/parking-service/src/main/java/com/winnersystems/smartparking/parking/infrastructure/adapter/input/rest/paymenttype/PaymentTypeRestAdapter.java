package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;
import com.winnersystems.smartparking.parking.application.port.input.paymenttype.*;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.request.CreatePaymentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.request.UpdatePaymentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.response.PaymentTypeResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.mapper.PaymentTypeRestMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payment-types")
public class PaymentTypeRestAdapter {

   private final CreatePaymentTypeUseCase createUseCase;
   private final UpdatePaymentTypeUseCase updateUseCase;
   private final DeletePaymentTypeUseCase deleteUseCase;
   private final GetPaymentTypeUseCase getUseCase;
   private final ListPaymentTypesUseCase listUseCase;
   private final TogglePaymentTypeStatusUseCase toggleUseCase;
   private final PaymentTypeRestMapper mapper;

   public PaymentTypeRestAdapter(
         CreatePaymentTypeUseCase createUseCase,
         UpdatePaymentTypeUseCase updateUseCase,
         DeletePaymentTypeUseCase deleteUseCase,
         GetPaymentTypeUseCase getUseCase,
         ListPaymentTypesUseCase listUseCase,
         TogglePaymentTypeStatusUseCase toggleUseCase,
         PaymentTypeRestMapper mapper) {
      this.createUseCase = createUseCase;
      this.updateUseCase = updateUseCase;
      this.deleteUseCase = deleteUseCase;
      this.getUseCase = getUseCase;
      this.listUseCase = listUseCase;
      this.toggleUseCase = toggleUseCase;
      this.mapper = mapper;
   }

   // POST /api/v1/payment-types
   @PostMapping
   public ResponseEntity<PaymentTypeResponse> create(
         @Valid @RequestBody CreatePaymentTypeRequest request) {
      PaymentTypeDto dto = createUseCase.createPaymentType(mapper.toCommand(request));
      return ResponseEntity.status(HttpStatus.CREATED).body(mapper.toResponse(dto));
   }

   // PUT /api/v1/payment-types/{id}
   @PutMapping("/{id}")
   public ResponseEntity<PaymentTypeResponse> update(
         @PathVariable Long id,
         @Valid @RequestBody UpdatePaymentTypeRequest request) {
      PaymentTypeDto dto = updateUseCase.updatePaymentType(id, mapper.toCommand(request));
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // DELETE /api/v1/payment-types/{id}
   @DeleteMapping("/{id}")
   public ResponseEntity<Void> delete(@PathVariable Long id) {
      deleteUseCase.deletePaymentType(id);
      return ResponseEntity.noContent().build();
   }

   // GET /api/v1/payment-types/{id}
   @GetMapping("/{id}")
   public ResponseEntity<PaymentTypeResponse> getById(@PathVariable Long id) {
      PaymentTypeDto dto = getUseCase.getPaymentTypeById(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // GET /api/v1/payment-types?page=0&size=10&search=cash&status=true
   @GetMapping
   public ResponseEntity<PagedResponse<PaymentTypeResponse>> list(
         @RequestParam(defaultValue = "0") int page,
         @RequestParam(defaultValue = "10") int size,
         @RequestParam(required = false) String search,
         @RequestParam(required = false) Boolean status) {
      PagedResponse<PaymentTypeDto> result = listUseCase.listAllPaymentTypes(page, size, search, status);
      return ResponseEntity.ok(result.map(mapper::toResponse));
   }

   // GET /api/v1/payment-types/active
   @GetMapping("/active")
   public ResponseEntity<List<PaymentTypeResponse>> listActive() {
      List<PaymentTypeResponse> response = listUseCase.listAllActivePaymentTypes().stream()
            .map(mapper::toResponse)
            .toList();
      return ResponseEntity.ok(response);
   }

   // PATCH /api/v1/payment-types/{id}/toggle-status
   @PatchMapping("/{id}/toggle-status")
   public ResponseEntity<PaymentTypeResponse> toggleStatus(@PathVariable Long id) {
      PaymentTypeDto dto = toggleUseCase.togglePaymentTypeStatus(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // PATCH /api/v1/payment-types/{id}/activate
   @PatchMapping("/{id}/activate")
   public ResponseEntity<PaymentTypeResponse> activate(@PathVariable Long id) {
      PaymentTypeDto dto = toggleUseCase.activatePaymentTypeDto(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }

   // PATCH /api/v1/payment-types/{id}/deactivate
   @PatchMapping("/{id}/deactivate")
   public ResponseEntity<PaymentTypeResponse> deactivate(@PathVariable Long id) {
      PaymentTypeDto dto = toggleUseCase.deactivatePaymentTypeDto(id);
      return ResponseEntity.ok(mapper.toResponse(dto));
   }
}