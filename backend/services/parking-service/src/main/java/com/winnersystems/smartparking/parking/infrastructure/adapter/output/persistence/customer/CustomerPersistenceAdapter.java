package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.port.output.CustomerPersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Customer;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.entity.CustomerEntity;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.mapper.CustomerPersistenceMapper;
import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

   // ========================= WRITE =========================

   @Override
   public Customer save(Customer customer) {
      CustomerEntity entity = customerMapper.toEntity(customer);
      CustomerEntity savedEntity = customerRepository.save(entity);
      return customerMapper.toDomain(savedEntity);
   }

   @Override
   public void delete(Long id) {
      customerRepository.deleteById(id);
   }

   // ========================= FIND ÚNICO =========================

   @Override
   public Optional<Customer> findById(Long id) {
      return customerRepository.findById(id)
            .map(customerMapper::toDomain);
   }

   @Override
   public Optional<Customer> findByDocument(Long documentTypeId, String documentNumber) {
      return customerRepository.findByDocumentTypeIdAndDocumentNumber(documentTypeId, documentNumber)
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

   // ========================= EXISTS =========================

   @Override
   public boolean existsByDocument(Long documentTypeId, String documentNumber) {
      return customerRepository.existsByDocument(documentTypeId, documentNumber);
   }

   @Override
   public boolean existsByEmail(String email) {
      return customerRepository.existsByEmail(email);
   }

   @Override
   public boolean existsByPhone(String phone) {
      return customerRepository.existsByPhone(phone);
   }

   // ========================= LIST (paginado) =========================

   @Override
   public PageResult<Customer> findAll(PageRequest pageRequest, String search, String status) {
      Pageable pageable = buildPageable(pageRequest);
      Page<CustomerEntity> page = customerRepository.findAllWithFilters(search, status, pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Customer> findRecurrent(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<CustomerEntity> page = customerRepository.findRecurrent(pageable);
      return toPageResult(page, pageRequest);
   }

   @Override
   public PageResult<Customer> findWithMobileAccount(PageRequest pageRequest) {
      Pageable pageable = buildPageable(pageRequest);
      Page<CustomerEntity> page = customerRepository.findWithMobileAccount(pageable);
      return toPageResult(page, pageRequest);
   }

   // ========================= LIST (sin paginación) =========================

   @Override
   public List<Customer> findAllActive() {
      return customerRepository.findAllActive().stream()
            .map(customerMapper::toDomain)
            .collect(Collectors.toList());
   }

   // ========================= COUNT =========================

   @Override
   public long count() {
      return customerRepository.count();
   }

   @Override
   public long countActive() {
      return customerRepository.countActive();
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

   private PageResult<Customer> toPageResult(Page<CustomerEntity> page, PageRequest pageRequest) {
      List<Customer> content = page.getContent().stream()
            .map(customerMapper::toDomain)
            .collect(Collectors.toList());

      return PageResult.of(content, pageRequest, page.getTotalElements());
   }
}