package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.mapper;

import com.winnersystems.smartparking.parking.domain.model.Customer;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper bidireccional entre Customer (dominio) y CustomerEntity (persistencia).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class CustomerPersistenceMapper {

   /**
    * Convierte Customer (dominio) → CustomerEntity (JPA).
    */
   public CustomerEntity toEntity(Customer customer) {
      if (customer == null) {
         return null;
      }

      CustomerEntity entity = new CustomerEntity();

      // IDs
      entity.setId(customer.getId());
      entity.setDocumentTypeId(customer.getDocumentTypeId());
      entity.setDocumentNumber(customer.getDocumentNumber());

      // Datos personales
      entity.setFirstName(customer.getFirstName());
      entity.setLastName(customer.getLastName());

      // Contacto
      entity.setPhone(customer.getPhone());
      entity.setEmail(customer.getEmail());
      entity.setAddress(customer.getAddress());

      // Tracking
      entity.setRegistrationDate(customer.getRegistrationDate());
      entity.setFirstSeenDate(customer.getFirstSeenDate());
      entity.setLastSeenDate(customer.getLastSeenDate());
      entity.setTotalVisits(customer.getTotalVisits());

      // App móvil
      entity.setAuthExternalId(customer.getAuthExternalId());

      // Auditoría
      entity.setCreatedAt(customer.getCreatedAt());
      entity.setCreatedBy(customer.getCreatedBy());
      entity.setUpdatedAt(customer.getUpdatedAt());
      entity.setUpdatedBy(customer.getUpdatedBy());
      entity.setDeletedAt(customer.getDeletedAt());
      entity.setDeletedBy(customer.getDeletedBy());

      return entity;
   }

   /**
    * Convierte CustomerEntity (JPA) → Customer (dominio).
    */
   public Customer toDomain(CustomerEntity entity) {
      if (entity == null) {
         return null;
      }

      Customer customer = new Customer();

      // IDs
      customer.setId(entity.getId());
      customer.setDocumentTypeId(entity.getDocumentTypeId());
      customer.setDocumentNumber(entity.getDocumentNumber());

      // Datos personales
      customer.setFirstName(entity.getFirstName());
      customer.setLastName(entity.getLastName());

      // Contacto
      customer.setPhone(entity.getPhone());
      customer.setEmail(entity.getEmail());
      customer.setAddress(entity.getAddress());

      // Tracking
      customer.setRegistrationDate(entity.getRegistrationDate());
      customer.setFirstSeenDate(entity.getFirstSeenDate());
      customer.setLastSeenDate(entity.getLastSeenDate());
      customer.setTotalVisits(entity.getTotalVisits());

      // App móvil
      customer.setAuthExternalId(entity.getAuthExternalId());

      // Auditoría
      customer.setCreatedAt(entity.getCreatedAt());
      customer.setCreatedBy(entity.getCreatedBy());
      customer.setUpdatedAt(entity.getUpdatedAt());
      customer.setUpdatedBy(entity.getUpdatedBy());
      customer.setDeletedAt(entity.getDeletedAt());
      customer.setDeletedBy(entity.getDeletedBy());

      return customer;
   }
}