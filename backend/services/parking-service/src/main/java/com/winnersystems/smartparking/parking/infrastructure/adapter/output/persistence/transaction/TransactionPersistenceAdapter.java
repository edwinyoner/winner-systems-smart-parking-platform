package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.TransactionPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Transaction;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.entity.TransactionEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.mapper.TransactionPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Adaptador de persistencia para Transaction.
 *
 * Implementa TransactionPersistencePort usando Spring Data JPA.
 * Convierte entre:
 *   - PageRequest (custom, application layer) ↔ Pageable (Spring, infrastructure)
 *   - Page<Entity> (Spring)                   ↔ PageResult<Domain> (custom)
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class TransactionPersistenceAdapter implements TransactionPersistencePort {

   private final TransactionRepository transactionRepository;
   private final TransactionPersistenceMapper transactionMapper;

   // ========================= WRITE =========================

   @Override
   public Transaction save(Transaction transaction) {
      TransactionEntity entity = transactionMapper.toEntity(transaction);
      TransactionEntity saved = transactionRepository.save(entity);
      return transactionMapper.toDomain(saved);
   }

   @Override
   public void delete(Long id) {
      transactionRepository.deleteById(id);
   }

   // ========================= FIND ÚNICO =========================

   @Override
   public Optional<Transaction> findById(Long id) {
      return transactionRepository.findById(id)
            .map(transactionMapper::toDomain);
   }

   @Override
   public Optional<Transaction> findActiveByVehicleId(Long vehicleId) {
      return transactionRepository.findActiveByVehicleId(vehicleId)
            .map(transactionMapper::toDomain);
   }

   @Override
   public Optional<Transaction> findActiveByPlateNumber(String plateNumber) {
      return transactionRepository.findActiveByPlateNumber(plateNumber)
            .map(transactionMapper::toDomain);
   }

   // ========================= EXISTS / COUNT =========================

   @Override
   public boolean existsActiveByVehicleId(Long vehicleId) {
      return transactionRepository.existsActiveByVehicleId(vehicleId);
   }

   @Override
   public long countActiveByZoneId(Long zoneId) {
      return transactionRepository.countActiveByZoneId(zoneId);
   }

   // ========================= LIST ACTIVAS =========================

   @Override
   public PageResult<Transaction> findAllActive(PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findAllActive(toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findActiveByZoneId(Long zoneId, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findActiveByZoneId(zoneId, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> searchActiveByPlate(String plateNumber, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.searchActiveByPlate(plateNumber, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findOverdue(int maxMinutes, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findOverdue(maxMinutes, toPageable(pageRequest))
      );
   }

   // ========================= LIST HISTÓRICO =========================

   @Override
   public PageResult<Transaction> findAll(PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findAll(toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                  PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findByDateRange(startDate, endDate, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findByStatus(String status, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findByStatus(status, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findByPaymentStatus(String paymentStatus, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findByPaymentStatus(paymentStatus, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> findByZoneId(Long zoneId, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.findByZoneId(zoneId, toPageable(pageRequest))
      );
   }

   @Override
   public PageResult<Transaction> searchByPlate(String plateNumber, PageRequest pageRequest) {
      return toPageResult(
            transactionRepository.searchByPlate(plateNumber, toPageable(pageRequest))
      );
   }

   // ========================= CONVERSIÓN PRIVADA =========================

   /**
    * Convierte PageRequest (custom, application layer) a Pageable (Spring, infrastructure).
    *
    * El Repository usa Spring internamente — esta conversión mantiene
    * el dominio libre de dependencias de framework.
    */
   private org.springframework.data.domain.PageRequest toPageable(PageRequest req) {
      if (req.hasSorting()) {
         Sort sort = req.isDescending()
               ? Sort.by(req.sortBy()).descending()
               : Sort.by(req.sortBy()).ascending();
         return org.springframework.data.domain.PageRequest.of(req.page(), req.size(), sort);
      }
      return org.springframework.data.domain.PageRequest.of(req.page(), req.size());
   }

   /**
    * Convierte Page<TransactionEntity> (Spring) a PageResult<Transaction> (custom).
    *
    * Mapea cada entidad JPA al modelo de dominio y encapsula
    * la metadata de paginación en el tipo custom del proyecto.
    */
   private PageResult<Transaction> toPageResult(Page<TransactionEntity> page) {
      return PageResult.of(
            page.getContent().stream()
                  .map(transactionMapper::toDomain)
                  .toList(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
      );
   }
}