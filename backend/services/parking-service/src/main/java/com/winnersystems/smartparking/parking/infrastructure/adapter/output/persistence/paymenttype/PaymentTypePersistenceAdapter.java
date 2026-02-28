package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.PaymentTypePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.PaymentType;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.entity.PaymentTypeEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.mapper.PaymentTypePersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.repository.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PaymentTypePersistenceAdapter implements PaymentTypePersistencePort {

   private final PaymentTypeRepository repository;
   private final PaymentTypePersistenceMapper mapper;

   // ========================= CREATE/UPDATE =========================

   @Override
   public PaymentType save(PaymentType paymentType) {
      PaymentTypeEntity entity;

      if (paymentType.getId() != null) {
         entity = repository.findById(paymentType.getId())
               .orElseThrow(() -> new IllegalArgumentException(
                     "Tipo de pago no encontrado con ID: " + paymentType.getId()));
         mapper.updateEntity(entity, paymentType);
      } else {
         entity = mapper.toEntity(paymentType);
      }

      return mapper.toDomain(repository.save(entity));
   }

   // ========================= READ =========================

   @Override
   public Optional<PaymentType> findById(Long id) {
      return repository.findByIdAndDeletedAtIsNull(id)
            .map(mapper::toDomain);
   }

   @Override
   public Optional<PaymentType> findByCode(String code) {
      return repository.findByCodeAndDeletedAtIsNull(code)
            .map(mapper::toDomain);
   }

   // ========================= LIST =========================

   @Override
   public List<PaymentType> findAllActive() {
      return repository.findByStatusTrueAndDeletedAtIsNullOrderByIdAsc()
            .stream()
            .map(mapper::toDomain)
            .toList();
   }

   @Override
   public PageResult<PaymentType> findAll(PageRequest pageRequest, String search, Boolean status) {
      Pageable pageable = buildPageable(pageRequest);

      boolean hasSearch = search != null && !search.trim().isEmpty();
      boolean hasStatus = status != null;

      Page<PaymentTypeEntity> page;

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
      Optional<PaymentTypeEntity> existing = repository.findByCodeAndDeletedAtIsNull(code);
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