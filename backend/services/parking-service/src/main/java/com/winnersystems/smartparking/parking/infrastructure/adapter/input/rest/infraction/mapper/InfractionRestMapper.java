package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.AssignFineCommand;
import com.winnersystems.smartparking.parking.application.dto.command.CreateInfractionCommand;
import com.winnersystems.smartparking.parking.application.dto.command.RecordFinePaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateInfractionCommand;
import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.AssignFineRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.CreateInfractionRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.RecordFinePaymentRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.request.UpdateInfractionRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.infraction.dto.response.InfractionResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre DTOs REST y Commands/DTOs de Application.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class InfractionRestMapper {

   // ========================= REQUEST → COMMAND =========================

   /**
    * CreateInfractionRequest → CreateInfractionCommand
    */
   public CreateInfractionCommand toCreateCommand(CreateInfractionRequest request) {
      return new CreateInfractionCommand(
            request.getParkingId(),
            request.getVehicleId(),
            request.getZoneId(),
            request.getSpaceId(),
            request.getTransactionId(),
            request.getCustomerId(),
            request.getInfractionType(),
            request.getSeverity(),
            request.getDescription(),
            request.getEvidence()
      );
   }

   /**
    * UpdateInfractionRequest → UpdateInfractionCommand
    */
   public UpdateInfractionCommand toUpdateCommand(UpdateInfractionRequest request) {
      return new UpdateInfractionCommand(
            request.getDescription(),
            request.getSeverity(),
            request.getEvidence(),
            request.getNotes()
      );
   }

   /**
    * AssignFineRequest → AssignFineCommand
    */
   public AssignFineCommand toAssignFineCommand(AssignFineRequest request) {
      return new AssignFineCommand(
            request.getFineAmount(),
            request.getFineDueDate()
      );
   }

   /**
    * RecordFinePaymentRequest → RecordFinePaymentCommand
    */
   public RecordFinePaymentCommand toRecordFinePaymentCommand(RecordFinePaymentRequest request) {
      return new RecordFinePaymentCommand(
            request.getAmount(),
            request.getReference()
      );
   }

   // ========================= DTO → RESPONSE =========================

   /**
    * InfractionDto → InfractionResponse
    */
   public InfractionResponse toResponse(InfractionDto dto) {
      return InfractionResponse.builder()
            .id(dto.id())
            .infractionCode(dto.infractionCode())
            .parkingId(dto.parkingId())
            .parkingName(dto.parkingName())
            .zoneId(dto.zoneId())
            .zoneName(dto.zoneName())
            .spaceId(dto.spaceId())
            .spaceCode(dto.spaceCode())
            .transactionId(dto.transactionId())
            .vehicleId(dto.vehicleId())
            .vehiclePlate(dto.vehiclePlate())
            .customerId(dto.customerId())
            .customerName(dto.customerName())
            .infractionType(dto.infractionType())
            .severity(dto.severity())
            .description(dto.description())
            .evidence(dto.evidence())
            .detectedAt(dto.detectedAt())
            .detectedBy(dto.detectedBy())
            .detectedByName(dto.detectedByName())
            .detectionMethod(dto.detectionMethod())
            .fineAmount(dto.fineAmount())
            .currency(dto.currency())
            .fineDueDate(dto.fineDueDate())
            .finePaid(dto.finePaid())
            .finePaidAt(dto.finePaidAt())
            .status(dto.status())
            .resolvedAt(dto.resolvedAt())
            .resolutionType(dto.resolutionType())
            .resolution(dto.resolution())
            .notificationSent(dto.notificationSent())
            .createdAt(dto.createdAt())
            .build();
   }
}