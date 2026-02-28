package com.winnersystems.smartparking.parking.application.port.input.rate;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;

import java.util.List;

public interface ListRatesUseCase {
   PagedResponse<RateDto> listAllRates(int page, int size, String search, Boolean status);
   List<RateDto> listAllActiveRates();
}