package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parkingshiftrate.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.ConfigureParkingShiftRatesCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingShiftRateDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parkingshiftrate.dto.request.ConfigureParkingShiftRatesRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.parkingshiftrate.dto.response.ParkingShiftRateResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Mapper para conversión entre DTOs REST y DTOs de aplicación para ParkingShiftRate.
 * Convierte Request → Command y Dto → Response.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class ParkingShiftRateRestMapper {

   /**
    * Convierte ConfigureParkingShiftRatesRequest a ConfigureParkingShiftRatesCommand.
    *
    * @param parkingId ID del parqueo al que se aplica la configuración
    * @param request datos de configuración desde el frontend
    * @return comando listo para el caso de uso
    */
   public ConfigureParkingShiftRatesCommand toCommand(
         Long parkingId,
         ConfigureParkingShiftRatesRequest request
   ) {
      List<ConfigureParkingShiftRatesCommand.ShiftRateConfig> configs =
            request.getConfigurations().stream()
                  .map(req -> ConfigureParkingShiftRatesCommand.ShiftRateConfig.builder()
                        .shiftId(req.getShiftId())
                        .rateId(req.getRateId())
                        .status(req.getStatus() != null ? req.getStatus() : true)
                        .build())
                  .toList();

      return ConfigureParkingShiftRatesCommand.builder()
            .parkingId(parkingId)
            .configurations(configs)
            .build();
   }

   /**
    * Convierte ParkingShiftRateDto a ParkingShiftRateResponse.
    *
    * @param dto DTO desde el caso de uso
    * @return respuesta lista para enviar al frontend
    */
   public ParkingShiftRateResponse toResponse(ParkingShiftRateDto dto) {
      return new ParkingShiftRateResponse(
            dto.id(),
            dto.parkingId(),
            dto.shiftId(),
            dto.shiftName(),
            dto.shiftCode(),
            dto.rateId(),
            dto.rateName(),
            dto.rateAmount(),
            dto.rateCurrency(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}