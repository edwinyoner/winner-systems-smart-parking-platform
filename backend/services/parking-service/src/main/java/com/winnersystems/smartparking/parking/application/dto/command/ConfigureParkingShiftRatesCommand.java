package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command para configurar tarifas por turno en un parqueo.
 * Esta configuración se aplica a TODAS las zonas del parqueo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConfigureParkingShiftRatesCommand {

   private Long parkingId;
   private List<ShiftRateConfig> configurations;

   /**
    * Configuración individual de turno-tarifa.
    */
   @Data
   @Builder
   @NoArgsConstructor
   @AllArgsConstructor
   public static class ShiftRateConfig {
      private Long shiftId;
      private Long rateId;
      private Boolean status;  // true = activo, false = inactivo
   }
}