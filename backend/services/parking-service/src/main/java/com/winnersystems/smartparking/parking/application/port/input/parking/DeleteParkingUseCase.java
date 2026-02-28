package com.winnersystems.smartparking.parking.application.port.input.parking;

/**
 * Puerto de entrada para eliminar un Parking (soft delete).
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface DeleteParkingUseCase {
   void deleteParking(Long parkingId);
}