package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parkingshiftrate.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request para configurar tarifas por turno en un parqueo.
 * Esta configuración se aplica a TODAS las zonas del parqueo.
 *
 * Ejemplo de uso en el Paso 4 del Stepper:
 * {
 *   "configurations": [
 *     { "shiftId": 1, "rateId": 1, "status": true },  // Mañana + Auto
 *     { "shiftId": 1, "rateId": 2, "status": true },  // Mañana + Moto
 *     { "shiftId": 2, "rateId": 1, "status": true }   // Tarde + Auto
 *   ]
 * }
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureParkingShiftRatesRequest {

   @NotEmpty(message = "Debe incluir al menos una configuración")
   private List<ShiftRateConfigRequest> configurations;

   /**
    * Configuración individual de turno-tarifa.
    */
   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class ShiftRateConfigRequest {

      @NotNull(message = "El ID del turno es obligatorio")
      private Long shiftId;

      @NotNull(message = "El ID de la tarifa es obligatorio")
      private Long rateId;

      /**
       * Estado de la configuración.
       * true = activo, false/null = inactivo
       * Por defecto: true
       */
      private Boolean status;
   }
}