// application/dto/command/CreateZoneCommand.java
package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Command para crear una nueva zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateZoneCommand {
   private Long parkingId;
   private String name;
   private String code;
   private String address;
   private String description;
   private Integer totalSpaces;
   private Double latitude;
   private Double longitude;
   private Boolean hasCamera;
   private List<String> cameraIds;
}