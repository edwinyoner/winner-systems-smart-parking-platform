package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.ParkingPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Parking;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.entity.ParkingEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.mapper.ParkingPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para Parking.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ParkingPersistenceAdapter implements ParkingPersistencePort {

   private final ParkingRepository repository;
   private final ParkingPersistenceMapper mapper;

   // ========================= SAVE =========================

   @Override
   public Parking save(Parking parking) {
      ParkingEntity entity;

      if (parking.getId() != null) {
         entity = repository.findById(parking.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Parking no encontrado con ID: " + parking.getId()
               ));
         mapper.updateEntity(entity, parking);
      } else {
         entity = mapper.toEntity(parking);
      }

      return mapper.toDomain(repository.save(entity));
   }

   // ========================= FIND =========================

   @Override
   public Optional<Parking> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Parking> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST PAGINADO =========================

   @Override
   public PageResult<Parking> findAll(PageRequest pageRequest, String search, String status) {
      Page<ParkingEntity> page = repository.findAllWithFilters(
            search,
            status,
            buildPageable(pageRequest)
      );
      return toPageResult(page);
   }

   // ========================= LIST SIN PAGINAR =========================

   @Override
   public List<Parking> findAllActive() {
      return repository.findByStatusAndDeletedAtIsNull(Parking.STATUS_ACTIVE)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   // ========================= EXISTS =========================

   @Override
   public boolean existsByCode(String code) {
      return repository.existsByCodeAndDeletedAtIsNull(code);
   }

   @Override
   public boolean existsByCodeAndIdNot(String code, Long id) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(e -> !e.getId().equals(id))
            .orElse(false);
   }

   // ========================= DELETE / COUNT =========================

   @Override
   public void delete(Long id) {
      repository.deleteById(id);
   }

   @Override
   public long count() {
      return repository.count();
   }

   // ========================= HELPERS PRIVADOS =========================

   private Pageable buildPageable(PageRequest pageRequest) {
      if (pageRequest.hasSorting()) {
         return org.springframework.data.domain.PageRequest.of(
               pageRequest.page(),
               pageRequest.size(),
               Sort.by(Sort.Direction.fromString(pageRequest.sortDirection()), pageRequest.sortBy())
         );
      }
      return org.springframework.data.domain.PageRequest.of(
            pageRequest.page(),
            pageRequest.size()
      );
   }

   private PageResult<Parking> toPageResult(Page<ParkingEntity> page) {
      List<Parking> content = page.getContent().stream()
            .map(mapper::toDomain)
            .toList();
      return PageResult.of(content, page.getNumber(), page.getSize(), page.getTotalElements());
   }
}