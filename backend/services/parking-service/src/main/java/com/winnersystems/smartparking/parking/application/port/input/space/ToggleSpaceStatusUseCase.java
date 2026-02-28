package com.winnersystems.smartparking.parking.application.port.input.space;

import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;

/**
 * Puerto de entrada para gestionar el estado de espacios.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ToggleSpaceStatusUseCase {
   SpaceDto toggleSpaceStatus(Long spaceId);
   SpaceDto markAsOccupied(Long spaceId);
   SpaceDto markAsAvailable(Long spaceId);
   SpaceDto setInMaintenance(Long spaceId);
   SpaceDto setOutOfService(Long spaceId);
}