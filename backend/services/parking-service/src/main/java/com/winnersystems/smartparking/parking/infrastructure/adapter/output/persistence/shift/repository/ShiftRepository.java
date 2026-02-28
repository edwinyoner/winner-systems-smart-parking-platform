package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.shift.entity.ShiftEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Shift.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface ShiftRepository extends JpaRepository<ShiftEntity, Long> {

   Optional<ShiftEntity> findByCodeAndDeletedAtIsNull(String code);
   boolean existsByCodeAndDeletedAtIsNull(String code);
   Optional<ShiftEntity> findByIdAndDeletedAtIsNull(Long id);
   List<ShiftEntity> findByStatusTrueAndDeletedAtIsNullOrderByIdAsc();
   List<ShiftEntity> findByDeletedAtIsNull(Sort sort);

   // ========================= PAGINACIÓN SIN FILTROS =========================
   Page<ShiftEntity> findByDeletedAtIsNull(Pageable pageable);

   // ========================= PAGINACIÓN CON FILTROS =========================
   // Solo search (busca en name y code)
   Page<ShiftEntity> findByDeletedAtIsNullAndNameContainingIgnoreCase(
         String name, Pageable pageable);
   // Solo status
   Page<ShiftEntity> findByDeletedAtIsNullAndStatus(
         boolean status, Pageable pageable);
   // Search + status
   Page<ShiftEntity> findByDeletedAtIsNullAndNameContainingIgnoreCaseAndStatus(
         String name, boolean status, Pageable pageable);
}