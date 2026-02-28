package com.winnersystems.smartparking.parking.application.port.input.space;

import com.winnersystems.smartparking.parking.application.dto.command.CreateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;

/**
 * Puerto de entrada para crear un espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface CreateSpaceUseCase {
   SpaceDto createSpace(CreateSpaceCommand command);
}