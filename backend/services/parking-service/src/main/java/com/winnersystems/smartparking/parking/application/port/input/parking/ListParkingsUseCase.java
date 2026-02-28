package com.winnersystems.smartparking.parking.application.port.input.parking;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;

import java.util.List;

/**
 * Puerto de entrada para listar Parkings.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ListParkingsUseCase {
   PagedResponse<ParkingDto> listAllParkings(int pageNumber, int pageSize, String search, String status);
   List<ParkingDto> listAllActiveParkings();
}