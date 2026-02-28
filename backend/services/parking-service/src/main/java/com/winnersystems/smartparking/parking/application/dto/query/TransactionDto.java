package com.winnersystems.smartparking.parking.application.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO simplificado para listar transacciones.
 * Usado en listados y búsquedas donde no se necesita todo el detalle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record TransactionDto(
      Long id,
      String plateNumber,
      String customerName,
      String zoneName,
      String spaceCode,
      LocalDateTime entryTime,
      LocalDateTime exitTime,
      String duration,          // Formato: "3h 30min"
      BigDecimal totalAmount,
      String status,            // ACTIVE, COMPLETED, CANCELLED
      String paymentStatus,     // PENDING, PAID, OVERDUE
      LocalDateTime createdAt
) {}