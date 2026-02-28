package com.winnersystems.smartparking.parking.application.port.input.shift;

import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;

public interface ToggleShiftStatusUseCase {
   ShiftDto toggleShiftStatus(Long id);
   ShiftDto activateShift(Long id);
   ShiftDto deactivateShift(Long id);
}