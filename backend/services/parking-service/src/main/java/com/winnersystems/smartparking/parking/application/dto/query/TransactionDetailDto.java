package com.winnersystems.smartparking.parking.application.dto.query;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO completo con todos los detalles de una transacción.
 * Usado para consultas individuales donde se necesita toda la información.
 *
 * Las clases internas también son records (inmutables, sin setters).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public record TransactionDetailDto(

      // ========================= BÁSICO =========================
      Long id,
      String status,
      String paymentStatus,

      // ========================= ENTIDADES ANIDADAS =========================
      VehicleInfo vehicle,
      CustomerInfo customer,
      ZoneInfo zone,
      SpaceInfo space,

      // ========================= TIEMPOS =========================
      LocalDateTime entryTime,
      LocalDateTime exitTime,
      Integer durationMinutes,
      String durationFormatted,         // "3h 30min"

      // ========================= DOCUMENTOS =========================
      DocumentInfo entryDocument,
      DocumentInfo exitDocument,

      // ========================= MONTOS =========================
      RateInfo rate,
      BigDecimal calculatedAmount,
      BigDecimal discountAmount,
      BigDecimal totalAmount,
      String currency,

      // ========================= PAGO =========================
      PaymentInfo payment,

      // ========================= OPERADORES =========================
      OperatorInfo entryOperator,
      OperatorInfo exitOperator,

      // ========================= EVIDENCIA =========================
      String entryPhotoUrl,
      String exitPhotoUrl,
      Double entryPlateConfidence,
      Double exitPlateConfidence,

      // ========================= COMPROBANTE =========================
      Boolean receiptSent,
      LocalDateTime receiptSentAt,
      String receiptWhatsAppStatus,
      String receiptEmailStatus,

      // ========================= OBSERVACIONES =========================
      String notes,
      String cancellationReason,

      // ========================= AUDITORÍA =========================
      LocalDateTime createdAt,
      LocalDateTime updatedAt

) {

   // ========================= RECORDS INTERNOS =========================

   /**
    * Información del vehículo involucrado en la transacción.
    */
   public record VehicleInfo(
         Long id,
         String plateNumber,
         String type,
         String brand,
         String model,
         String color
   ) {}

   /**
    * Información del cliente/conductor.
    */
   public record CustomerInfo(
         Long id,
         String documentType,
         String documentNumber,
         String name,
         String phone,
         String email
   ) {}

   /**
    * Información de la zona de estacionamiento.
    */
   public record ZoneInfo(
         Long id,
         String name,
         String code
   ) {}

   /**
    * Información del espacio de estacionamiento.
    */
   public record SpaceInfo(
         Long id,
         String code,
         String type
   ) {}

   /**
    * Documento de identificación (entrada o salida).
    */
   public record DocumentInfo(
         String type,
         String number
   ) {}

   /**
    * Información de la tarifa aplicada.
    */
   public record RateInfo(
         Long id,
         String name,
         BigDecimal hourlyRate
   ) {}

   /**
    * Información del pago registrado.
    */
   public record PaymentInfo(
         Long id,
         String paymentType,
         BigDecimal amount,
         String referenceNumber,
         LocalDateTime paymentDate,
         String status
   ) {}

   /**
    * Información del operador (entrada o salida).
    */
   public record OperatorInfo(
         Long id,
         String name,
         String email
   ) {}
}