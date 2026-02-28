package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer;

import com.winnersystems.smartparking.parking.application.port.output.CustomerPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Customer;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.mapper.CustomerPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adaptador de persistencia para Customer.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class CustomerPersistenceAdapter implements CustomerPersistencePort {

   private final CustomerRepository customerRepository;
   private final CustomerPersistenceMapper customerMapper;

   @Override
   public Customer save(Customer customer) {
      CustomerEntity entity = customerMapper.toEntity(customer);
      CustomerEntity savedEntity = customerRepository.save(entity);
      return customerMapper.toDomain(savedEntity);
   }

   @Override
   public Optional<Customer> findById(Long id) {
      return customerRepository.findById(id)
            .map(customerMapper::toDomain);
   }

   @Override
   public Optional<Customer> findByDocument(Long documentTypeId, String documentNumber) {
      return customerRepository.findByDocument(documentTypeId, documentNumber)
            .map(customerMapper::toDomain);
   }

   @Override
   public Optional<Customer> findByEmail(String email) {
      return customerRepository.findByEmail(email)
            .map(customerMapper::toDomain);
   }

   @Override
   public Optional<Customer> findByPhone(String phone) {
      return customerRepository.findByPhone(phone)
            .map(customerMapper::toDomain);
   }
}