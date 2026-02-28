// application/port/input/rate/CreateRateUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.rate;

import com.winnersystems.smartparking.parking.application.dto.command.CreateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;

public interface CreateRateUseCase {
   RateDto createRate(CreateRateCommand command);
}