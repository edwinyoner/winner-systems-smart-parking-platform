package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.CreateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateZoneCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ZoneDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request.CreateZoneRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.request.UpdateZoneRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.zone.dto.response.ZoneResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para zonas.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ZoneRestMapper {

   /**
    * Convierte CreateZoneRequest a CreateZoneCommand.
    */
   public CreateZoneCommand toCommand(CreateZoneRequest request) {
      return CreateZoneCommand.builder()
            .parkingId(request.getParkingId())
            .name(request.getName())
            .code(request.getCode())
            .address(request.getAddress())
            .description(request.getDescription())
            .totalSpaces(request.getTotalSpaces())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .hasCamera(request.getHasCamera())
            .cameraIds(request.getCameraIds())
            .build();
   }

   /**
    * Convierte UpdateZoneRequest a UpdateZoneCommand.
    */
   public UpdateZoneCommand toCommand(UpdateZoneRequest request) {
      return UpdateZoneCommand.builder()
            .name(request.getName())
            .address(request.getAddress())
            .description(request.getDescription())
            .totalSpaces(request.getTotalSpaces())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .hasCamera(request.getHasCamera())
            .cameraIds(request.getCameraIds())
            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte ZoneDto a ZoneResponse.
    */
   public ZoneResponse toResponse(ZoneDto dto) {
      return new ZoneResponse(
            dto.id(),
            dto.parkingId(),
            dto.name(),
            dto.code(),
            dto.address(),
            dto.description(),
            dto.totalSpaces(),
            dto.availableSpaces(),
            dto.occupancyPercentage(),
            dto.latitude(),
            dto.longitude(),
            dto.hasCamera(),
            dto.cameraIds(),
            dto.cameraCount(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}