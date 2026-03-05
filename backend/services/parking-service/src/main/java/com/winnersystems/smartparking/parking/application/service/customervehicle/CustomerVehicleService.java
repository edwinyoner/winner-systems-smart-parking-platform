package com.winnersystems.smartparking.parking.application.service.customervehicle;

import com.winnersystems.smartparking.parking.application.dto.command.CreateCustomerVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.customervehicle.*;
import com.winnersystems.smartparking.parking.application.port.output.*;
import com.winnersystems.smartparking.parking.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de aplicación para gestión de relaciones Cliente-Vehículo.
 *
 * Responsabilidades:
 * - Registrar relaciones cliente-vehículo
 * - Listar vehículos por cliente
 * - Listar clientes por vehículo
 * - Identificar combinaciones frecuentes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class CustomerVehicleService implements
      CreateCustomerVehicleUseCase,
      GetCustomerVehicleUseCase,
      ListCustomerVehiclesUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final CustomerVehiclePersistencePort customerVehiclePersistencePort;
   private final CustomerPersistencePort customerPersistencePort;
   private final VehiclePersistencePort vehiclePersistencePort;

   // ========================= CONSTRUCTOR =========================

   public CustomerVehicleService(
         CustomerVehiclePersistencePort customerVehiclePersistencePort,
         CustomerPersistencePort customerPersistencePort,
         VehiclePersistencePort vehiclePersistencePort) {
      this.customerVehiclePersistencePort = customerVehiclePersistencePort;
      this.customerPersistencePort = customerPersistencePort;
      this.vehiclePersistencePort = vehiclePersistencePort;
   }

   // ========================= CreateCustomerVehicleUseCase =========================

   @Override
   public CustomerVehicleDto createCustomerVehicle(CreateCustomerVehicleCommand command) {
      // 1. Validar que existan customer y vehicle
      validateCustomer(command.customerId());
      validateVehicle(command.vehicleId());

      // 2. Buscar si ya existe la relación
      return customerVehiclePersistencePort
            .findByCustomerAndVehicle(command.customerId(), command.vehicleId())
            .map(existing -> {
               // Ya existe, incrementar usageCount
               existing.incrementUsage();
               CustomerVehicle updated = customerVehiclePersistencePort.save(existing);
               return buildCustomerVehicleDto(updated);
            })
            .orElseGet(() -> {
               // No existe, crear nueva relación
               CustomerVehicle relation = new CustomerVehicle(
                     command.customerId(),
                     command.vehicleId()
               );
               relation.incrementUsage(); // Inicializa en 1
               relation.setCreatedBy(getCurrentUserId());

               CustomerVehicle saved = customerVehiclePersistencePort.save(relation);
               return buildCustomerVehicleDto(saved);
            });
   }

   // ========================= GetCustomerVehicleUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public CustomerVehicleDto getCustomerVehicle(Long customerId, Long vehicleId) {
      return customerVehiclePersistencePort.findByCustomerAndVehicle(customerId, vehicleId)
            .map(this::buildCustomerVehicleDto)
            .orElse(null);
   }

   @Override
   @Transactional(readOnly = true)
   public boolean existsCustomerVehicle(Long customerId, Long vehicleId) {
      return customerVehiclePersistencePort.existsByCustomerAndVehicle(customerId, vehicleId);
   }

   // ========================= ListCustomerVehiclesUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public List<CustomerVehicleDto> listVehiclesByCustomer(Long customerId) {
      return customerVehiclePersistencePort.findByCustomerId(customerId).stream()
            .map(this::buildCustomerVehicleDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public List<CustomerVehicleDto> listCustomersByVehicle(Long vehicleId) {
      return customerVehiclePersistencePort.findByVehicleId(vehicleId).stream()
            .map(this::buildCustomerVehicleDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<CustomerVehicleDto> listFrequentCombinations(int minUsageCount,
                                                                     int pageNumber,
                                                                     int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "usageCount", "DESC");
      PageResult<CustomerVehicle> result = customerVehiclePersistencePort
            .findFrequentCombinations(minUsageCount, request);
      return toCustomerVehiclePagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<CustomerVehicleDto> listAllCustomerVehicles(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<CustomerVehicle> result = customerVehiclePersistencePort.findAll(request);
      return toCustomerVehiclePagedResponse(result);
   }

   // ========================= HELPERS - VALIDACIONES =========================

   private void validateCustomer(Long customerId) {
      if (!customerPersistencePort.findById(customerId).isPresent()) {
         throw new IllegalArgumentException("Cliente no encontrado: " + customerId);
      }
   }

   private void validateVehicle(Long vehicleId) {
      if (!vehiclePersistencePort.findById(vehicleId).isPresent()) {
         throw new IllegalArgumentException("Vehículo no encontrado: " + vehicleId);
      }
   }

   // ========================= BUILDERS - DTOs =========================

   private CustomerVehicleDto buildCustomerVehicleDto(CustomerVehicle cv) {
      String customerName = cv.getCustomerId() != null
            ? customerPersistencePort.findById(cv.getCustomerId())
            .map(Customer::getFullName)
            .orElse(null)
            : null;

      String customerDocument = cv.getCustomerId() != null
            ? customerPersistencePort.findById(cv.getCustomerId())
            .map(Customer::getDocumentNumber)
            .orElse(null)
            : null;

      String vehiclePlate = cv.getVehicleId() != null
            ? vehiclePersistencePort.findById(cv.getVehicleId())
            .map(Vehicle::getLicensePlate)
            .orElse(null)
            : null;

      return new CustomerVehicleDto(
            cv.getId(),
            cv.getCustomerId(),
            customerName,
            customerDocument,
            cv.getVehicleId(),
            vehiclePlate,
            cv.getCreatedAt(),
            cv.getCreatedBy()
      );
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private PagedResponse<CustomerVehicleDto> toCustomerVehiclePagedResponse(
         PageResult<CustomerVehicle> result) {
      List<CustomerVehicleDto> content = result.content().stream()
            .map(this::buildCustomerVehicleDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   // ========================= HELPERS - UTILIDADES =========================

   private Long getCurrentUserId() {
      // TODO: Obtener desde SecurityContext
      return 1L;
   }
}