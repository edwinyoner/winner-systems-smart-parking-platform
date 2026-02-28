// infrastructure/adapter/input/rest/zone/dto/request/UpdateZoneRequest.java
package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Request para actualizar una zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateZoneRequest {

   @NotBlank(message = "El nombre de la zona es obligatorio")
   @Size(max = 100, message = "El nombre no puede exceder 100 caracteres")
   private String name;

   @NotBlank(message = "La dirección es obligatoria")
   @Size(max = 200, message = "La dirección no puede exceder 200 caracteres")
   private String address;

   private String description;

   @Min(value = 1, message = "La capacidad debe ser al menos 1")
   private Integer totalSpaces;

   @DecimalMin(value = "-90.0", message = "Latitud inválida")
   @DecimalMax(value = "90.0", message = "Latitud inválida")
   private Double latitude;

   @DecimalMin(value = "-180.0", message = "Longitud inválida")
   @DecimalMax(value = "180.0", message = "Longitud inválida")
   private Double longitude;

   private Boolean hasCamera;

   private List<String> cameraIds;

   private String status;
}