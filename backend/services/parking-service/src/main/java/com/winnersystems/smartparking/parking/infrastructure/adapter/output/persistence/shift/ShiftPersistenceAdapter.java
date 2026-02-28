package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.ShiftPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Shift;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.entity.ShiftEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.mapper.ShiftPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.repository.ShiftRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para Shift.
 * Implementa el puerto de salida ShiftPersistencePort.
 * Traduce entre DTOs puros (Application) y Spring Data (Infrastructure).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class ShiftPersistenceAdapter implements ShiftPersistencePort {

   private final ShiftRepository repository;
   private final ShiftPersistenceMapper mapper;

   // ========================= CREATE/UPDATE =========================

   @Override
   public Shift save(Shift shift) {
      ShiftEntity entity;

      if (shift.getId() != null) {
         // Actualizar existente
         entity = repository.findById(shift.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Turno no encontrado con ID: " + shift.getId()
               ));
         mapper.updateEntity(entity, shift);
      } else {
         // Crear nuevo
         entity = mapper.toEntity(shift);
      }

      ShiftEntity savedEntity = repository.save(entity);
      return mapper.toDomain(savedEntity);
   }

   // ========================= READ =========================

   @Override
   public Optional<Shift> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<Shift> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST =========================

   @Override
   public List<Shift> findAllActive() {
      // FIX: OrderByIdAsc en lugar de OrderByNameAsc
      return repository.findByStatusTrueAndDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public PageResult<Shift> findAll(PageRequest pageRequest, String search, Boolean status) {
      Pageable pageable = buildPageable(pageRequest);

      boolean hasSearch = search != null && !search.trim().isEmpty();
      boolean hasStatus = status != null;

      Page<ShiftEntity> page;

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
   public boolean existsByCode(String code) {
      return repository.existsByCodeAndDeletedAtIsNull(code);
   }

   @Override
   public boolean existsByCodeAndIdNot(String code, Long id) {
      Optional<ShiftEntity> existing = repository.findByCodeAndDeletedAtIsNull(code);
      return existing.isPresent() && !existing.get().getId().equals(id);
   }

   // ========================= DELETE =========================

   @Override
   public void delete(Long id) {
      // Hard delete (eliminar físicamente de la BD)
      repository.deleteById(id);
   }
}