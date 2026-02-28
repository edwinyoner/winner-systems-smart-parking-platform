package com.winnersystems.smartparking.parking.application.dto.query;

import java.time.LocalDateTime;

public record PaymentTypeDto(
      Long id,
      String code,
      String name,
      String description,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}