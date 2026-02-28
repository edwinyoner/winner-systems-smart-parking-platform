package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.mapper;

import com.winnersystems.smartparking.parking.domain.model.Transaction;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.transaction.entity.TransactionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Transaction (dominio) y TransactionEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class TransactionPersistenceMapper {

   /**
    * Convierte Transaction (dominio) a TransactionEntity (JPA).
    */
   public TransactionEntity toEntity(Transaction transaction) {
      if (transaction == null) {
         return null;
      }

      return TransactionEntity.builder()
            .id(transaction.getId())
            .vehicleId(transaction.getVehicleId())
            .customerId(transaction.getCustomerId())
            .parkingSpaceId(transaction.getParkingSpaceId())
            .zoneId(transaction.getZoneId())
            .rateId(transaction.getRateId())
            .entryDocumentTypeId(transaction.getEntryDocumentTypeId())
            .entryDocumentNumber(transaction.getEntryDocumentNumber())
            .exitDocumentTypeId(transaction.getExitDocumentTypeId())
            .exitDocumentNumber(transaction.getExitDocumentNumber())
            .entryTime(transaction.getEntryTime())
            .exitTime(transaction.getExitTime())
            .durationMinutes(transaction.getDurationMinutes())
            .entryOperatorId(transaction.getEntryOperatorId())
            .exitOperatorId(transaction.getExitOperatorId())
            .entryMethod(transaction.getEntryMethod())
            .exitMethod(transaction.getExitMethod())
            .entryPhotoUrl(transaction.getEntryPhotoUrl())
            .exitPhotoUrl(transaction.getExitPhotoUrl())
            .entryPlateConfidence(transaction.getEntryPlateConfidence())
            .exitPlateConfidence(transaction.getExitPlateConfidence())
            .calculatedAmount(transaction.getCalculatedAmount())
            .discountAmount(transaction.getDiscountAmount())
            .totalAmount(transaction.getTotalAmount())
            .currency(transaction.getCurrency())
            .status(transaction.getStatus())
            .paymentStatus(transaction.getPaymentStatus())
            .receiptSent(transaction.getReceiptSent())
            .receiptSentAt(transaction.getReceiptSentAt())
            .receiptWhatsAppStatus(transaction.getReceiptWhatsAppStatus())
            .receiptEmailStatus(transaction.getReceiptEmailStatus())
            .notes(transaction.getNotes())
            .cancellationReason(transaction.getCancellationReason())
            .createdAt(transaction.getCreatedAt())
            .createdBy(transaction.getCreatedBy())
            .updatedAt(transaction.getUpdatedAt())
            .updatedBy(transaction.getUpdatedBy())
            // ✅ FIX: campos de soft delete que faltaban
//            .deletedAt(transaction.getDeletedAt())
//            .deletedBy(transaction.getDeletedBy())
            .build();
   }

   /**
    * Convierte TransactionEntity (JPA) a Transaction (dominio).
    */
   public Transaction toDomain(TransactionEntity entity) {
      if (entity == null) {
         return null;
      }

      Transaction transaction = new Transaction();
      transaction.setId(entity.getId());
      transaction.setVehicleId(entity.getVehicleId());
      transaction.setCustomerId(entity.getCustomerId());
      transaction.setParkingSpaceId(entity.getParkingSpaceId());
      transaction.setZoneId(entity.getZoneId());
      transaction.setRateId(entity.getRateId());
      transaction.setEntryDocumentTypeId(entity.getEntryDocumentTypeId());
      transaction.setEntryDocumentNumber(entity.getEntryDocumentNumber());
      transaction.setExitDocumentTypeId(entity.getExitDocumentTypeId());
      transaction.setExitDocumentNumber(entity.getExitDocumentNumber());
      transaction.setEntryTime(entity.getEntryTime());
      transaction.setExitTime(entity.getExitTime());
      transaction.setDurationMinutes(entity.getDurationMinutes());
      transaction.setEntryOperatorId(entity.getEntryOperatorId());
      transaction.setExitOperatorId(entity.getExitOperatorId());
      transaction.setEntryMethod(entity.getEntryMethod());
      transaction.setExitMethod(entity.getExitMethod());
      transaction.setEntryPhotoUrl(entity.getEntryPhotoUrl());
      transaction.setExitPhotoUrl(entity.getExitPhotoUrl());
      transaction.setEntryPlateConfidence(entity.getEntryPlateConfidence());
      transaction.setExitPlateConfidence(entity.getExitPlateConfidence());
      transaction.setCalculatedAmount(entity.getCalculatedAmount());
      transaction.setDiscountAmount(entity.getDiscountAmount());
      transaction.setTotalAmount(entity.getTotalAmount());
      transaction.setCurrency(entity.getCurrency());
      transaction.setStatus(entity.getStatus());
      transaction.setPaymentStatus(entity.getPaymentStatus());
      transaction.setReceiptSent(entity.getReceiptSent());
      transaction.setReceiptSentAt(entity.getReceiptSentAt());
      transaction.setReceiptWhatsAppStatus(entity.getReceiptWhatsAppStatus());
      transaction.setReceiptEmailStatus(entity.getReceiptEmailStatus());
      transaction.setNotes(entity.getNotes());
      transaction.setCancellationReason(entity.getCancellationReason());
      transaction.setCreatedAt(entity.getCreatedAt());
      transaction.setCreatedBy(entity.getCreatedBy());
      transaction.setUpdatedAt(entity.getUpdatedAt());
      transaction.setUpdatedBy(entity.getUpdatedBy());
      // ✅ FIX: campos de soft delete que faltaban
//      transaction.setDeletedAt(entity.getDeletedAt());
//      transaction.setDeletedBy(entity.getDeletedBy());

      return transaction;
   }
}