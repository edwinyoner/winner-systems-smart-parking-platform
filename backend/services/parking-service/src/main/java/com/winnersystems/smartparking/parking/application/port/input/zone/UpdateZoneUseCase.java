// application/port/input/zone/UpdateZoneUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;

/**
 * Caso de uso para actualizar una zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface UpdateZoneUseCase {
   ZoneDto updateZone(Long zoneId, UpdateZoneCommand command);
}