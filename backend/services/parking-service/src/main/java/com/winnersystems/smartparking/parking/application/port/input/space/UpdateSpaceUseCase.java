package com.winnersystems.smartparking.parking.application.port.input.space;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;

/**
 * Puerto de entrada para actualizar un espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface UpdateSpaceUseCase {
   SpaceDto updateSpace(Long spaceId, UpdateSpaceCommand command);
}