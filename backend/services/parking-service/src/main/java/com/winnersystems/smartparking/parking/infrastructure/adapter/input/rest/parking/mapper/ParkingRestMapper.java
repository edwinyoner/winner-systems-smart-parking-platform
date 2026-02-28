package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.CreateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateParkingCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.request.CreateParkingRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.request.UpdateParkingRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parking.dto.response.ParkingResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para parkings.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ParkingRestMapper {

   /**
    * Convierte CreateParkingRequest a CreateParkingCommand.
    */
   public CreateParkingCommand toCommand(CreateParkingRequest request) {
      return CreateParkingCommand.builder()
            .name(request.getName())
            .code(request.getCode())
            .description(request.getDescription())
            .address(request.getAddress())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .managerId(request.getManagerId())
            .managerName(request.getManagerName())
            .build();
   }

   /**
    * Convierte UpdateParkingRequest a UpdateParkingCommand.
    */
   public UpdateParkingCommand toCommand(UpdateParkingRequest request) {
      return UpdateParkingCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .address(request.getAddress())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .managerId(request.getManagerId())
            .managerName(request.getManagerName())
            .build();
   }

   /**
    * Convierte ParkingDto a ParkingResponse.
    */
   public ParkingResponse toResponse(ParkingDto dto) {
      return new ParkingResponse(
            dto.id(),
            dto.name(),
            dto.code(),
            dto.description(),
            dto.address(),
            dto.latitude(),
            dto.longitude(),
            dto.managerId(),
            dto.managerName(),
            dto.totalZones(),
            dto.totalSpaces(),
            dto.availableSpaces(),
            dto.occupancyPercentage(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}