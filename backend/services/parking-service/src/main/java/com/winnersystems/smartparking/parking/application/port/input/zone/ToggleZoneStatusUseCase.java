// application/port/input/zone/ToggleZoneStatusUseCase.java
package com.winnersystems.smartparking.parking.application.port.input.zone;

import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;

/**
 * Caso de uso para activar/desactivar zonas.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ToggleZoneStatusUseCase {
   ZoneDto toggleZoneStatus(Long zoneId);
   ZoneDto activateZone(Long zoneId);
   ZoneDto deactivateZone(Long zoneId);
   ZoneDto setInMaintenance(Long zoneId);
   ZoneDto setOutOfService(Long zoneId);
}