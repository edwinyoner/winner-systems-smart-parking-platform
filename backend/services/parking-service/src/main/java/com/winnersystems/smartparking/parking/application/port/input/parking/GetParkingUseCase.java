package com.winnersystems.smartparking.parking.application.port.input.parking;

import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;

/**
 * Puerto de entrada para obtener un Parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface GetParkingUseCase {
   ParkingDto getParkingById(Long parkingId);
}