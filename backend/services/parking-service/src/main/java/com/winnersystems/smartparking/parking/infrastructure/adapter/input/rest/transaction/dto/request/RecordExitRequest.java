package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO para registrar salida de vehículo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecordExitRequest {

   // Identificar transacción por ID o por placa (uno u otro)
   private Long transactionId;

   @Size(min = 5, max = 20, message = "La placa debe tener entre 5 y 20 caracteres")
   @Pattern(regexp = "^[A-Z0-9-]+$", message = "La placa solo puede contener letras mayúsculas, números y guiones")
   private String plateNumber;

   @NotNull(message = "El tipo de documento de salida es obligatorio")
   private Long exitDocumentTypeId;

   @NotBlank(message = "El número de documento de salida es obligatorio")
   @Size(min = 8, max = 20, message = "El documento debe tener entre 8 y 20 caracteres")
   private String exitDocumentNumber;

   @NotNull(message = "El operador es obligatorio")
   private Long operatorId;

   // Campos opcionales de evidencia
   private String exitMethod;       // MANUAL, CAMERA_AI, SENSOR
   private String photoUrl;
   private Double plateConfidence;

   private String notes;
}