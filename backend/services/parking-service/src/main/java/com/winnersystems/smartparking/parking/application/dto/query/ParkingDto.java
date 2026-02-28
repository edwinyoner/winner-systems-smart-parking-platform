package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;

/**
 * DTO de consulta para Parking.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record ParkingDto(
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