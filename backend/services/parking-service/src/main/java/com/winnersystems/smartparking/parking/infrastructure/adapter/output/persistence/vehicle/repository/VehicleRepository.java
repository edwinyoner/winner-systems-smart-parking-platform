package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio Spring Data JPA para VehicleEntity.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Long> {

   /**
    * Busca un vehículo por su placa.
    */
   Optional<VehicleEntity> findByLicensePlate(String licensePlate);

   /**
    * Verifica si existe un vehículo con esa placa.
    */
   boolean existsByLicensePlate(String licensePlate);
}