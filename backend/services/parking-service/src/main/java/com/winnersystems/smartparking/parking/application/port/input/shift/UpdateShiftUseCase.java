package com.winnersystems.smartparking.parking.application.port.input.shift;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;

public interface UpdateShiftUseCase {
   ShiftDto updateShift(Long shiftId, UpdateShiftCommand command);
}