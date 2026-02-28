package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.response;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Response con información de un turno.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record ShiftResponse(
      Long id,
      String name,
      String code,
      String description,
      LocalTime startTime,
      LocalTime endTime,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}