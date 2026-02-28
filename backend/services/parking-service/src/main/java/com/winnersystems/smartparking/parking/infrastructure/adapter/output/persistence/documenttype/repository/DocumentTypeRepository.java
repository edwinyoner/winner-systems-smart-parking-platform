package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.documenttype.entity.DocumentTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentTypeRepository extends JpaRepository<DocumentTypeEntity, Long> {

   Optional<DocumentTypeEntity> findByIdAndDeletedAtIsNull(Long id);
   Optional<DocumentTypeEntity> findByCodeAndDeletedAtIsNull(String code);
   boolean existsByCodeAndDeletedAtIsNull(String code);

   List<DocumentTypeEntity> findByStatusTrueAndDeletedAtIsNullOrderByIdAsc();
   List<DocumentTypeEntity> findByDeletedAtIsNull(Sort sort);

   // ========================= PAGINACIÓN SIN FILTROS =========================
   Page<DocumentTypeEntity> findByDeletedAtIsNull(Pageable pageable);

   // ========================= PAGINACIÓN CON FILTROS =========================
   Page<DocumentTypeEntity> findByDeletedAtIsNullAndNameContainingIgnoreCase(
         String name, Pageable pageable);

   Page<DocumentTypeEntity> findByDeletedAtIsNullAndStatus(
         boolean status, Pageable pageable);

   Page<DocumentTypeEntity> findByDeletedAtIsNullAndNameContainingIgnoreCaseAndStatus(
         String name, boolean status, Pageable pageable);
}