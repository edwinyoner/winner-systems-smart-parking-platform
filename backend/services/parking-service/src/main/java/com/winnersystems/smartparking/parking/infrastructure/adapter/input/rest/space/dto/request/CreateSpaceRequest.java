package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para crear un espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpaceRequest {

   @NotNull(message = "El ID de la zona es obligatorio")
   private Long zoneId;

   @NotBlank(message = "El tipo de espacio es obligatorio")
   @Pattern(regexp = "PARALLEL|DIAGONAL|PERPENDICULAR",
         message = "Tipo inválido. Debe ser: PARALLEL, DIAGONAL, PERPENDICULAR")
   private String type;

   @NotBlank(message = "El código del espacio es obligatorio")
   @Size(max = 20, message = "El código no puede exceder 20 caracteres")
   private String code;

   @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
   private String description;

   @DecimalMin(value = "1.0", message = "El ancho debe ser al menos 1.0 metros")
   @DecimalMax(value = "10.0", message = "El ancho no puede exceder 10.0 metros")
   private Double width;

   @DecimalMin(value = "1.0", message = "El largo debe ser al menos 1.0 metros")
   @DecimalMax(value = "15.0", message = "El largo no puede exceder 15.0 metros")
   private Double length;

   private Boolean hasSensor;

   @Size(max = 50, message = "El ID del sensor no puede exceder 50 caracteres")
   private String sensorId;

   private Boolean hasCameraCoverage;

   private Boolean status;
}