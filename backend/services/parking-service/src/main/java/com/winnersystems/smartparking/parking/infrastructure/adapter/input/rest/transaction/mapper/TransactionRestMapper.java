package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.ProcessPaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.command.RecordEntryCommand;
import com.winnersystems.smartparking.parking.application.dto.command.RecordExitCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ActiveTransactionDto;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDetailDto;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.ProcessPaymentRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.RecordEntryRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.request.RecordExitRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.ActiveTransactionResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.TransactionDetailResponse;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.transaction.dto.response.TransactionResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre Request/Response y Commands/DTOs.
 *
 * Nota: TransactionDto, TransactionDetailDto y ActiveTransactionDto son records.
 * Los records usan accessors sin prefijo "get" → dto.id(), dto.status(), etc.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class TransactionRestMapper {

   // ========================= REQUEST → COMMAND =========================

   public RecordEntryCommand toCommand(RecordEntryRequest request) {
      if (request == null) return null;

      return RecordEntryCommand.builder()
            .zoneId(request.getZoneId())
            .spaceId(request.getSpaceId())
            .plateNumber(request.getPlateNumber())
            .documentTypeId(request.getDocumentTypeId())
            .documentNumber(request.getDocumentNumber())
            .customerName(request.getCustomerName())
            .customerEmail(request.getCustomerEmail())
            .customerPhone(request.getCustomerPhone())
            .operatorId(request.getOperatorId())
            .entryMethod(request.getEntryMethod())
            .photoUrl(request.getPhotoUrl())
            .plateConfidence(request.getPlateConfidence())
            .notes(request.getNotes())
            .build();
   }

   public RecordExitCommand toCommand(RecordExitRequest request) {
      if (request == null) return null;

      return RecordExitCommand.builder()
            .transactionId(request.getTransactionId())
            .plateNumber(request.getPlateNumber())
            .exitDocumentTypeId(request.getExitDocumentTypeId())
            .exitDocumentNumber(request.getExitDocumentNumber())
            .operatorId(request.getOperatorId())
            .exitMethod(request.getExitMethod())
            .photoUrl(request.getPhotoUrl())
            .plateConfidence(request.getPlateConfidence())
            .notes(request.getNotes())
            .build();
   }

   public ProcessPaymentCommand toCommand(ProcessPaymentRequest request) {
      if (request == null) return null;

      return ProcessPaymentCommand.builder()
            .transactionId(request.getTransactionId())
            .paymentTypeId(request.getPaymentTypeId())
            .amountPaid(request.getAmountPaid())
            .operatorId(request.getOperatorId())
            .referenceNumber(request.getReferenceNumber())
            .notes(request.getNotes())
            .sendReceipt(request.getSendReceipt())
            .build();
   }

   // ========================= DTO → RESPONSE =========================

   /**
    * TransactionDto es record → usar dto.campo() sin "get"
    */
   public TransactionResponse toResponse(TransactionDto dto) {
      if (dto == null) return null;

      return TransactionResponse.builder()
            .id(dto.id())
            .plateNumber(dto.plateNumber())
            .customerName(dto.customerName())
            .zoneName(dto.zoneName())
            .spaceCode(dto.spaceCode())
            .entryTime(dto.entryTime())
            .exitTime(dto.exitTime())
            .duration(dto.duration())
            .totalAmount(dto.totalAmount())
            .status(dto.status())
            .paymentStatus(dto.paymentStatus())
            .createdAt(dto.createdAt())
            .build();
   }

   /**
    * TransactionDetailDto es record con inner records → dto.campo(), dto.vehicle().id(), etc.
    */
   public TransactionDetailResponse toDetailResponse(TransactionDetailDto dto) {
      if (dto == null) return null;

      TransactionDetailResponse response = TransactionDetailResponse.builder()
            .id(dto.id())
            .status(dto.status())
            .paymentStatus(dto.paymentStatus())
            .entryTime(dto.entryTime())
            .exitTime(dto.exitTime())
            .durationMinutes(dto.durationMinutes())
            .durationFormatted(dto.durationFormatted())
            .calculatedAmount(dto.calculatedAmount())
            .discountAmount(dto.discountAmount())
            .totalAmount(dto.totalAmount())
            .currency(dto.currency())
            .entryPhotoUrl(dto.entryPhotoUrl())
            .exitPhotoUrl(dto.exitPhotoUrl())
            .entryPlateConfidence(dto.entryPlateConfidence())
            .exitPlateConfidence(dto.exitPlateConfidence())
            .receiptSent(dto.receiptSent())
            .receiptSentAt(dto.receiptSentAt())
            .receiptWhatsAppStatus(dto.receiptWhatsAppStatus())
            .receiptEmailStatus(dto.receiptEmailStatus())
            .notes(dto.notes())
            .cancellationReason(dto.cancellationReason())
            .createdAt(dto.createdAt())
            .updatedAt(dto.updatedAt())
            .build();

      // Vehículo — inner record: dto.vehicle().id()
      if (dto.vehicle() != null) {
         response.setVehicle(TransactionDetailResponse.VehicleInfo.builder()
               .id(dto.vehicle().id())
               .plateNumber(dto.vehicle().plateNumber())
               .brand(dto.vehicle().brand())
               .color(dto.vehicle().color())
               .build());
      }

      // Cliente
      if (dto.customer() != null) {
         response.setCustomer(TransactionDetailResponse.CustomerInfo.builder()
               .id(dto.customer().id())
               .documentNumber(dto.customer().documentNumber())
               .name(dto.customer().name())
               .phone(dto.customer().phone())
               .email(dto.customer().email())
               .build());
      }

      // Zona
      if (dto.zone() != null) {
         response.setZone(TransactionDetailResponse.ZoneInfo.builder()
               .id(dto.zone().id())
               .name(dto.zone().name())
               .code(dto.zone().code())
               .build());
      }

      // Espacio
      if (dto.space() != null) {
         response.setSpace(TransactionDetailResponse.SpaceInfo.builder()
               .id(dto.space().id())
               .code(dto.space().code())
               .build());
      }

      // Documento entrada
      if (dto.entryDocument() != null) {
         response.setEntryDocument(TransactionDetailResponse.DocumentInfo.builder()
               .number(dto.entryDocument().number())
               .build());
      }

      // Documento salida
      if (dto.exitDocument() != null) {
         response.setExitDocument(TransactionDetailResponse.DocumentInfo.builder()
               .number(dto.exitDocument().number())
               .build());
      }

      // Tarifa
      if (dto.rate() != null) {
         response.setRate(TransactionDetailResponse.RateInfo.builder()
               .id(dto.rate().id())
               .name(dto.rate().name())
               .hourlyRate(dto.rate().hourlyRate())
               .build());
      }

      // Pago
      if (dto.payment() != null) {
         response.setPayment(TransactionDetailResponse.PaymentInfo.builder()
               .id(dto.payment().id())
               .amount(dto.payment().amount())
               .referenceNumber(dto.payment().referenceNumber())
               .paymentDate(dto.payment().paymentDate())
               .status(dto.payment().status())
               .build());
      }

      return response;
   }

   /**
    * ActiveTransactionDto es record → usar dto.campo() sin "get"
    */
   public ActiveTransactionResponse toActiveResponse(ActiveTransactionDto dto) {
      if (dto == null) return null;

      return ActiveTransactionResponse.builder()
            .id(dto.id())
            .vehicleId(dto.vehicleId())
            .plateNumber(dto.plateNumber())
            .vehicleBrand(dto.vehicleBrand())
            .vehicleColor(dto.vehicleColor())
            .customerId(dto.customerId())
            .customerName(dto.customerName())
            .customerPhone(dto.customerPhone())
            .customerEmail(dto.customerEmail())
            .documentNumber(dto.documentNumber())
            .zoneId(dto.zoneId())
            .zoneName(dto.zoneName())
            .zoneCode(dto.zoneCode())
            .spaceId(dto.spaceId())
            .spaceCode(dto.spaceCode())
            .entryTime(dto.entryTime())
            .elapsedMinutes(dto.elapsedMinutes())
            .elapsedFormatted(dto.elapsedFormatted())
            .hourlyRate(dto.hourlyRate())
            .currentAmount(dto.currentAmount())
            .currency(dto.currency())                       // ← campo agregado
            .maxRecommendedMinutes(dto.maxRecommendedMinutes())
            .isOverdue(dto.isOverdue())
            .requiresAttention(dto.requiresAttention())
            .entryOperatorId(dto.entryOperatorId())
            .entryMethod(dto.entryMethod())
            .entryPhotoUrl(dto.entryPhotoUrl())
            .plateConfidence(dto.plateConfidence())
            .notes(dto.notes())
            .createdAt(dto.createdAt())
            .build();
   }
}