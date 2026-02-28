package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * DTO de consulta para Shift.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record ShiftDto(
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