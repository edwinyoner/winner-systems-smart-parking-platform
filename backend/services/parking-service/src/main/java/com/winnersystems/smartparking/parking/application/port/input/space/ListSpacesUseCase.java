package com.winnersystems.smartparking.parking.application.port.input.space;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;

import java.util.List;

/**
 * Puerto de entrada para listar espacios de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ListSpacesUseCase {
   PagedResponse<SpaceDto> listAllSpaces(int pageNumber, int pageSize, String search, String status);
   List<SpaceDto> listAllActiveSpaces();
   List<SpaceDto> listSpacesByZone(Long zoneId);
}