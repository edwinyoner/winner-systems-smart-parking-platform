package com.winnersystems.smartparking.parking.domain.exception;

/**
 * Excepción lanzada cuando se intenta asignar un espacio que no está disponible.
 *
 * Escenarios:
 * - Espacio ya está OCCUPIED por otro vehículo
 * - Espacio en MAINTENANCE (mantenimiento)
 * - Espacio INACTIVE (deshabilitado)
 *
 * Esta excepción protege la integridad de las asignaciones de espacios.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public class SpaceNotAvailableException extends ParkingDomainException {

   private final String spaceCode;
   private final String currentStatus;

   public SpaceNotAvailableException(String spaceCode, String currentStatus) {
      super(String.format(
            "El espacio '%s' no está disponible. Estado actual: %s",
            spaceCode, currentStatus
      ));
      this.spaceCode = spaceCode;
      this.currentStatus = currentStatus;
   }

   public String getSpaceCode() {
      return spaceCode;
   }

   public String getCurrentStatus() {
      return currentStatus;
   }
}