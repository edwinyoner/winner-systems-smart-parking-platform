package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.mapper;

import org.springframework.stereotype.Component;
import com.winnersystems.smartparking.parking.application.dto.command.CreateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateShiftCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ShiftDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request.CreateShiftRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.request.UpdateShiftRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.shift.dto.response.ShiftResponse;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para turnos.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ShiftRestMapper {

   /**
    * Convierte CreateShiftRequest a CreateShiftCommand.
    */
   public CreateShiftCommand toCommand(CreateShiftRequest request) {
      return CreateShiftCommand.builder()
            .name(request.getName())
            .code(request.getCode())
            .description(request.getDescription())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
//            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte UpdateShiftRequest a UpdateShiftCommand.
    */
   public UpdateShiftCommand toCommand(UpdateShiftRequest request) {
      return UpdateShiftCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .startTime(request.getStartTime())
            .endTime(request.getEndTime())
            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte ShiftDto a ShiftResponse.
    */
   public ShiftResponse toResponse(ShiftDto dto) {
      if (dto == null) return null;

      return new ShiftResponse(
            dto.id(),
            dto.name(),
            dto.code(),
            dto.description(),
            dto.startTime(),
            dto.endTime(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}