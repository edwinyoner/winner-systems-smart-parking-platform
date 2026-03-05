package com.winnersystems.smartparking.parking.application.service.customer;

import com.winnersystems.smartparking.parking.application.dto.command.CreateCustomerCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdateCustomerCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.customer.*;
import com.winnersystems.smartparking.parking.application.port.output.CustomerPersistencePort;
import com.winnersystems.smartparking.parking.application.port.output.DocumentTypePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.Customer;
import com.winnersystems.smartparking.parking.domain.model.DocumentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de clientes/conductores.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class CustomerService implements
      CreateCustomerUseCase,
      GetCustomerUseCase,
      ListCustomersUseCase,
      UpdateCustomerUseCase,
      DeleteCustomerUseCase,
      RestoreCustomerUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final CustomerPersistencePort customerPersistencePort;
   private final DocumentTypePersistencePort documentTypePersistencePort;

   // ========================= CONSTRUCTOR =========================

   public CustomerService(
         CustomerPersistencePort customerPersistencePort,
         DocumentTypePersistencePort documentTypePersistencePort) {
      this.customerPersistencePort = customerPersistencePort;
      this.documentTypePersistencePort = documentTypePersistencePort;
   }

   // ========================= CreateCustomerUseCase =========================

   @Override
   public CustomerDto createCustomer(CreateCustomerCommand command) {
      // 1. Validar que no exista cliente con ese documento
      if (customerPersistencePort.existsByDocument(command.documentTypeId(), command.documentNumber())) {
         throw new IllegalArgumentException(
               "Ya existe un cliente con el documento: " + command.documentNumber()
         );
      }

      // 2. Validar que el tipo de documento exista
      DocumentType docType = documentTypePersistencePort.findById(command.documentTypeId())
            .orElseThrow(() -> new IllegalArgumentException("Tipo de documento no encontrado"));

      // 3. Crear cliente
      Customer customer = new Customer(
            command.documentTypeId(),
            command.documentNumber(),
            command.firstName(),
            command.lastName()
      );

      if (command.phone() != null) customer.setPhone(command.phone());
      if (command.email() != null) customer.setEmail(command.email());
      if (command.address() != null) customer.setAddress(command.address());

      customer.normalizeDocumentNumber();

      Customer saved = customerPersistencePort.save(customer);

      return buildCustomerDto(saved, docType.getName());
   }

   // ========================= GetCustomerUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public CustomerDto getCustomerById(Long customerId) {
      Customer customer = customerPersistencePort.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

      DocumentType docType = documentTypePersistencePort.findById(customer.getDocumentTypeId())
            .orElse(null);

      return buildCustomerDto(customer, docType != null ? docType.getName() : null);
   }

   @Override
   @Transactional(readOnly = true)
   public CustomerDto getCustomerByDocument(Long documentTypeId, String documentNumber) {
      return customerPersistencePort.findByDocument(documentTypeId, documentNumber)
            .map(customer -> {
               DocumentType docType = documentTypePersistencePort.findById(documentTypeId).orElse(null);
               return buildCustomerDto(customer, docType != null ? docType.getName() : null);
            })
            .orElse(null);
   }

   @Override
   @Transactional(readOnly = true)
   public CustomerDto getCustomerByEmail(String email) {
      return customerPersistencePort.findByEmail(email)
            .map(customer -> {
               DocumentType docType = documentTypePersistencePort.findById(customer.getDocumentTypeId())
                     .orElse(null);
               return buildCustomerDto(customer, docType != null ? docType.getName() : null);
            })
            .orElse(null);
   }

   @Override
   @Transactional(readOnly = true)
   public CustomerDto getCustomerByPhone(String phone) {
      return customerPersistencePort.findByPhone(phone)
            .map(customer -> {
               DocumentType docType = documentTypePersistencePort.findById(customer.getDocumentTypeId())
                     .orElse(null);
               return buildCustomerDto(customer, docType != null ? docType.getName() : null);
            })
            .orElse(null);
   }

   // ========================= ListCustomersUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<CustomerDto> listAllCustomers(int pageNumber, int pageSize,
                                                      String search, String status) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Customer> result = customerPersistencePort.findAll(req, search, status);
      return toCustomerPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public List<CustomerDto> listAllActiveCustomers() {
      List<Customer> customers = customerPersistencePort.findAllActive();
      return customers.stream()
            .map(c -> {
               DocumentType docType = documentTypePersistencePort.findById(c.getDocumentTypeId())
                     .orElse(null);
               return buildCustomerDto(c, docType != null ? docType.getName() : null);
            })
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<CustomerDto> listRecurrentCustomers(int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "totalVisits", "DESC");
      PageResult<Customer> result = customerPersistencePort.findRecurrent(req);
      return toCustomerPagedResponse(result);
   }

   // ========================= UpdateCustomerUseCase =========================

   @Override
   public CustomerDto updateCustomer(Long customerId, UpdateCustomerCommand command) {
      Customer customer = customerPersistencePort.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

      if (customer.isDeleted()) {
         throw new IllegalStateException("No se puede actualizar un cliente eliminado");
      }

      customer.updatePersonalInfo(command.firstName(), command.lastName());
      customer.updateContactInfo(command.phone(), command.email(), command.address());

      Customer updated = customerPersistencePort.save(customer);

      DocumentType docType = documentTypePersistencePort.findById(updated.getDocumentTypeId())
            .orElse(null);

      return buildCustomerDto(updated, docType != null ? docType.getName() : null);
   }

   // ========================= DeleteCustomerUseCase =========================

   @Override
   public void deleteCustomer(Long customerId, Long deletedBy) {
      Customer customer = customerPersistencePort.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

      customer.markAsDeleted(deletedBy);
      customerPersistencePort.save(customer);
   }

   // ========================= RestoreCustomerUseCase =========================

   @Override
   public CustomerDto restoreCustomer(Long customerId) {
      Customer customer = customerPersistencePort.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));

      if (!customer.isDeleted()) {
         throw new IllegalStateException("El cliente no está eliminado");
      }

      customer.restore();
      Customer restored = customerPersistencePort.save(customer);

      DocumentType docType = documentTypePersistencePort.findById(restored.getDocumentTypeId())
            .orElse(null);

      return buildCustomerDto(restored, docType != null ? docType.getName() : null);
   }

   // ========================= BUILDER PRIVADO — RECORD =========================

   /**
    * Construye CustomerDto (record) con todos los datos.
    */
   private CustomerDto buildCustomerDto(Customer c, String documentTypeName) {
      return new CustomerDto(
            c.getId(),
            c.getDocumentTypeId(),
            documentTypeName,
            c.getDocumentNumber(),
            c.getFirstName(),
            c.getLastName(),
            c.getFullName(),
            c.getPhone(),
            c.getEmail(),
            c.getAddress(),
            c.getRegistrationDate(),
            c.getFirstSeenDate(),
            c.getLastSeenDate(),
            c.getTotalVisits(),
            c.isRecurrent(),
            c.getAuthExternalId(),
            c.hasMobileAccount(),
            c.getCreatedAt(),
            c.getUpdatedAt()
      );
   }

   // ========================= HELPER DE PAGINACIÓN =========================

   private PagedResponse<CustomerDto> toCustomerPagedResponse(PageResult<Customer> result) {
      List<CustomerDto> content = result.content().stream()
            .map(c -> {
               DocumentType docType = documentTypePersistencePort.findById(c.getDocumentTypeId())
                     .orElse(null);
               return buildCustomerDto(c, docType != null ? docType.getName() : null);
            })
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }
}