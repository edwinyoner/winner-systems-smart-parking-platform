package com.winnersystems.smartparking.parking.application.port.input.shift;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;

import java.util.List;

public interface ListShiftsUseCase {
   PagedResponse<ShiftDto> listAllShifts(int page, int size, String search, Boolean status);
   List<ShiftDto> listAllActiveShifts();
}