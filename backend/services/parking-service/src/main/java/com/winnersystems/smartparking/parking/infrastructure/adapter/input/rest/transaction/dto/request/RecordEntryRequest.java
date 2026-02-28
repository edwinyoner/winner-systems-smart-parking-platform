package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO para registrar entrada de vehículo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordEntryRequest {

   @NotNull(message = "La zona es obligatoria")
   private Long zoneId;

   @NotNull(message = "El espacio es obligatorio")
   private Long spaceId;

   @NotBlank(message = "La placa es obligatoria")
   @Size(min = 5, max = 20, message = "La placa debe tener entre 5 y 20 caracteres")
   @Pattern(regexp = "^[A-Z0-9-]+$", message = "La placa solo puede contener letras mayúsculas, números y guiones")
   private String plateNumber;

   @NotNull(message = "El tipo de documento es obligatorio")
   private Long documentTypeId;

   @NotBlank(message = "El número de documento es obligatorio")
   @Size(min = 8, max = 20, message = "El documento debe tener entre 8 y 20 caracteres")
   private String documentNumber;

   // Campos opcionales del cliente
   private String customerName;

   @Email(message = "El email debe ser válido")
   private String customerEmail;

   @Pattern(regexp = "^[0-9+\\-\\s()]+$", message = "El teléfono solo puede contener números, espacios y símbolos +-()")
   private String customerPhone;

   @NotNull(message = "El operador es obligatorio")
   private Long operatorId;

   // Campos opcionales de evidencia
   private String entryMethod;      // MANUAL, CAMERA_AI, SENSOR
   private String photoUrl;
   private Double plateConfidence;

   private String notes;
}