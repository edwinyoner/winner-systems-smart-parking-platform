package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.mapper;

import org.springframework.stereotype.Component;
import com.winnersystems.smartparking.parking.application.dto.command.CreateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateRateCommand;
import com.winnersystems.smartparking.parking.application.dto.query.RateDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.request.CreateRateRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.request.UpdateRateRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.rate.dto.response.RateResponse;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para tarifas.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class RateRestMapper {

   /**
    * Convierte CreateRateRequest a CreateRateCommand.
    */
   public CreateRateCommand toCommand(CreateRateRequest request) {
      return CreateRateCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .amount(request.getAmount())
            .currency(request.getCurrency())
//            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte UpdateRateRequest a UpdateRateCommand.
    */
   public UpdateRateCommand toCommand(UpdateRateRequest request) {
      return UpdateRateCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .amount(request.getAmount())
            .currency(request.getCurrency())
            .status(request.getStatus())
            .build();
   }

   /**
    * Convierte RateDto a RateResponse.
    */
   public RateResponse toResponse(RateDto dto) {
      if (dto == null) return null;

      return new RateResponse(
            dto.id(),
            dto.name(),
            dto.description(),
            dto.amount(),
            dto.currency(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}