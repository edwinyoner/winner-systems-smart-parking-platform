package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.PaymentPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Payment;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity.PaymentEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.mapper.PaymentPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador de persistencia para Payment.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class PaymentPersistenceAdapter implements PaymentPersistencePort {

   private final PaymentRepository paymentRepository;
   private final PaymentPersistenceMapper paymentMapper;

   // ========================= WRITE =========================

   @Override
   public Payment save(Payment payment) {
      PaymentEntity entity = paymentMapper.toEntity(payment);
      PaymentEntity savedEntity = paymentRepository.save(entity);
      return paymentMapper.toDomain(savedEntity);
   }

   // ========================= FIND ÚNICO =========================

   @Override
   public Optional<Payment> findById(Long id) {
      return paymentRepository.findById(id)
            .map(paymentMapper::toDomain);
   }

   @Override
   public Optional<Payment> findByTransactionId(Long transactionId) {
      return paymentRepository.findByTransactionId(transactionId)
            .map(paymentMapper::toDomain);
   }

   @Override
   public Optional<Payment> findByReferenceNumber(String referenceNumber) {
      return paymentRepository.findByReferenceNumber(referenceNumber)
            .map(paymentMapper::toDomain);
   }

   // ========================= EXISTS =========================

   @Override
   public boolean existsByTransactionId(Long transactionId) {
      return paymentRepository.existsByTransactionId(transactionId);
   }

   @Override
   public boolean existsByReferenceNumber(String referenceNumber) {
      return paymentRepository.existsByReferenceNumber(referenceNumber);
   }

   // ========================= LIST (paginado) =========================

   @Override
   public PageResult<Payment> findAll(PageRequest pageRequest, String search, String status) {
      Pageable pageable = buildPageable(pageRequest);
      Page<PaymentEntity> page = paymentRepository.findAllWithFilters(search, status, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Payment> findByPaymentType(Long paymentTypeId, PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<PaymentEntity> page = paymentRepository.findByPaymentTypeId(paymentTypeId, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Payment> findByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                              PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<PaymentEntity> page = paymentRepository.findByDateRange(startDate, endDate, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Payment> findWithRefunds(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<PaymentEntity> page = paymentRepository.findWithRefunds(pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Payment> findByOperator(Long operatorId, PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<PaymentEntity> page = paymentRepository.findByOperatorId(operatorId, pageable);
      return toPageResult(page, pageRequest);
   }

   // ========================= LIST (sin paginación) =========================

   @Override
   public List<Payment> findAllCompleted() {
      return paymentRepository.findAllCompleted().stream()
            .map(paymentMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= COUNT =========================

   @Override
   public long count() {
      return paymentRepository.count();
   }

   @Override
   public long countByStatus(String status) {
      return paymentRepository.countByStatus(status);
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private Pageable buildPageable(PageRequest pageRequest) {
      if (pageRequest.hasSorting()) {
         org.springframework.data.domain.Sort.Direction direction =
               pageRequest.isAscending()
                     ? org.springframework.data.domain.Sort.Direction.ASC
                     : org.springframework.data.domain.Sort.Direction.DESC;

         return org.springframework.data.domain.PageRequest.of(
               pageRequest.page(),
               pageRequest.size(),
               org.springframework.data.domain.Sort.by(direction, pageRequest.sortBy())
         );
      }
      return org.springframework.data.domain.PageRequest.of(pageRequest.page(), pageRequest.size());
   }

   private PageResult<Payment> toPageResult(Page<PaymentEntity> page, PageRequest pageRequest) {
      List<Payment> content = page.getContent().stream()
            .map(paymentMapper::toDomain)
            .collect(Collectors.toList());

      return PageResult.of(content, pageRequest, page.getTotalElements());
   }
}