package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response con información de una tarifa.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record RateResponse(
      Long id,
      String name,
      String description,
      BigDecimal amount,
      String currency,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}