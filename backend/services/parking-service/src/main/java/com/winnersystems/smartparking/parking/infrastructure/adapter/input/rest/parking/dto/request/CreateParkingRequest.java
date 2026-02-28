package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para crear un parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateParkingRequest {

   @NotBlank(message = "El nombre del parking es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @NotBlank(message = "El código del parking es obligatorio")
   @Size(max = 20, message = "El código no puede exceder 20 caracteres")
   private String code;

   @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
   private String description;

   @NotBlank(message = "La dirección es obligatoria")
   @Size(max = 255, message = "La dirección no puede exceder 255 caracteres")
   private String address;

   @DecimalMin(value = "-90.0", message = "Latitud inválida")
   @DecimalMax(value = "90.0", message = "Latitud inválida")
   private Double latitude;

   @DecimalMin(value = "-180.0", message = "Longitud inválida")
   @DecimalMax(value = "180.0", message = "Longitud inválida")
   private Double longitude;

   private Long managerId;
   private String managerName;
}