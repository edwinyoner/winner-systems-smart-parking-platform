package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Response con información de una zona de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ZoneResponse(
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
) {}