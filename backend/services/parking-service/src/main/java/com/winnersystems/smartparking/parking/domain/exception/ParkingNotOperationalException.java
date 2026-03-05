package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción lanzada cuando se intenta usar un parking que no está operativo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class ParkingNotOperationalException extends RuntimeException {

   private final String parkingName;
   private final String currentStatus;

   public ParkingNotOperationalException(String parkingName, String currentStatus) {
      super(String.format("El parking '%s' no está operativo. Estado actual: %s",
            parkingName, currentStatus));
      this.parkingName = parkingName;
      this.currentStatus = currentStatus;
   }

   public String getParkingName() {
      return parkingName;
   }

   public String getCurrentStatus() {
      return currentStatus;
   }
}