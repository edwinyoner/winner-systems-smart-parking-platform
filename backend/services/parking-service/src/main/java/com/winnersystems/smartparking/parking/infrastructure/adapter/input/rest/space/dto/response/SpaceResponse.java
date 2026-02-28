package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.response;

import java.time.LocalDateTime;

/**
 * Response con información de un espacio de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record SpaceResponse(
      Long id,
      Long zoneId,
      String type,
      String code,
      String description,
      Double width,
      Double length,
      Boolean hasSensor,
      String sensorId,
      Boolean hasCameraCoverage,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}