// application/port/input/zone/CreateZoneUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

import com.winnersystems.smartparking.parking.application.dto.command.CreateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;

/**
 * Caso de uso para crear una nueva zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface CreateZoneUseCase {
   ZoneDto createZone(CreateZoneCommand command);
}