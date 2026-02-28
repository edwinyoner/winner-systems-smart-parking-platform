package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.mapper;

import com.winnersystems.smartparking.parking.domain.model.Customer;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper entre Customer (dominio) y CustomerEntity (JPA).
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
public class CustomerPersistenceMapper {

   public CustomerEntity toEntity(Customer customer) {
      if (customer == null) {
         return null;
      }

      return CustomerEntity.builder()
            .id(customer.getId())
            .documentTypeId(customer.getDocumentTypeId())
            .documentNumber(customer.getDocumentNumber())
            .firstName(customer.getFirstName())
            .lastName(customer.getLastName())
            .phone(customer.getPhone())
            .email(customer.getEmail())
            .address(customer.getAddress())
            .registrationDate(customer.getRegistrationDate())
            .firstSeenDate(customer.getFirstSeenDate())
            .lastSeenDate(customer.getLastSeenDate())
            .totalVisits(customer.getTotalVisits())
            .authExternalId(customer.getAuthExternalId())
            .createdAt(customer.getCreatedAt())
            .createdBy(customer.getCreatedBy())
            .updatedAt(customer.getUpdatedAt())
            .updatedBy(customer.getUpdatedBy())
            .deletedAt(customer.getDeletedAt())
            .deletedBy(customer.getDeletedBy())
            .build();
   }

   public Customer toDomain(CustomerEntity entity) {
      if (entity == null) {
         return null;
      }

      Customer customer = new Customer();
      customer.setId(entity.getId());
      customer.setDocumentTypeId(entity.getDocumentTypeId());
      customer.setDocumentNumber(entity.getDocumentNumber());
      customer.setFirstName(entity.getFirstName());
      customer.setLastName(entity.getLastName());
      customer.setPhone(entity.getPhone());
      customer.setEmail(entity.getEmail());
      customer.setAddress(entity.getAddress());
      customer.setRegistrationDate(entity.getRegistrationDate());
      customer.setFirstSeenDate(entity.getFirstSeenDate());
      customer.setLastSeenDate(entity.getLastSeenDate());
      customer.setTotalVisits(entity.getTotalVisits());
      customer.setAuthExternalId(entity.getAuthExternalId());
      customer.setCreatedAt(entity.getCreatedAt());
      customer.setCreatedBy(entity.getCreatedBy());
      customer.setUpdatedAt(entity.getUpdatedAt());
      customer.setUpdatedBy(entity.getUpdatedBy());
      customer.setDeletedAt(entity.getDeletedAt());
      customer.setDeletedBy(entity.getDeletedBy());

      return customer;
   }
}