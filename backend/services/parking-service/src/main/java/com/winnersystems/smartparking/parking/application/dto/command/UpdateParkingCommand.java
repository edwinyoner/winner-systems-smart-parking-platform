package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command para actualizar un Parking existente.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateParkingCommand {

   private String name;
   private String description;
   private String address;
   private Double latitude;
   private Double longitude;
   private Long managerId;
   private String managerName;
   private String status;
}