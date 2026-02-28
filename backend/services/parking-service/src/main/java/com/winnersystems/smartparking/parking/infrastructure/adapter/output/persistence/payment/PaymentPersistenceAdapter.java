package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment;

import com.winnersystems.smartparking.parking.application.port.output.PaymentPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Payment;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity.PaymentEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.mapper.PaymentPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

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

   @Override
   public Payment save(Payment payment) {
      PaymentEntity entity = paymentMapper.toEntity(payment);
      PaymentEntity savedEntity = paymentRepository.save(entity);
      return paymentMapper.toDomain(savedEntity);
   }

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
   public boolean existsByTransactionId(Long transactionId) {
      return paymentRepository.existsByTransactionId(transactionId);
   }
}