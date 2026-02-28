package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.response;

import java.time.LocalDateTime;

/**
 * Response con información de un parking.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ParkingResponse(
      Long id,
      String name,
      String code,
      String description,
      String address,
      Double latitude,
      Double longitude,
      Long managerId,
      String managerName,
      Integer totalZones,
      Integer totalSpaces,
      Integer availableSpaces,
      Double occupancyPercentage,
      String status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}