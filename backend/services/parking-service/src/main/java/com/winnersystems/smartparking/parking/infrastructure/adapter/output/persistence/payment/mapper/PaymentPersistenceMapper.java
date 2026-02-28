package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.mapper;

import com.winnersystems.smartparking.parking.domain.model.Payment;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.payment.entity.PaymentEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Payment (dominio) y PaymentEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class PaymentPersistenceMapper {

   public PaymentEntity toEntity(Payment payment) {
      if (payment == null) {
         return null;
      }

      return PaymentEntity.builder()
            .id(payment.getId())
            .transactionId(payment.getTransactionId())
            .paymentTypeId(payment.getPaymentTypeId())
            .amount(payment.getAmount())
            .currency(payment.getCurrency())
            .paymentDate(payment.getPaymentDate())
            .referenceNumber(payment.getReferenceNumber())
            .operatorId(payment.getOperatorId())
            .status(payment.getStatus())
            .refundAmount(payment.getRefundAmount())
            .refundDate(payment.getRefundDate())
            .refundReason(payment.getRefundReason())
            .refundOperatorId(payment.getRefundOperatorId())
            .notes(payment.getNotes())
            .createdAt(payment.getCreatedAt())
            .createdBy(payment.getCreatedBy())
            .updatedAt(payment.getUpdatedAt())
            .updatedBy(payment.getUpdatedBy())
            .build();
   }

   public Payment toDomain(PaymentEntity entity) {
      if (entity == null) {
         return null;
      }

      Payment payment = new Payment();
      payment.setId(entity.getId());
      payment.setTransactionId(entity.getTransactionId());
      payment.setPaymentTypeId(entity.getPaymentTypeId());
      payment.setAmount(entity.getAmount());
      payment.setCurrency(entity.getCurrency());
      payment.setPaymentDate(entity.getPaymentDate());
      payment.setReferenceNumber(entity.getReferenceNumber());
      payment.setOperatorId(entity.getOperatorId());
      payment.setStatus(entity.getStatus());
      payment.setRefundAmount(entity.getRefundAmount());
      payment.setRefundDate(entity.getRefundDate());
      payment.setRefundReason(entity.getRefundReason());
      payment.setRefundOperatorId(entity.getRefundOperatorId());
      payment.setNotes(entity.getNotes());
      payment.setCreatedAt(entity.getCreatedAt());
      payment.setCreatedBy(entity.getCreatedBy());
      payment.setUpdatedAt(entity.getUpdatedAt());
      payment.setUpdatedBy(entity.getUpdatedBy());

      return payment;
   }
}