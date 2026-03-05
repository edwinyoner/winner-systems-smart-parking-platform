package com.winnersystems.smartparking.parking.application.service.infraction;

import com.winnersystems.smartparking.parking.application.dto.command.*;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.infraction.*;
import com.winnersystems.smartparking.parking.application.port.output.*;
import com.winnersystems.smartparking.parking.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para gestión de infracciones.
 *
 * Responsabilidades:
 * - Crear y actualizar infracciones
 * - Asignar multas
 * - Registrar pagos de multas
 * - Resolver infracciones
 * - Listar con filtros múltiples
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class InfractionService implements
      CreateInfractionUseCase,
      GetInfractionUseCase,
      UpdateInfractionUseCase,
      ListInfractionsUseCase,
      AssignFineUseCase,
      RecordFinePaymentUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final InfractionPersistencePort infractionPersistencePort;
   private final ParkingPersistencePort parkingPersistencePort;
   private final ZonePersistencePort zonePersistencePort;
   private final VehiclePersistencePort vehiclePersistencePort;
   private final CustomerPersistencePort customerPersistencePort;

   // ========================= CONSTRUCTOR =========================

   public InfractionService(
         InfractionPersistencePort infractionPersistencePort,
         ParkingPersistencePort parkingPersistencePort,
         ZonePersistencePort zonePersistencePort,
         VehiclePersistencePort vehiclePersistencePort,
         CustomerPersistencePort customerPersistencePort) {
      this.infractionPersistencePort = infractionPersistencePort;
      this.parkingPersistencePort = parkingPersistencePort;
      this.zonePersistencePort = zonePersistencePort;
      this.vehiclePersistencePort = vehiclePersistencePort;
      this.customerPersistencePort = customerPersistencePort;
   }

   // ========================= CreateInfractionUseCase =========================

   @Override
   public InfractionDto createInfraction(CreateInfractionCommand command) {
      // 1. Validar entidades relacionadas
      validateParking(command.parkingId());
      validateZone(command.zoneId());
      validateVehicle(command.vehicleId());

      // 2. Crear infracción
      Infraction infraction = new Infraction(
            command.parkingId(),
            command.vehicleId(),
            command.zoneId(),
            command.infractionType(),
            getCurrentUserId()
      );

      if (command.spaceId() != null) {
         infraction.setSpaceId(command.spaceId());
      }

      if (command.transactionId() != null) {
         infraction.setTransactionId(command.transactionId());
      }

      if (command.customerId() != null) {
         infraction.setCustomerId(command.customerId());
      }

      if (command.severity() != null) {
         infraction.setSeverity(command.severity());
      }

      if (command.description() != null) {
         infraction.setDescription(command.description());
      }

      if (command.evidence() != null) {
         infraction.attachEvidence(command.evidence());
      }

      infraction.setDetectionMethod(Infraction.METHOD_MANUAL);
      infraction.setCreatedBy(getCurrentUserId());

      Infraction saved = infractionPersistencePort.save(infraction);

      // 3. Generar código único (requiere ID de base de datos)
      saved.generateInfractionCode();
      saved = infractionPersistencePort.save(saved);

      return buildInfractionDto(saved);
   }

   // ========================= GetInfractionUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public InfractionDto getInfractionById(Long infractionId) {
      Infraction infraction = loadInfraction(infractionId);
      return buildInfractionDto(infraction);
   }

   @Override
   @Transactional(readOnly = true)
   public InfractionDto getInfractionByCode(String infractionCode) {
      return infractionPersistencePort.findByCode(infractionCode)
            .map(this::buildInfractionDto)
            .orElse(null);
   }

   // ========================= UpdateInfractionUseCase =========================

   @Override
   public InfractionDto updateInfraction(Long infractionId, UpdateInfractionCommand command) {
      Infraction infraction = loadInfraction(infractionId);

      // Validar que no esté resuelta
      if (infraction.isResolved()) {
         throw new IllegalStateException(
               String.format("No se puede actualizar la infracción #%d porque ya está resuelta",
                     infractionId)
         );
      }

      // Actualizar campos
      if (command.description() != null) {
         infraction.setDescription(command.description());
      }

      if (command.severity() != null) {
         infraction.setSeverity(command.severity());
      }

      if (command.evidence() != null) {
         infraction.attachEvidence(command.evidence());
      }

      if (command.notes() != null) {
         infraction.setNotes(command.notes());
      }

      infraction.updateModifiedBy(getCurrentUserId());

      Infraction saved = infractionPersistencePort.save(infraction);
      return buildInfractionDto(saved);
   }

   // ========================= ListInfractionsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listAllInfractions(int pageNumber, int pageSize,
                                                          String search, String status) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "detectedAt", "DESC");
      PageResult<Infraction> result = infractionPersistencePort.findAll(request, search, status);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listInfractionsByVehicle(Long vehicleId,
                                                                int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "detectedAt", "DESC");
      PageResult<Infraction> result = infractionPersistencePort.findByVehicle(vehicleId, request);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listInfractionsByCustomer(Long customerId,
                                                                 int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "detectedAt", "DESC");
      PageResult<Infraction> result = infractionPersistencePort.findByCustomer(customerId, request);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public List<InfractionDto> listInfractionsByTransaction(Long transactionId) {
      return infractionPersistencePort.findByTransaction(transactionId).stream()
            .map(this::buildInfractionDto)
            .toList();
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listInfractionsWithPendingFines(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "fineDueDate", "ASC");
      PageResult<Infraction> result = infractionPersistencePort.findWithPendingFines(request);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listInfractionsWithOverdueFines(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "fineDueDate", "ASC");
      PageResult<Infraction> result = infractionPersistencePort.findWithOverdueFines(request);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<InfractionDto> listInfractionsByDateRange(LocalDateTime startDate,
                                                                  LocalDateTime endDate,
                                                                  int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "detectedAt", "DESC");
      PageResult<Infraction> result = infractionPersistencePort
            .findByDetectionDateRange(startDate, endDate, request);
      return toInfractionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public List<InfractionDto> listAllPendingInfractions() {
      return infractionPersistencePort.findAllPending().stream()
            .map(this::buildInfractionDto)
            .toList();
   }

   // ========================= AssignFineUseCase =========================

   @Override
   public InfractionDto assignFine(Long infractionId, AssignFineCommand command) {
      Infraction infraction = loadInfraction(infractionId);

      // Validar estado
      if (infraction.isResolved()) {
         throw new IllegalStateException(
               String.format("No se puede asignar multa a la infracción #%d porque ya está resuelta",
                     infractionId)
         );
      }

      if (infraction.hasFine()) {
         throw new IllegalStateException(
               String.format("La infracción #%d ya tiene una multa asignada", infractionId)
         );
      }

      // Asignar multa
      if (command.fineDueDate() != null) {
         infraction.assignFine(command.fineAmount(), command.fineDueDate());
      } else {
         infraction.assignFine(command.fineAmount()); // 7 días por defecto
      }

      Infraction saved = infractionPersistencePort.save(infraction);
      return buildInfractionDto(saved);
   }

   // ========================= RecordFinePaymentUseCase =========================

   @Override
   public InfractionDto recordFinePayment(Long infractionId, RecordFinePaymentCommand command) {
      Infraction infraction = loadInfraction(infractionId);

      // Validar multa
      if (!infraction.hasFine()) {
         throw new IllegalStateException(
               String.format("La infracción #%d no tiene multa asignada", infractionId)
         );
      }

      if (infraction.isFinePaid()) {
         throw new IllegalStateException(
               String.format("La multa de la infracción #%d ya está pagada", infractionId)
         );
      }

      // Validar monto
      if (command.amount().compareTo(infraction.getFineAmount()) < 0) {
         throw new IllegalArgumentException(
               String.format("Monto insuficiente. Requerido: %s, recibido: %s",
                     infraction.getFineAmount(), command.amount())
         );
      }

      // Registrar pago
      infraction.recordFinePayment(command.amount(), command.reference());

      // Resolver automáticamente como PAID
      infraction.resolveAsPaid(getCurrentUserId(), "Multa pagada");

      Infraction saved = infractionPersistencePort.save(infraction);
      return buildInfractionDto(saved);
   }

   // ========================= HELPERS - CARGA =========================

   private Infraction loadInfraction(Long infractionId) {
      return infractionPersistencePort.findById(infractionId)
            .orElseThrow(() -> new IllegalArgumentException("Infracción no encontrada: " + infractionId));
   }

   // ========================= HELPERS - VALIDACIONES =========================

   private void validateParking(Long parkingId) {
      if (!parkingPersistencePort.findById(parkingId).isPresent()) {
         throw new IllegalArgumentException("Parking no encontrado: " + parkingId);
      }
   }

   private void validateZone(Long zoneId) {
      if (!zonePersistencePort.findById(zoneId).isPresent()) {
         throw new IllegalArgumentException("Zona no encontrada: " + zoneId);
      }
   }

   private void validateVehicle(Long vehicleId) {
      if (!vehiclePersistencePort.findById(vehicleId).isPresent()) {
         throw new IllegalArgumentException("Vehículo no encontrado: " + vehicleId);
      }
   }

   // ========================= BUILDERS - DTOs =========================

   private InfractionDto buildInfractionDto(Infraction i) {
      String parkingName = i.getParkingId() != null
            ? parkingPersistencePort.findById(i.getParkingId()).map(Parking::getName).orElse(null)
            : null;

      String zoneName = i.getZoneId() != null
            ? zonePersistencePort.findById(i.getZoneId()).map(Zone::getName).orElse(null)
            : null;

      String vehiclePlate = i.getVehicleId() != null
            ? vehiclePersistencePort.findById(i.getVehicleId()).map(Vehicle::getLicensePlate).orElse(null)
            : null;

      String customerName = i.getCustomerId() != null
            ? customerPersistencePort.findById(i.getCustomerId()).map(Customer::getFullName).orElse(null)
            : null;

      return new InfractionDto(
            i.getId(),
            i.getInfractionCode(),
            i.getParkingId(),
            parkingName,
            i.getZoneId(),
            zoneName,
            i.getSpaceId(),
            null, // spaceCode - TODO
            i.getTransactionId(),
            i.getVehicleId(),
            vehiclePlate,
            i.getCustomerId(),
            customerName,
            i.getInfractionType(),
            i.getSeverity(),
            i.getDescription(),
            i.getEvidence(),
            i.getDetectedAt(),
            i.getDetectedBy(),
            null, // detectedByName - TODO: auth-service
            i.getDetectionMethod(),
            i.getFineAmount(),
            i.getCurrency(),
            i.getFineDueDate(),
            i.getFinePaid(),
            i.getFinePaidAt(),
            i.getStatus(),
            i.getResolvedAt(),
            i.getResolutionType(),
            i.getResolution(),
            i.getNotificationSent(),
            i.getCreatedAt()
      );
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private PagedResponse<InfractionDto> toInfractionPagedResponse(PageResult<Infraction> result) {
      List<InfractionDto> content = result.content().stream()
            .map(this::buildInfractionDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   // ========================= HELPERS - UTILIDADES =========================

   private Long getCurrentUserId() {
      // TODO: Obtener desde SecurityContext
      return 1L;
   }
}