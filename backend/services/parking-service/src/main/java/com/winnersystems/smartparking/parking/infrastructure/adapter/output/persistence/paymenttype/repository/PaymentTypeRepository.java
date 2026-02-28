package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.paymenttype.entity.PaymentTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentTypeRepository extends JpaRepository<PaymentTypeEntity, Long> {

   Optional<PaymentTypeEntity> findByIdAndDeletedAtIsNull(Long id);
   Optional<PaymentTypeEntity> findByCodeAndDeletedAtIsNull(String code);
   boolean existsByCodeAndDeletedAtIsNull(String code);

   List<PaymentTypeEntity> findByStatusTrueAndDeletedAtIsNullOrderByIdAsc();
   List<PaymentTypeEntity> findByDeletedAtIsNull(Sort sort);

   // ========================= PAGINACIÓN SIN FILTROS =========================
   Page<PaymentTypeEntity> findByDeletedAtIsNull(Pageable pageable);

   // ========================= PAGINACIÓN CON FILTROS =========================
   Page<PaymentTypeEntity> findByDeletedAtIsNullAndNameContainingIgnoreCase(
         String name, Pageable pageable);

   Page<PaymentTypeEntity> findByDeletedAtIsNullAndStatus(
         boolean status, Pageable pageable);

   Page<PaymentTypeEntity> findByDeletedAtIsNullAndNameContainingIgnoreCaseAndStatus(
         String name, boolean status, Pageable pageable);
}