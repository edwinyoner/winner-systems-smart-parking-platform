package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.CustomerVehiclePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.CustomerVehicle;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.entity.CustomerVehicleEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.mapper.CustomerVehiclePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customervehicle.repository.CustomerVehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para CustomerVehicle.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomerVehiclePersistenceAdapter implements CustomerVehiclePersistencePort {

   private final CustomerVehicleRepository customerVehicleRepository;
   private final CustomerVehiclePersistenceMapper customerVehicleMapper;

   // ========================= WRITE =========================

   @Override
   public CustomerVehicle save(CustomerVehicle customerVehicle) {
      CustomerVehicleEntity entity = customerVehicleMapper.toEntity(customerVehicle);
      CustomerVehicleEntity savedEntity = customerVehicleRepository.save(entity);
      return customerVehicleMapper.toDomain(savedEntity);
   }

   // ========================= FIND ÚNICO =========================

   @Override
   public Optional<CustomerVehicle> findById(Long id) {
      return customerVehicleRepository.findById(id)
            .map(customerVehicleMapper::toDomain);
   }

   @Override
   public Optional<CustomerVehicle> findByCustomerAndVehicle(Long customerId, Long vehicleId) {
      return customerVehicleRepository.findByCustomerIdAndVehicleId(customerId, vehicleId)
            .map(customerVehicleMapper::toDomain);
   }

   // ========================= EXISTS =========================

   @Override
   public boolean existsByCustomerAndVehicle(Long customerId, Long vehicleId) {
      return customerVehicleRepository.existsByCustomerIdAndVehicleId(customerId, vehicleId);
   }

   // ========================= LIST (por customer) =========================

   @Override
   public List<CustomerVehicle> findByCustomerId(Long customerId) {
      return customerVehicleRepository.findByCustomerId(customerId).stream()
            .map(customerVehicleMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= LIST (por vehicle) =========================

   @Override
   public List<CustomerVehicle> findByVehicleId(Long vehicleId) {
      return customerVehicleRepository.findByVehicleId(vehicleId).stream()
            .map(customerVehicleMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= LIST (combinaciones frecuentes - paginado) =========================

   @Override
   public PageResult<CustomerVehicle> findFrequentCombinations(int minUsageCount, PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<CustomerVehicleEntity> page = customerVehicleRepository.findFrequentCombinations(
            minUsageCount,
            pageable
      );
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<CustomerVehicle> findAll(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<CustomerVehicleEntity> page = customerVehicleRepository.findAll(pageable);
      return toPageResult(page, pageRequest);
   }

   // ========================= COUNT =========================

   @Override
   public long count() {
      return customerVehicleRepository.count();
   }

   @Override
   public long countByCustomerId(Long customerId) {
      return customerVehicleRepository.countByCustomerId(customerId);
   }

   @Override
   public long countByVehicleId(Long vehicleId) {
      return customerVehicleRepository.countByVehicleId(vehicleId);
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

   private PageResult<CustomerVehicle> toPageResult(Page<CustomerVehicleEntity> page, PageRequest pageRequest) {
      List<CustomerVehicle> content = page.getContent().stream()
            .map(customerVehicleMapper::toDomain)
            .collect(Collectors.toList());

      return PageResult.of(content, pageRequest, page.getTotalElements());
   }
}