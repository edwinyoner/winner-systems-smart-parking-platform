package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO para procesar pago de transacción.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {

   // Se setea automáticamente desde el path /{id}/payment
   private Long transactionId;

   @NotNull(message = "El tipo de pago es obligatorio")
   private Long paymentTypeId;

   @NotNull(message = "El monto pagado es obligatorio")
   @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
   private BigDecimal amountPaid;

   @NotNull(message = "El operador es obligatorio")
   private Long operatorId;

   // Campos opcionales
   private String referenceNumber;
   private Boolean sendReceipt;
   private String notes;
}