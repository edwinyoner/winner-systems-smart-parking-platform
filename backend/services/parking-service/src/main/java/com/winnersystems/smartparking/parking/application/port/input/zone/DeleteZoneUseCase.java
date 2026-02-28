// application/port/input/zone/DeleteZoneUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

/**
 * Caso de uso para eliminar una zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface DeleteZoneUseCase {
   void deleteZone(Long zoneId);
}