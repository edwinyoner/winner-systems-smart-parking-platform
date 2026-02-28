// application/port/input/zone/GetZoneUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;

/**
 * Caso de uso para obtener información de zonas.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface GetZoneUseCase {
   ZoneDto getZoneById(Long zoneId);
}