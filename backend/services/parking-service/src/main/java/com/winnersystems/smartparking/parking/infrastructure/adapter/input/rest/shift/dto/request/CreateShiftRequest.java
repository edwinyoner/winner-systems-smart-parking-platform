// parking-service/.../shift/dto/request/CreateShiftRequest.java
package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateShiftRequest {

   @NotBlank(message = "El nombre del turno es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @NotBlank(message = "El código del turno es obligatorio")
   @Size(max = 50, message = "El código no puede exceder 50 caracteres")
   @Pattern(regexp = "^[A-Z_]+$", message = "El código solo puede contener mayúsculas y guiones bajos")
   private String code;

   @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
   private String description;

   @NotNull(message = "La hora de inicio es obligatoria")
   private LocalTime startTime;

   @NotNull(message = "La hora de fin es obligatoria")
   private LocalTime endTime;

   private Boolean status; // Por defecto true en el backend
}