package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle;

import com.winnersystems.smartparking.parking.application.port.output.VehiclePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Vehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.mapper.VehiclePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de persistencia para Vehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class VehiclePersistenceAdapter implements VehiclePersistencePort {

   private final VehicleRepository vehicleRepository;
   private final VehiclePersistenceMapper vehicleMapper;

   @Override
   public Vehicle save(Vehicle vehicle) {
      VehicleEntity entity = vehicleMapper.toEntity(vehicle);
      VehicleEntity savedEntity = vehicleRepository.save(entity);
      return vehicleMapper.toDomain(savedEntity);
   }

   @Override
   public Optional<Vehicle> findById(Long id) {
      return vehicleRepository.findById(id)
            .map(vehicleMapper::toDomain);
   }

   @Override
   public Optional<Vehicle> findByPlateNumber(String plateNumber) {
      return vehicleRepository.findByLicensePlate(plateNumber)
            .map(vehicleMapper::toDomain);
   }

   @Override
   public boolean existsByPlateNumber(String plateNumber) {
      return vehicleRepository.existsByLicensePlate(plateNumber);
   }
}