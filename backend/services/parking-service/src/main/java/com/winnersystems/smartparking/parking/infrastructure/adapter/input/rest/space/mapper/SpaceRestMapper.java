package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.CreateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateSpaceCommand;
import com.winnersystems.smartparking.parking.application.dto.query.SpaceDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.request.CreateSpaceRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.request.UpdateSpaceRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.space.dto.response.SpaceResponse;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para espacios.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class SpaceRestMapper {

   /**
    * Convierte CreateSpaceRequest a CreateSpaceCommand.
    */
   public CreateSpaceCommand toCommand(CreateSpaceRequest request) {
      return CreateSpaceCommand.builder()
            .zoneId(request.getZoneId())
            .type(request.getType())
            .code(request.getCode())
            .description(request.getDescription())
            .width(request.getWidth())
            .length(request.getLength())
            .hasSensor(request.getHasSensor())
            .sensorId(request.getSensorId())
            .hasCameraCoverage(request.getHasCameraCoverage())
//            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte UpdateSpaceRequest a UpdateSpaceCommand.
    */
   public UpdateSpaceCommand toCommand(UpdateSpaceRequest request) {
      return UpdateSpaceCommand.builder()
            .type(request.getType())
            .description(request.getDescription())
            .width(request.getWidth())
            .length(request.getLength())
            .hasSensor(request.getHasSensor())
            .sensorId(request.getSensorId())
            .hasCameraCoverage(request.getHasCameraCoverage())
            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte SpaceDto a SpaceResponse.
    */
   public SpaceResponse toResponse(SpaceDto dto) {
      return new SpaceResponse(
            dto.id(),
            dto.zoneId(),
            dto.type(),
            dto.code(),
            dto.description(),
            dto.width(),
            dto.length(),
            dto.hasSensor(),
            dto.sensorId(),
            dto.hasCameraCoverage(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}