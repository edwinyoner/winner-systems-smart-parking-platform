// infrastructure/adapter/input/rest/zone/dto/request/CreateZoneRequest.java
package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request para crear una zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateZoneRequest {

   @NotNull(message = "El ID del parqueo es obligatorio")
   private Long parkingId;

   @NotBlank(message = "El nombre de la zona es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @NotBlank(message = "El código de la zona es obligatorio")
   @Size(max = 50, message = "El código no puede exceder 50 caracteres")
   private String code;

   @NotBlank(message = "La dirección es obligatoria")
   @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
   private String address;

   @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
   private String description;

   @NotNull(message = "La capacidad total es obligatoria")
   @Min(value = 1, message = "La capacidad debe ser al menos 1")
   @Max(value = 9999, message = "La capacidad no puede exceder 9999")
   private Integer totalSpaces;

   @DecimalMin(value = "-90.0", message = "Latitud inválida")
   @DecimalMax(value = "90.0", message = "Latitud inválida")
   private Double latitude;

   @DecimalMin(value = "-180.0", message = "Longitud inválida")
   @DecimalMax(value = "180.0", message = "Longitud inválida")
   private Double longitude;

   private Boolean hasCamera;

   private List<String> cameraIds;
}