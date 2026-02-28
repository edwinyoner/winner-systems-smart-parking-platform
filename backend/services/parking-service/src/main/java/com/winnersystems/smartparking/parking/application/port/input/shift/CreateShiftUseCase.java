// application/port/input/shift/CreateShiftUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.shift;

import com.winnersystems.smartparking.parking.application.dto.command.CreateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;

public interface CreateShiftUseCase {
   ShiftDto createShift(CreateShiftCommand command);
}