package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.SpacePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Space;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.entity.SpaceEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.mapper.SpacePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.space.repository.SpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para Space.
 * Implementa el puerto de salida SpacePersistencePort.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class SpacePersistenceAdapter implements SpacePersistencePort {

   private final SpaceRepository repository;
   private final SpacePersistenceMapper mapper;

   // ========================= SAVE =========================

   @Override
   public Space save(Space space) {
      SpaceEntity entity;

      if (space.getId() != null) {
         entity = repository.findById(space.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Espacio no encontrado con ID: " + space.getId()
               ));
         mapper.updateEntity(entity, space);
      } else {
         entity = mapper.toEntity(space);
      }

      return mapper.toDomain(repository.save(entity));
   }

   // ========================= FIND =========================

   @Override
   public Optional<Space> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Space> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST SIN PAGINAR =========================

   @Override
   public List<Space> findAllActive() {
      return repository.findByStatusAndDeletedAtIsNull(Space.STATUS_AVAILABLE)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   // ========================= LIST PAGINADO =========================

   @Override
   public PageResult<Space> findAll(PageRequest pageRequest, String search, String status) {
      Page<SpaceEntity> page = repository.findAllWithFilters(
            search,
            status,
            buildPageable(pageRequest)
      );
      return toPageResult(page);
   }

   // ========================= LIST BY ZONE =========================

   @Override
   public List<Space> findByZoneId(Long zoneId) {
      return repository.findByZoneIdAndDeletedAtIsNull(zoneId)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public List<Space> findAvailableByZoneId(Long zoneId) {
      return repository.findAvailableByZoneId(zoneId)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public List<Space> findByZoneIdAndType(Long zoneId, String type) {
      return repository.findByZoneIdAndType(zoneId, type)
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   // ========================= COUNT =========================

   @Override
   public long countAvailableByZoneId(Long zoneId) {
      return repository.countAvailableByZoneId(zoneId);
   }

   @Override
   public long countByZoneId(Long zoneId) {
      return repository.countByZoneIdAndDeletedAtIsNull(zoneId);
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

   private PageResult<Space> toPageResult(Page<SpaceEntity> page) {
      List<Space> content = page.getContent().stream()
            .map(mapper::toDomain)
            .toList();
      return PageResult.of(content, page.getNumber(), page.getSize(), page.getTotalElements());
   }
}