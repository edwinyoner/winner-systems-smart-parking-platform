package com.winnersystems.smartparking.parking.application.port.input.rate;

import com.winnersystems.smartparking.parking.application.dto.query.RateDto;

public interface ToggleRateStatusUseCase {
   RateDto toggleRateStatus(Long id);
   RateDto activateRate(Long id);
   RateDto deactivateRate(Long id);
}