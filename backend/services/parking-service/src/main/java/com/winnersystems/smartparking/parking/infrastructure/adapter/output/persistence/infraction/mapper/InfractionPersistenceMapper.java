package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.mapper;

import com.winnersystems.smartparking.parking.domain.model.Infraction;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.infraction.entity.InfractionEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper bidireccional entre Infraction (dominio) y InfractionEntity (persistencia).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class InfractionPersistenceMapper {

   /**
    * Convierte Infraction (dominio) → InfractionEntity (JPA).
    */
   public InfractionEntity toEntity(Infraction infraction) {
      if (infraction == null) {
         return null;
      }

      InfractionEntity entity = new InfractionEntity();

      // IDs
      entity.setId(infraction.getId());
      entity.setInfractionCode(infraction.getInfractionCode());

      // Relaciones
      entity.setParkingId(infraction.getParkingId());
      entity.setZoneId(infraction.getZoneId());
      entity.setSpaceId(infraction.getSpaceId());
      entity.setTransactionId(infraction.getTransactionId());
      entity.setVehicleId(infraction.getVehicleId());
      entity.setCustomerId(infraction.getCustomerId());

      // Infracción
      entity.setInfractionType(infraction.getInfractionType());
      entity.setSeverity(infraction.getSeverity());
      entity.setDetectedAt(infraction.getDetectedAt());
      entity.setDetectedBy(infraction.getDetectedBy());
      entity.setDetectionMethod(infraction.getDetectionMethod());
      entity.setDescription(infraction.getDescription());
      entity.setEvidence(infraction.getEvidence());

      // Multa
      entity.setFineAmount(infraction.getFineAmount());
      entity.setCurrency(infraction.getCurrency());
      entity.setFineDueDate(infraction.getFineDueDate());

      // Resolución
      entity.setStatus(infraction.getStatus());
      entity.setResolvedAt(infraction.getResolvedAt());
      entity.setResolvedBy(infraction.getResolvedBy());
      entity.setResolution(infraction.getResolution());
      entity.setResolutionType(infraction.getResolutionType());

      // Pago de multa
      entity.setFinePaid(infraction.getFinePaid());
      entity.setFinePaidAt(infraction.getFinePaidAt());
      entity.setFinePaidAmount(infraction.getFinePaidAmount());
      entity.setFinePaymentReference(infraction.getFinePaymentReference());

      // Notificación
      entity.setNotificationSent(infraction.getNotificationSent());
      entity.setNotificationSentAt(infraction.getNotificationSentAt());
      entity.setNotificationMethod(infraction.getNotificationMethod());

      // Observaciones
      entity.setNotes(infraction.getNotes());

      // Auditoría
      entity.setCreatedAt(infraction.getCreatedAt());
      entity.setCreatedBy(infraction.getCreatedBy());
      entity.setUpdatedAt(infraction.getUpdatedAt());
      entity.setUpdatedBy(infraction.getUpdatedBy());
      entity.setDeletedAt(infraction.getDeletedAt());
      entity.setDeletedBy(infraction.getDeletedBy());

      return entity;
   }

   /**
    * Convierte InfractionEntity (JPA) → Infraction (dominio).
    */
   public Infraction toDomain(InfractionEntity entity) {
      if (entity == null) {
         return null;
      }

      Infraction infraction = new Infraction();

      // IDs
      infraction.setId(entity.getId());
      infraction.setInfractionCode(entity.getInfractionCode());

      // Relaciones
      infraction.setParkingId(entity.getParkingId());
      infraction.setZoneId(entity.getZoneId());
      infraction.setSpaceId(entity.getSpaceId());
      infraction.setTransactionId(entity.getTransactionId());
      infraction.setVehicleId(entity.getVehicleId());
      infraction.setCustomerId(entity.getCustomerId());

      // Infracción
      infraction.setInfractionType(entity.getInfractionType());
      infraction.setSeverity(entity.getSeverity());
      infraction.setDetectedAt(entity.getDetectedAt());
      infraction.setDetectedBy(entity.getDetectedBy());
      infraction.setDetectionMethod(entity.getDetectionMethod());
      infraction.setDescription(entity.getDescription());
      infraction.setEvidence(entity.getEvidence());

      // Multa
      infraction.setFineAmount(entity.getFineAmount());
      infraction.setCurrency(entity.getCurrency());
      infraction.setFineDueDate(entity.getFineDueDate());

      // Resolución
      infraction.setStatus(entity.getStatus());
      infraction.setResolvedAt(entity.getResolvedAt());
      infraction.setResolvedBy(entity.getResolvedBy());
      infraction.setResolution(entity.getResolution());
      infraction.setResolutionType(entity.getResolutionType());

      // Pago de multa
      infraction.setFinePaid(entity.getFinePaid());
      infraction.setFinePaidAt(entity.getFinePaidAt());
      infraction.setFinePaidAmount(entity.getFinePaidAmount());
      infraction.setFinePaymentReference(entity.getFinePaymentReference());

      // Notificación
      infraction.setNotificationSent(entity.getNotificationSent());
      infraction.setNotificationSentAt(entity.getNotificationSentAt());
      infraction.setNotificationMethod(entity.getNotificationMethod());

      // Observaciones
      infraction.setNotes(entity.getNotes());

      // Auditoría
      infraction.setCreatedAt(entity.getCreatedAt());
      infraction.setCreatedBy(entity.getCreatedBy());
      infraction.setUpdatedAt(entity.getUpdatedAt());
      infraction.setUpdatedBy(entity.getUpdatedBy());
      infraction.setDeletedAt(entity.getDeletedAt());
      infraction.setDeletedBy(entity.getDeletedBy());

      return infraction;
   }
}