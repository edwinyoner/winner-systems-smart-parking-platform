package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate;

import com.winnersystems.smartparking.parking.application.port.output.ParkingShiftRatePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.ParkingShiftRate;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.entity.ParkingShiftRateEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.mapper.ParkingShiftRatePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.repository.ParkingShiftRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para ParkingShiftRate.
 * Implementa el puerto de salida definido en la capa de aplicación.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ParkingShiftRatePersistenceAdapter implements ParkingShiftRatePersistencePort {

   private final ParkingShiftRateRepository repository;
   private final ParkingShiftRatePersistenceMapper mapper;

   @Override
   @Transactional
   public ParkingShiftRate save(ParkingShiftRate config) {
      log.debug("💾 Guardando configuración: {}", config);

      ParkingShiftRateEntity entity;

      if (config.getId() != null) {
         // Actualización de configuración existente
         entity = repository.findById(config.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Configuración no encontrada con ID: " + config.getId()
               ));
         mapper.updateEntity(entity, config);
      } else {
         // Nueva configuración
         entity = mapper.toEntity(config);
      }

      ParkingShiftRateEntity savedEntity = repository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ParkingShiftRate> findById(Long id) {
      log.debug("🔍 Buscando configuración por ID: {}", id);
      return repository.findById(id)
            .map(mapper::toDomain);
   }

   @Override
   @Transactional(readOnly = true)
   public List<ParkingShiftRate> findByParkingId(Long parkingId) {
      log.debug("📋 Listando configuraciones del parqueo ID: {}", parkingId);
      return repository.findByParkingId(parkingId)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public Optional<ParkingShiftRate> findByParkingIdAndShiftId(
         Long parkingId,
         Long shiftId
   ) {
      log.debug("🔍 Buscando configuración: parking={}, shift={}", parkingId, shiftId);

      return repository.findByParkingIdAndShiftId(parkingId, shiftId)
            .map(mapper::toDomain);
   }

   @Override
   @Transactional
   public void deleteByParkingId(Long parkingId) {
      log.info("🗑️ Eliminando todas las configuraciones del parqueo ID: {}", parkingId);
      repository.deleteByParkingId(parkingId);
   }

   @Override
   @Transactional
   public void deleteById(Long id) {
      log.info("🗑️ Eliminando configuración ID: {}", id);
      repository.deleteById(id);
   }

   @Override
   @Transactional(readOnly = true)
   public boolean existsByParkingIdAndShiftId(Long parkingId, Long shiftId) {
      return repository.existsByParkingIdAndShiftId(parkingId, shiftId);
   }
}