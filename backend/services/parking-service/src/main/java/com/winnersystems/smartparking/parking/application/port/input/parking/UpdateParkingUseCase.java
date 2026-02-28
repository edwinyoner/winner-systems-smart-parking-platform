package com.winnersystems.smartparking.parking.application.port.input.parking;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;

/**
 * Puerto de entrada para actualizar un Parking existente.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface UpdateParkingUseCase {
   ParkingDto updateParking(Long parkingId, UpdateParkingCommand command);
}