package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;

/**
 * DTO de consulta para Space.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record SpaceDto(
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