package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command para actualizar un espacio de estacionamiento existente.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSpaceCommand {

   private String type;
   private String description;
   private Double width;
   private Double length;
   private Boolean hasSensor;
   private String sensorId;
   private Boolean hasCameraCoverage;
   private String status;
}