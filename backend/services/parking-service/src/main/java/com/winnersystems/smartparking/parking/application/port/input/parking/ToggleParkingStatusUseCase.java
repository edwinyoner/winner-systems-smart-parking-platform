package com.winnersystems.smartparking.parking.application.port.input.parking;

import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;

/**
 * Puerto de entrada para gestionar el estado de un Parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ToggleParkingStatusUseCase {
   ParkingDto toggleParkingStatus(Long parkingId);
   ParkingDto activateParking(Long parkingId);
   ParkingDto deactivateParking(Long parkingId);
   ParkingDto setInMaintenance(Long parkingId);
   ParkingDto setOutOfService(Long parkingId);
}