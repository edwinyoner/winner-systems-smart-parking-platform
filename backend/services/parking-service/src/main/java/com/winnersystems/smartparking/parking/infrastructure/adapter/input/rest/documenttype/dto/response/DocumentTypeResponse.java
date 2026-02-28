package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.documenttype.dto.response;

import java.time.LocalDateTime;

public record DocumentTypeResponse(
      Long id,
      String code,
      String name,
      String description,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}