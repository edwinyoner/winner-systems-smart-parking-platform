// application/dto/command/UpdateZoneCommand.java
package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command para actualizar una zona de estacionamiento existente.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateZoneCommand {

   private String name;
   private String address;
   private String description;
   private Integer totalSpaces;
   private Double latitude;
   private Double longitude;
   private Boolean hasCamera;
   private List<String> cameraIds;
   private String status;
}