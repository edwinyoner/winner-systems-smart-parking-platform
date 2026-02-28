package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.rate.entity.RateEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para Rate.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface RateRepository extends JpaRepository<RateEntity, Long> {

   Optional<RateEntity> findByNameAndDeletedAtIsNull(String name);
   boolean existsByNameAndDeletedAtIsNull(String name);
   Optional<RateEntity> findByIdAndDeletedAtIsNull(Long id);
   List<RateEntity> findByStatusTrueAndDeletedAtIsNullOrderByIdAsc();
   List<RateEntity> findByDeletedAtIsNull(Sort sort);

   // ========================= PAGINACIÓN SIN FILTROS =========================
   Page<RateEntity> findByDeletedAtIsNull(Pageable pageable);

   // ========================= PAGINACIÓN CON FILTROS =========================
   // Solo search
   Page<RateEntity> findByDeletedAtIsNullAndNameContainingIgnoreCase(
         String name, Pageable pageable);
   // Solo status
   Page<RateEntity> findByDeletedAtIsNullAndStatus(
         boolean status, Pageable pageable);
   // Search + status
   Page<RateEntity> findByDeletedAtIsNullAndNameContainingIgnoreCaseAndStatus(
         String name, boolean status, Pageable pageable);
}