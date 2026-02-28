package com.winnersystems.smartparking.parking.application.port.input.rate;

import com.winnersystems.smartparking.parking.application.dto.query.RateDto;

public interface GetRateUseCase {
   RateDto getRateById(Long id);
}