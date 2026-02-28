// parking-service/.../shift/dto/request/UpdateRateRequest.java
package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRateRequest {

   @NotBlank(message = "El nombre de la tarifa es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   private String description;

   @NotNull(message = "El monto es obligatorio")
   @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
   @Digits(integer = 8, fraction = 2, message = "El monto debe tener máximo 8 dígitos enteros y 2 decimales")
   private BigDecimal amount;

   @Size(max = 3, message = "La moneda debe ser un código de 3 caracteres")
   private String currency;

   private Boolean status;
}