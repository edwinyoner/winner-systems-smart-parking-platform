package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO para registrar pago de multa.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordFinePaymentRequest {

   @NotNull(message = "amount es requerido")
   @DecimalMin(value = "0.01", message = "amount debe ser mayor a 0")
   private BigDecimal amount;

   private String reference;
}