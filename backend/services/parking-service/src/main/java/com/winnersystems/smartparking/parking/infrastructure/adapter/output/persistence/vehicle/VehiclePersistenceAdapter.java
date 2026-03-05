package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.VehiclePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Vehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.entity.VehicleEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.mapper.VehiclePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

   // ========================= WRITE =========================

   @Override
   public Vehicle save(Vehicle vehicle) {
      VehicleEntity entity = vehicleMapper.toEntity(vehicle);
      VehicleEntity savedEntity = vehicleRepository.save(entity);
      return vehicleMapper.toDomain(savedEntity);
   }

   @Override
   public void delete(Long id) {
      vehicleRepository.deleteById(id);
   }

   // ========================= FIND ÚNICO =========================

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

   // ========================= EXISTS =========================

   @Override
   public boolean existsByPlateNumber(String plateNumber) {
      return vehicleRepository.existsByLicensePlate(plateNumber);
   }

   // ========================= LIST (paginado) =========================

   @Override
   public PageResult<Vehicle> findAll(PageRequest pageRequest, String search, String status) {
      Pageable pageable = buildPageable(pageRequest);
      Page<VehicleEntity> page = vehicleRepository.findAllWithFilters(search, status, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Vehicle> findRecurrent(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<VehicleEntity> page = vehicleRepository.findRecurrent(pageable);
      return toPageResult(page, pageRequest);
   }

   // ========================= LIST (sin paginación) =========================

   @Override
   public List<Vehicle> findAllActive() {
      return vehicleRepository.findAllActive().stream()
            .map(vehicleMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= COUNT =========================

   @Override
   public long count() {
      return vehicleRepository.count();
   }

   @Override
   public long countActive() {
      return vehicleRepository.countActive();
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private Pageable buildPageable(PageRequest pageRequest) {
      if (pageRequest.hasSorting()) {
         org.springframework.data.domain.Sort.Direction direction =
               pageRequest.isAscending()
                     ? org.springframework.data.domain.Sort.Direction.ASC
                     : org.springframework.data.domain.Sort.Direction.DESC;

         return org.springframework.data.domain.PageRequest.of(
               pageRequest.page(),
               pageRequest.size(),
               org.springframework.data.domain.Sort.by(direction, pageRequest.sortBy())
         );
      }
      return org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());
   }

   private PageResult<Vehicle> toPageResult(Page<VehicleEntity> page, PageRequest pageRequest) {
      List<Vehicle> content = page.getContent().stream()
            .map(vehicleMapper::toDomain)
            .collect(Collectors.toList());

      return PageResult.of(content, pageRequest, page.getTotalElements());
   }
}