package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.RatePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Rate;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.entity.RateEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.mapper.RatePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para Rate.
 * Implementa el puerto de salida RatePersistencePort.
 * Traduce entre DTOs puros (Application) y Spring Data (Infrastructure).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class RatePersistenceAdapter implements RatePersistencePort {

   private final RateRepository repository;
   private final RatePersistenceMapper mapper;

   // ========================= CREATE/UPDATE =========================

   @Override
   public Rate save(Rate rate) {
      RateEntity entity;

      if (rate.getId() != null) {
         // Actualizar existente
         entity = repository.findById(rate.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Tarifa no encontrada con ID: " + rate.getId()
               ));
         mapper.updateEntity(entity, rate);
      } else {
         // Crear nueva
         entity = mapper.toEntity(rate);
      }

      RateEntity savedEntity = repository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   // ========================= READ =========================

   @Override
   public Optional<Rate> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   // ========================= LIST =========================

   @Override
   public List<Rate> findAllActive() {
      // FIX: OrderByIdAsc en lugar de OrderByNameAsc
      return repository.findByStatusTrueAndDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public PageResult<Rate> findAll(PageRequest pageRequest, String search, Boolean status) {
      Pageable pageable = buildPageable(pageRequest);

      boolean hasSearch = search != null && !search.trim().isEmpty();
      boolean hasStatus = status != null;

      Page<RateEntity> page;

      if (hasSearch && hasStatus) {
         page = repository.findByDeletedAtIsNullAndNameContainingIgnoreCaseAndStatus(
               search.trim(), status, pageable);
      } else if (hasSearch) {
         page = repository.findByDeletedAtIsNullAndNameContainingIgnoreCase(
               search.trim(), pageable);
      } else if (hasStatus) {
         page = repository.findByDeletedAtIsNullAndStatus(status, pageable);
      } else {
         page = repository.findByDeletedAtIsNull(pageable);
      }

      return PageResult.of(
            page.getContent().stream().map(mapper::toDomain).toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
      );
   }

   // Método privado reutilizable (reemplaza el bloque if/else duplicado)
   private Pageable buildPageable(PageRequest req) {
      if (req.hasSorting()) {
         return org.springframework.data.domain.PageRequest.of(
               req.page(), req.size(),
               Sort.by(Sort.Direction.fromString(req.sortDirection()), req.sortBy())
         );
      }
      return org.springframework.data.domain.PageRequest.of(
            req.page(), req.size(),
            Sort.by(Sort.Direction.ASC, "id")
      );
   }

   // ========================= VALIDATION =========================

   @Override
   public boolean existsByName(String name) {
      return repository.existsByNameAndDeletedAtIsNull(name);
   }

   @Override
   public boolean existsByNameAndIdNot(String name, Long id) {
      Optional<RateEntity> existing = repository.findByNameAndDeletedAtIsNull(name);
      return existing.isPresent() && !existing.get().getId().equals(id);
   }

   // ========================= DELETE =========================

   @Override
   public void delete(Long id) {
      // Hard delete (eliminar físicamente de la BD)
      repository.deleteById(id);
   }
}