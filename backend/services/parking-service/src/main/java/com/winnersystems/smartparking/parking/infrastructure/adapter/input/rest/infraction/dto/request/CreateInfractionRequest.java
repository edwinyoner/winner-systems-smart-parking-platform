package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO para crear una nueva infracción.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateInfractionRequest {

   @NotNull(message = "parkingId es requerido")
   private Long parkingId;

   @NotNull(message = "zoneId es requerido")
   private Long zoneId;

   private Long spaceId;

   private Long transactionId;

   @NotNull(message = "vehicleId es requerido")
   private Long vehicleId;

   private Long customerId;

   @NotBlank(message = "infractionType es requerido")
   private String infractionType;

   private String severity;

   private String description;

   private String evidence;

   private String notes;
}