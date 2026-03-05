package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO para actualizar una infracción existente.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateInfractionRequest {

   private String description;

   private String severity;

   private String evidence;

   private String notes;
}