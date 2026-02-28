package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Command para crear un nuevo Parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateParkingCommand {
   private String name;
   private String code;
   private String description;
   private String address;
   private Double latitude;
   private Double longitude;
   private Long managerId;
   private String managerName;
}