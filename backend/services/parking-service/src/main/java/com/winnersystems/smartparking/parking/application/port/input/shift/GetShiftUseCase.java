package com.winnersystems.smartparking.parking.application.port.input.shift;

import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;

public interface GetShiftUseCase {
   ShiftDto getShiftById(Long id);
}