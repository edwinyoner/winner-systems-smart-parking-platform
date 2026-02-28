package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

/**
 * Request para actualizar un turno.
 * El código NO se actualiza.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShiftRequest {

   @NotBlank(message = "El nombre del turno es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
   private String description;

   @NotNull(message = "La hora de inicio es obligatoria")
   private LocalTime startTime;

   @NotNull(message = "La hora de fin es obligatoria")
   private LocalTime endTime;

   private Boolean status;
}