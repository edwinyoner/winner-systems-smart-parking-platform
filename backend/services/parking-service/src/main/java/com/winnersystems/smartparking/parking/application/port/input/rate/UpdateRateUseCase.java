package com.winnersystems.smartparking.parking.application.port.input.rate;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;

public interface UpdateRateUseCase {
   RateDto updateRate(Long rateId, UpdateRateCommand command);
}