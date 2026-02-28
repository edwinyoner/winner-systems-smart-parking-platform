package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.ZonePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Zone;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.entity.ZoneEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.mapper.ZonePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.zone.repository.ZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para Zone.
 * Implementa el puerto de salida ZonePersistencePort.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ZonePersistenceAdapter implements ZonePersistencePort {

   private final ZoneRepository repository;
   private final ZonePersistenceMapper mapper;

   // ========================= SAVE =========================

   @Override
   public Zone save(Zone zone) {
      ZoneEntity entity;

      if (zone.getId() != null) {
         entity = repository.findById(zone.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Zona no encontrada con ID: " + zone.getId()
               ));
         mapper.updateEntity(entity, zone);
      } else {
         entity = mapper.toEntity(zone);
      }

      return mapper.toDomain(repository.save(entity));
   }

   // ========================= FIND =========================

   @Override
   public Optional<Zone> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Zone> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST SIN PAGINAR =========================

   @Override
   public List<Zone> findAllActive() {
      return repository.findByStatusAndDeletedAtIsNullOrderByNameAsc(Zone.STATUS_ACTIVE)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public List<Zone> findByParkingId(Long parkingId) {              // ← AGREGADO
      return repository.findByParkingIdAndDeletedAtIsNull(parkingId)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   // ========================= LIST PAGINADO =========================

   @Override
   public PageResult<Zone> findAll(PageRequest pageRequest, String search, String status) {
      Page<ZoneEntity> page = repository.findAllWithFilters(
            search,
            status,
            buildPageable(pageRequest)
      );
      return toPageResult(page);
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

   @Override
   public boolean existsByName(String name) {
      return repository.existsByNameAndDeletedAtIsNull(name);
   }

   // ========================= DELETE =========================

   @Override
   public void delete(Long id) {
      repository.deleteById(id);
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

   private PageResult<Zone> toPageResult(Page<ZoneEntity> page) {
      List<Zone> content = page.getContent().stream()
            .map(mapper::toDomain)
            .toList();
      return PageResult.of(content, page.getNumber(), page.getSize(), page.getTotalElements());
   }
}