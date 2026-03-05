package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Request DTO para asignar multa a una infracción.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignFineRequest {

   @NotNull(message = "fineAmount es requerido")
   @DecimalMin(value = "0.01", message = "fineAmount debe ser mayor a 0")
   private BigDecimal fineAmount;

   private LocalDateTime fineDueDate;
}