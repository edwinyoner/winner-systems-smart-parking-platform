package com.winnersystems.smartparking.parking.application.port.input.space;

import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;

/**
 * Puerto de entrada para obtener espacios de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface GetSpaceUseCase {
   SpaceDto getSpaceById(Long spaceId);
}