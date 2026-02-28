package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO de consulta para Zone.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record ZoneDto(
      Long id,
      Long parkingId,
      String name,
      String code,
      String address,
      String description,
      Integer totalSpaces,
      Integer availableSpaces,
      Double occupancyPercentage,
      Double latitude,
      Double longitude,
      Boolean hasCamera,
      List<String> cameraIds,
      Integer cameraCount,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {
}