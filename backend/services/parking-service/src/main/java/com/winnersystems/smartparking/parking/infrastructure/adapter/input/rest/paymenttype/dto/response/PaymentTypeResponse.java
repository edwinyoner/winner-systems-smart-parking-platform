package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.response;

import java.time.LocalDateTime;

public record PaymentTypeResponse(
      Long id,
      String code,
      String name,
      String description,
      Boolean status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt
) {}