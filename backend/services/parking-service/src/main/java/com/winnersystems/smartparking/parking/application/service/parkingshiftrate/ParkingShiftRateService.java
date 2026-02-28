package com.winnersystems.smartparking.parking.application.service.parkingshiftrate;

import com.winnersystems.smartparking.parking.application.dto.command.ConfigureParkingShiftRatesCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingShiftRateDto;
import com.winnersystems.smartparking.parking.application.port.input.parkingshiftrate.ConfigureParkingShiftRatesUseCase;
import com.winnersystems.smartparking.parking.application.port.output.ParkingPersistencePort;
import com.winnersystems.smartparking.parking.application.port.output.ParkingShiftRatePersistencePort;
import com.winnersystems.smartparking.parking.application.port.output.RatePersistencePort;
import com.winnersystems.smartparking.parking.application.port.output.ShiftPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Parking;
import com.winnersystems.smartparking.parking.domain.model.ParkingShiftRate;
import com.winnersystems.smartparking.parking.domain.model.Rate;
import com.winnersystems.smartparking.parking.domain.model.Shift;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio para gestión de configuraciones ParkingShiftRate.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ParkingShiftRateService implements ConfigureParkingShiftRatesUseCase {

   private final ParkingShiftRatePersistencePort parkingShiftRatePort;
   private final ParkingPersistencePort parkingPort;
   private final ShiftPersistencePort shiftPort;
   private final RatePersistencePort ratePort;

   @Override
   @Transactional
   public List<ParkingShiftRateDto> configureShiftRates(ConfigureParkingShiftRatesCommand command) {
      log.info("🔧 Configurando tarifas por turno para parqueo ID: {}", command.getParkingId());

      // Validar que el parqueo existe
      Parking parking = parkingPort.findById(command.getParkingId())
            .orElseThrow(() -> new IllegalArgumentException(
                  "Parqueo no encontrado con ID: " + command.getParkingId()
            ));

      // Eliminar configuraciones anteriores de este parqueo
      parkingShiftRatePort.deleteByParkingId(command.getParkingId());

      // Crear nuevas configuraciones
      List<ParkingShiftRate> savedConfigs = command.getConfigurations().stream()
            .map(config -> {
               // Validar que shift y rate existen
               validateShiftExists(config.getShiftId());
               validateRateExists(config.getRateId());

               // Crear configuración
               ParkingShiftRate parkingShiftRate = new ParkingShiftRate(
                     command.getParkingId(),
                     config.getShiftId(),
                     config.getRateId(),
                     config.getStatus() != null ? config.getStatus() : true
               );

               return parkingShiftRatePort.save(parkingShiftRate);
            })
            .toList();

      log.info("✅ {} configuraciones guardadas para parqueo {} (se aplican a todas sus zonas)",
            savedConfigs.size(), command.getParkingId());

      return savedConfigs.stream()
            .map(this::mapToDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public List<ParkingShiftRateDto> getParkingShiftRates(Long parkingId) {
      log.debug("📋 Obteniendo configuraciones de parqueo ID: {}", parkingId);

      List<ParkingShiftRate> configs = parkingShiftRatePort.findByParkingId(parkingId);

      return configs.stream()
            .map(this::mapToDto)
            .toList();
   }

   @Override
   @Transactional
   public void deleteShiftRateConfig(Long configId) {
      log.info("🗑️ Eliminando configuración ID: {}", configId);

      parkingShiftRatePort.deleteById(configId);

      log.info("✅ Configuración eliminada");
   }

   @Override
   @Transactional
   public ParkingShiftRateDto toggleShiftRateStatus(Long configId) {
      log.info("🔄 Cambiando estado de configuración ID: {}", configId);

      ParkingShiftRate config = parkingShiftRatePort.findById(configId)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Configuración no encontrada con ID: " + configId
            ));

      config.toggleStatus();
      ParkingShiftRate updated = parkingShiftRatePort.save(config);

      log.info("✅ Estado cambiado a: {}", updated.getStatus() ? "ACTIVO" : "INACTIVO");

      return mapToDto(updated);
   }

   // ========================= VALIDACIONES =========================

   private void validateShiftExists(Long shiftId) {
      if (!shiftPort.findById(shiftId).isPresent()) {
         throw new IllegalArgumentException("Turno no encontrado con ID: " + shiftId);
      }
   }

   private void validateRateExists(Long rateId) {
      if (!ratePort.findById(rateId).isPresent()) {
         throw new IllegalArgumentException("Tarifa no encontrada con ID: " + rateId);
      }
   }

   // ========================= MAPPER =========================

   private ParkingShiftRateDto mapToDto(ParkingShiftRate config) {
      // Obtener datos de Shift y Rate para el DTO
      Shift shift = shiftPort.findById(config.getShiftId()).orElse(null);
      Rate rate = ratePort.findById(config.getRateId()).orElse(null);

      return new ParkingShiftRateDto(
            config.getId(),
            config.getParkingId(),
            config.getShiftId(),
            shift != null ? shift.getName() : null,
            shift != null ? shift.getCode() : null,
            config.getRateId(),
            rate != null ? rate.getName() : null,
            rate != null ? rate.getAmount() : null,
            rate != null ? rate.getCurrency() : null,
            config.getStatus(),
            config.getCreatedAt(),
            config.getUpdatedAt()
      );
   }
}