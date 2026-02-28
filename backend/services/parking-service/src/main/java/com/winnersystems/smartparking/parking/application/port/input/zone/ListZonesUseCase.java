// application/port/input/zone/ListZonesUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

import com.winnersystems.smartparking.parking.application.dto.query.PagedResponse;
import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;

import java.util.List;

/**
 * Caso de uso para listar zonas de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ListZonesUseCase {
   PagedResponse<ZoneDto> listAllZones(int pageNumber, int pageSize, String search, String status);
   List<ZoneDto> listAllActiveZones();
   List<ZoneDto> listZonesByParking(Long parkingId);
}