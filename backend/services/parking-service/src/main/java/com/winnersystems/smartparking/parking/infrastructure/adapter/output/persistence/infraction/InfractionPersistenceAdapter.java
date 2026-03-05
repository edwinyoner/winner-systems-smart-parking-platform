package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.InfractionPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Infraction;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.entity.InfractionEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.mapper.InfractionPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.repository.InfractionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Infraction.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class InfractionPersistenceAdapter implements InfractionPersistencePort {

   private final InfractionRepository infractionRepository;
   private final InfractionPersistenceMapper infractionMapper;

   // ========================= WRITE =========================

   @Override
   public Infraction save(Infraction infraction) {
      InfractionEntity entity = infractionMapper.toEntity(infraction);
      InfractionEntity savedEntity = infractionRepository.save(entity);
      return infractionMapper.toDomain(savedEntity);
   }

   @Override
   public void delete(Long id) {
      infractionRepository.deleteById(id);
   }

   // ========================= FIND ÚNICO =========================

   @Override
   public Optional<Infraction> findById(Long id) {
      return infractionRepository.findById(id)
            .map(infractionMapper::toDomain);
   }

   @Override
   public Optional<Infraction> findByCode(String infractionCode) {
      return infractionRepository.findByInfractionCode(infractionCode)
            .map(infractionMapper::toDomain);
   }

   // ========================= EXISTS =========================

   @Override
   public boolean existsByCode(String infractionCode) {
      return infractionRepository.existsByInfractionCode(infractionCode);
   }

   // ========================= LIST (paginado) =========================

   @Override
   public PageResult<Infraction> findAll(PageRequest pageRequest, String search, String status) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findAllWithFilters(search, status, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Infraction> findByVehicle(Long vehicleId, PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findByVehicleId(vehicleId, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Infraction> findByCustomer(Long customerId, PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findByCustomerId(customerId, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public List<Infraction> findByTransaction(Long transactionId) {
      return infractionRepository.findByTransactionId(transactionId).stream()
            .map(infractionMapper::toDomain)
            .collect(Collectors.toList());
   }

   @Override
   public PageResult<Infraction> findWithPendingFines(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findWithPendingFines(pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Infraction> findWithOverdueFines(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findWithOverdueFines(
            LocalDateTime.now(),
            pageable
      );
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Infraction> findByDetectionDateRange(LocalDateTime startDate,
                                                          LocalDateTime endDate,
                                                          PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<InfractionEntity> page = infractionRepository.findByDetectionDateRange(
            startDate,
            endDate,
            pageable
      );
      return toPageResult(page, pageRequest);
   }

   // ========================= LIST (sin paginación) =========================

   @Override
   public List<Infraction> findAllPending() {
      return infractionRepository.findAllPending().stream()
            .map(infractionMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= COUNT =========================

   @Override
   public long count() {
      return infractionRepository.count();
   }

   @Override
   public long countByStatus(String status) {
      return infractionRepository.countByStatus(status);
   }

   @Override
   public long countPendingFines() {
      return infractionRepository.countPendingFines();
   }

   @Override
   public long countOverdueFines() {
      return infractionRepository.countOverdueFines(LocalDateTime.now());
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

   private PageResult<Infraction> toPageResult(Page<InfractionEntity> page, PageRequest pageRequest) {
      List<Infraction> content = page.getContent().stream()
            .map(infractionMapper::toDomain)
            .collect(Collectors.toList());

      return PageResult.of(content, pageRequest, page.getTotalElements());
   }
}