package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command para crear un nuevo espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpaceCommand {
   private Long zoneId;
   private String type;
   private String code;
   private String description;
   private Double width;
   private Double length;
   private Boolean hasSensor;
   private String sensorId;
   private Boolean hasCameraCoverage;
}