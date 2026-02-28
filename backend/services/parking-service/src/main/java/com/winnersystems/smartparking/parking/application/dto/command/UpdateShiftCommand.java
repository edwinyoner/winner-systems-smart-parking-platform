// application/dto/command/UpdateShiftCommand.java
package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShiftCommand {

   private String name;
   private String code;
   private String description;
   private LocalTime startTime;
   private LocalTime endTime;
   private Boolean status;
}