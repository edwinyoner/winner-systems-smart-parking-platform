package com.winnersystems.smartparking.parking.application.port.input.parking;

import com.winnersystems.smartparking.parking.application.dto.command.CreateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;

/**
 * Puerto de entrada para crear un nuevo Parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface CreateParkingUseCase {
   ParkingDto createParking(CreateParkingCommand command);
}