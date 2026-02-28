package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.DocumentTypePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.DocumentType;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.entity.DocumentTypeEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.mapper.DocumentTypePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.repository.DocumentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DocumentTypePersistenceAdapter implements DocumentTypePersistencePort {

   private final DocumentTypeRepository repository;
   private final DocumentTypePersistenceMapper mapper;

   // ========================= CREATE/UPDATE =========================

   @Override
   public DocumentType save(DocumentType documentType) {
      DocumentTypeEntity entity;

      if (documentType.getId() != null) {
         entity = repository.findById(documentType.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Tipo de documento no encontrado con ID: " + documentType.getId()));
         mapper.updateEntity(entity, documentType);
      } else {
         entity = mapper.toEntity(documentType);
      }

      return mapper.toDomain(repository.save(entity));
   }

   // ========================= READ =========================

   @Override
   public Optional<DocumentType> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<DocumentType> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST =========================

   @Override
   public List<DocumentType> findAllActive() {
      return repository.findByStatusTrueAndDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public PageResult<DocumentType> findAll(PageRequest pageRequest, String search, Boolean status) {
      Pageable pageable = buildPageable(pageRequest);

      boolean hasSearch = search != null && !search.trim().isEmpty();
      boolean hasStatus = status != null;

      Page<DocumentTypeEntity> page;

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

   // ========================= VALIDATION =========================

   @Override
   public boolean existsByCode(String code) {
      return repository.existsByCodeAndDeletedAtIsNull(code);
   }

   @Override
   public boolean existsByCodeAndIdNot(String code, Long id) {
      Optional<DocumentTypeEntity> existing = repository.findByCodeAndDeletedAtIsNull(code);
      return existing.isPresent() && !existing.get().getId().equals(id);
   }

   // ========================= DELETE =========================

   @Override
   public void delete(Long id) {
      repository.deleteById(id);
   }

   // ========================= HELPERS =========================

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
}