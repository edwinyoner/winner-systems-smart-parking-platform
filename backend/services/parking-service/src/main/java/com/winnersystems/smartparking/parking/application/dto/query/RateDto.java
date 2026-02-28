package com.winnersystems.smartparking.parking.application.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de consulta para Rate.
 *
 * @author Edwin Yoner - Winner Systems
 */
public record RateDto(
      Long id,
      String name,
      String description,
      BigDecimal amount,
      String currency,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}