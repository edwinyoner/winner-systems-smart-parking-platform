// application/dto/command/UpdateRateCommand.java
package com.winnersystems.smartparking.parking.application.dto.command;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRateCommand {

   private String name;
   private String description;
   private BigDecimal amount;
   private String currency;
   private Boolean status;
}