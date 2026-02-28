package com.winnersystems.smartparking.parking.application.service.transaction;

import com.winnersystems.smartparking.parking.application.dto.command.ProcessPaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.command.RecordEntryCommand;
import com.winnersystems.smartparking.parking.application.dto.command.RecordExitCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.transaction.*;
import com.winnersystems.smartparking.parking.application.port.output.*;
import com.winnersystems.smartparking.parking.domain.exception.*;
import com.winnersystems.smartparking.parking.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para gestión de transacciones de estacionamiento.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class TransactionService implements
      RecordEntryUseCase,
      RecordExitUseCase,
      ProcessPaymentUseCase,
      GetTransactionUseCase,
      ListActiveTransactionsUseCase,
      ListTransactionsUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final TransactionPersistencePort transactionPersistencePort;
   private final VehiclePersistencePort vehiclePersistencePort;
   private final CustomerPersistencePort customerPersistencePort;
   private final SpacePersistencePort spacePersistencePort;
   private final ZonePersistencePort zonePersistencePort;
   private final RatePersistencePort ratePersistencePort;
   private final PaymentPersistencePort paymentPersistencePort;

   // ========================= CONSTANTES =========================

   private static final int MAX_RECOMMENDED_MINUTES = 480; // 8 horas — TODO: hacer configurable

   // ========================= CONSTRUCTOR =========================

   public TransactionService(
         TransactionPersistencePort transactionPersistencePort,
         VehiclePersistencePort vehiclePersistencePort,
         CustomerPersistencePort customerPersistencePort,
         SpacePersistencePort spacePersistencePort,
         ZonePersistencePort zonePersistencePort,
         RatePersistencePort ratePersistencePort,
         PaymentPersistencePort paymentPersistencePort) {
      this.transactionPersistencePort = transactionPersistencePort;
      this.vehiclePersistencePort = vehiclePersistencePort;
      this.customerPersistencePort = customerPersistencePort;
      this.spacePersistencePort = spacePersistencePort;
      this.zonePersistencePort = zonePersistencePort;
      this.ratePersistencePort = ratePersistencePort;
      this.paymentPersistencePort = paymentPersistencePort;
   }

   // ========================= RecordEntryUseCase =========================

   @Override
   public TransactionDetailDto recordEntry(RecordEntryCommand command) {
      // 1. Validar zona
      Zone zone = zonePersistencePort.findById(command.getZoneId())
            .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada"));
      if (!zone.isOperational()) {
         throw new ZoneNotOperationalException(zone.getName(), "La zona no está operativa");
      }

      // 2. Validar espacio
      Space space = spacePersistencePort.findById(command.getSpaceId())
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
      if (!space.isAvailable()) {
         throw new SpaceNotAvailableException(space.getCode(), space.getStatus());
      }

      // 3. Buscar o crear vehículo
      Vehicle vehicle = vehiclePersistencePort.findByPlateNumber(command.getPlateNumber())
            .orElseGet(() -> {
               Vehicle v = new Vehicle(command.getPlateNumber().trim().toUpperCase());
               return vehiclePersistencePort.save(v);
            });

      // 4. Verificar que el vehículo no esté ya dentro
      if (transactionPersistencePort.existsActiveByVehicleId(vehicle.getId())) {
         Transaction active = transactionPersistencePort
               .findActiveByVehicleId(vehicle.getId()).orElseThrow();
         throw new VehicleAlreadyInsideException(vehicle.getLicensePlate(), active.getId());
      }

      // 5. Buscar o crear cliente
      Customer customer = customerPersistencePort
            .findByDocument(command.getDocumentTypeId(), command.getDocumentNumber())
            .orElseGet(() -> {
               Customer c = new Customer();
               c.setDocumentTypeId(command.getDocumentTypeId());
               c.setDocumentNumber(command.getDocumentNumber().trim());
               if (command.getCustomerName() != null) {
                  String[] parts = command.getCustomerName().trim().split("\\s+", 2);
                  c.setFirstName(parts[0]);
                  if (parts.length > 1) c.setLastName(parts[1]);
               }
               if (command.getCustomerPhone() != null) c.setPhone(command.getCustomerPhone().trim());
               if (command.getCustomerEmail() != null) c.setEmail(command.getCustomerEmail().trim().toLowerCase());
               return customerPersistencePort.save(c);
            });

      // 6. Obtener tarifa aplicable
      // TODO: Implementar lógica con ZoneShiftRate (zona + turno actual)
      List<Rate> activeRates = ratePersistencePort.findAllActive();
      if (activeRates.isEmpty()) {
         throw new IllegalArgumentException("No hay tarifas activas configuradas");
      }
      Rate rate = activeRates.get(0);

      // 7. Crear y guardar transacción
      Transaction transaction = new Transaction(
            vehicle.getId(), customer.getId(), space.getId(), zone.getId(), rate.getId(),
            command.getDocumentTypeId(), command.getDocumentNumber()
      );
      transaction.recordEntry(command.getOperatorId());
      transaction.setEntryMethod(command.getEntryMethod() != null ? command.getEntryMethod() : Transaction.METHOD_MANUAL);
      if (command.getPhotoUrl() != null) transaction.recordEntryPhoto(command.getPhotoUrl(), command.getPlateConfidence());
      if (command.getNotes() != null) transaction.setNotes(command.getNotes());
      transaction.setCreatedBy(command.getOperatorId());

      Transaction saved = transactionPersistencePort.save(transaction);

      // 8. Marcar espacio como ocupado
      space.markAsOccupied();
      spacePersistencePort.save(space);

      return buildDetailDto(saved, vehicle, customer, zone, space, rate, null);
   }

   // ========================= RecordExitUseCase =========================

   @Override
   public TransactionDetailDto recordExit(RecordExitCommand command) {
      // 1. Buscar transacción activa
      Transaction transaction;
      if (command.getTransactionId() != null) {
         transaction = transactionPersistencePort.findById(command.getTransactionId())
               .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));
      } else if (command.getPlateNumber() != null) {
         transaction = transactionPersistencePort.findActiveByPlateNumber(command.getPlateNumber())
               .orElseThrow(() -> new IllegalArgumentException(
                     "No hay transacción activa para: " + command.getPlateNumber()));
      } else {
         throw new IllegalArgumentException("Debe proporcionar ID de transacción o placa");
      }

      // 2. Validar estado
      if (!transaction.isActive()) {
         throw new InvalidTransactionStateException(transaction.getId(),
               transaction.getStatus(), Transaction.STATUS_ACTIVE, "registrar salida");
      }

      // 3. Registrar salida y validar documentos (anti-robo)
      transaction.recordExit(command.getExitDocumentTypeId(), command.getExitDocumentNumber(), command.getOperatorId());
      if (!transaction.verifyDocumentMatch()) {
         throw new DocumentMismatchException(
               transaction.getEntryDocumentNumber(), transaction.getExitDocumentNumber(), transaction.getId());
      }

      transaction.setExitMethod(command.getExitMethod() != null ? command.getExitMethod() : Transaction.METHOD_MANUAL);
      if (command.getPhotoUrl() != null) transaction.recordExitPhoto(command.getPhotoUrl(), command.getPlateConfidence());
      if (command.getNotes() != null) {
         String notes = transaction.getNotes() != null
               ? transaction.getNotes() + " | " + command.getNotes()
               : command.getNotes();
         transaction.setNotes(notes);
      }

      // 4. Calcular monto
      Rate rate = ratePersistencePort.findById(transaction.getRateId())
            .orElseThrow(() -> new IllegalArgumentException("Tarifa no encontrada"));
      transaction.calculateAmount(rate.getAmount());
      transaction.setUpdatedBy(command.getOperatorId());

      Transaction saved = transactionPersistencePort.save(transaction);

      // 5. Liberar espacio
      Space space = spacePersistencePort.findById(transaction.getParkingSpaceId())
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado"));
      space.markAsAvailable();
      spacePersistencePort.save(space);

      Vehicle vehicle = vehiclePersistencePort.findById(transaction.getVehicleId()).orElseThrow();
      Customer customer = customerPersistencePort.findById(transaction.getCustomerId()).orElseThrow();
      Zone zone = zonePersistencePort.findById(transaction.getZoneId()).orElseThrow();

      return buildDetailDto(saved, vehicle, customer, zone, space, rate, null);
   }

   // ========================= ProcessPaymentUseCase =========================

   @Override
   public TransactionDetailDto processPayment(ProcessPaymentCommand command) {
      Transaction transaction = transactionPersistencePort.findById(command.getTransactionId())
            .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada"));

      if (!transaction.isCompleted()) {
         throw new InvalidTransactionStateException(transaction.getId(),
               transaction.getStatus(), Transaction.STATUS_COMPLETED, "procesar pago");
      }
      if (!transaction.isPending()) {
         throw new InvalidTransactionStateException(transaction.getId(),
               transaction.getPaymentStatus(), Transaction.PAYMENT_STATUS_PENDING, "procesar pago");
      }
      if (command.getAmountPaid().compareTo(transaction.getTotalAmount()) < 0) {
         throw new IllegalArgumentException(
               String.format("Monto insuficiente. Requerido: %s, recibido: %s",
                     transaction.getTotalAmount(), command.getAmountPaid()));
      }

      // Crear pago
      Payment payment = new Payment(
            transaction.getId(), command.getPaymentTypeId(),
            command.getAmountPaid(), command.getOperatorId());
      if (command.getReferenceNumber() != null) payment.setReferenceNumber(command.getReferenceNumber());
      if (command.getNotes() != null) payment.setNotes(command.getNotes());
      payment.setCreatedBy(command.getOperatorId());

      Payment savedPayment = paymentPersistencePort.save(payment);

      // Actualizar transacción
      transaction.markAsPaid();
      transaction.setUpdatedBy(command.getOperatorId());
      if (Boolean.TRUE.equals(command.getSendReceipt())) {
         transaction.markReceiptAsSent();
         transaction.updateWhatsAppStatus("PENDING");
         transaction.updateEmailStatus("PENDING");
      }

      Transaction saved = transactionPersistencePort.save(transaction);

      Vehicle vehicle = vehiclePersistencePort.findById(transaction.getVehicleId()).orElseThrow();
      Customer customer = customerPersistencePort.findById(transaction.getCustomerId()).orElseThrow();
      Zone zone = zonePersistencePort.findById(transaction.getZoneId()).orElseThrow();
      Space space = spacePersistencePort.findById(transaction.getParkingSpaceId()).orElseThrow();
      Rate rate = ratePersistencePort.findById(transaction.getRateId()).orElseThrow();

      return buildDetailDto(saved, vehicle, customer, zone, space, rate, savedPayment);
   }

   // ========================= GetTransactionUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public TransactionDetailDto getById(Long transactionId) {
      return loadDetailDto(transactionPersistencePort.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada")));
   }

   @Override
   @Transactional(readOnly = true)
   public TransactionDetailDto getActiveByPlate(String plateNumber) {
      return transactionPersistencePort.findActiveByPlateNumber(plateNumber)
            .map(this::loadDetailDto)
            .orElse(null);
   }

   // ========================= ListActiveTransactionsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listAll(int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findAllActive(req);
      return toActivePagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listActiveByZone(Long zoneId, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findActiveByZoneId(zoneId, req);
      return toActivePagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> searchActiveByPlate(String plateNumber, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.searchActiveByPlate(plateNumber, req);
      return toActivePagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listOverdue(int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "ASC");
      PageResult<Transaction> result = transactionPersistencePort.findOverdue(MAX_RECOMMENDED_MINUTES, req);
      return toActivePagedResponse(result);
   }

   // ========================= ListTransactionsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listAll(int pageNumber, int pageSize, String sortBy, String sortDirection) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, sortBy, sortDirection);
      PageResult<Transaction> result = transactionPersistencePort.findAll(req);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listByDateRange(LocalDateTime start, LocalDateTime end,
                                                        int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByDateRange(start, end, req);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listByStatus(String status, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByStatus(status, req);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listByPaymentStatus(String paymentStatus, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByPaymentStatus(paymentStatus, req);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listByZone(Long zoneId, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByZoneId(zoneId, req);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> searchByPlate(String plateNumber, int pageNumber, int pageSize) {
      PageRequest req = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.searchByPlate(plateNumber, req);
      return toTransactionPagedResponse(result);
   }

   // ========================= BUILDERS PRIVADOS — RECORDS =========================

   /**
    * Construye TransactionDetailDto (record) con todos los datos relacionados.
    * Usa el constructor del record directamente con inner records.
    */
   private TransactionDetailDto buildDetailDto(Transaction t, Vehicle v, Customer c,
                                               Zone z, Space sp, Rate r, Payment p) {
      return new TransactionDetailDto(
            t.getId(),
            t.getStatus(),
            t.getPaymentStatus(),

            // VehicleInfo (record interno)
            new TransactionDetailDto.VehicleInfo(
                  v.getId(), v.getLicensePlate(), null, v.getBrand(), null, v.getColor()
            ),

            // CustomerInfo (record interno)
            new TransactionDetailDto.CustomerInfo(
                  c.getId(), c.getDocumentNumber(), null,
                  c.getFullName(), c.getPhone(), c.getEmail()
            ),

            // ZoneInfo (record interno)
            new TransactionDetailDto.ZoneInfo(z.getId(), z.getName(), z.getCode()),

            // SpaceInfo (record interno)
            new TransactionDetailDto.SpaceInfo(sp.getId(), sp.getCode(), sp.getType()),

            t.getEntryTime(),
            t.getExitTime(),
            t.getDurationMinutes(),
            t.getFormattedDuration(),

            // DocumentInfo entrada
            new TransactionDetailDto.DocumentInfo(null, t.getEntryDocumentNumber()),

            // DocumentInfo salida (nullable)
            t.getExitDocumentNumber() != null
                  ? new TransactionDetailDto.DocumentInfo(null, t.getExitDocumentNumber())
                  : null,

            // RateInfo (record interno)
            new TransactionDetailDto.RateInfo(r.getId(), r.getName(), r.getAmount()),

            t.getCalculatedAmount(),
            t.getDiscountAmount(),
            t.getTotalAmount(),
            t.getCurrency(),

            // PaymentInfo (nullable)
            p != null ? new TransactionDetailDto.PaymentInfo(
                  p.getId(), null, p.getAmount(),
                  p.getReferenceNumber(), p.getPaymentDate(), p.getStatus()
            ) : null,

            // OperatorInfo entrada/salida (nullable — se carga desde auth-service en fases futuras)
            new TransactionDetailDto.OperatorInfo(t.getEntryOperatorId(), null, null),
            t.getExitOperatorId() != null
                  ? new TransactionDetailDto.OperatorInfo(t.getExitOperatorId(), null, null)
                  : null,

            t.getEntryPhotoUrl(),
            t.getExitPhotoUrl(),
            t.getEntryPlateConfidence(),
            t.getExitPlateConfidence(),

            t.getReceiptSent(),
            t.getReceiptSentAt(),
            t.getReceiptWhatsAppStatus(),
            t.getReceiptEmailStatus(),

            t.getNotes(),
            t.getCancellationReason(),
            t.getCreatedAt(),
            t.getUpdatedAt()
      );
   }

   /**
    * Carga datos relacionados y construye TransactionDetailDto.
    */
   private TransactionDetailDto loadDetailDto(Transaction t) {
      Vehicle v = vehiclePersistencePort.findById(t.getVehicleId()).orElseThrow();
      Customer c = customerPersistencePort.findById(t.getCustomerId()).orElseThrow();
      Zone z = zonePersistencePort.findById(t.getZoneId()).orElseThrow();
      Space sp = spacePersistencePort.findById(t.getParkingSpaceId()).orElseThrow();
      Rate r = ratePersistencePort.findById(t.getRateId()).orElseThrow();
      Payment p = paymentPersistencePort.findByTransactionId(t.getId()).orElse(null);
      return buildDetailDto(t, v, c, z, sp, r, p);
   }

   /**
    * Construye ActiveTransactionDto (record) usando el factory method of().
    * Calcula tiempo transcurrido y monto en tiempo real.
    */
   private ActiveTransactionDto buildActiveDto(Transaction t) {
      Vehicle v = vehiclePersistencePort.findById(t.getVehicleId()).orElseThrow();
      Customer c = customerPersistencePort.findById(t.getCustomerId()).orElseThrow();
      Zone z = zonePersistencePort.findById(t.getZoneId()).orElseThrow();
      Space sp = spacePersistencePort.findById(t.getParkingSpaceId()).orElseThrow();
      Rate r = ratePersistencePort.findById(t.getRateId()).orElseThrow();

      int elapsedMinutes = (int) Duration.between(t.getEntryTime(), LocalDateTime.now()).toMinutes();
      BigDecimal currentAmount = r.getAmount()
            .multiply(BigDecimal.valueOf(elapsedMinutes / 60.0))
            .setScale(2, RoundingMode.HALF_UP);

      return ActiveTransactionDto.of(
            t.getId(),
            v.getId(), v.getLicensePlate(), null, v.getBrand(), v.getColor(),
            c.getId(), c.getFullName(), c.getPhone(), c.getEmail(), null, c.getDocumentNumber(),
            z.getId(), z.getName(), z.getCode(),
            sp.getId(), sp.getCode(), sp.getType(),
            t.getEntryTime(), elapsedMinutes, formatDuration(elapsedMinutes),
            r.getAmount(), currentAmount,
            elapsedMinutes > MAX_RECOMMENDED_MINUTES, MAX_RECOMMENDED_MINUTES,
            elapsedMinutes > MAX_RECOMMENDED_MINUTES,
            t.getEntryOperatorId(), null, t.getEntryMethod(),
            t.getEntryPhotoUrl(), t.getEntryPlateConfidence(),
            t.getNotes(), t.getCreatedAt()
      );
   }

   /**
    * Construye TransactionDto (record) simplificado para listados.
    */
   private TransactionDto buildTransactionDto(Transaction t) {
      Vehicle v = vehiclePersistencePort.findById(t.getVehicleId()).orElseThrow();
      Customer c = customerPersistencePort.findById(t.getCustomerId()).orElseThrow();
      Zone z = zonePersistencePort.findById(t.getZoneId()).orElseThrow();
      Space sp = spacePersistencePort.findById(t.getParkingSpaceId()).orElseThrow();

      return new TransactionDto(
            t.getId(),
            v.getLicensePlate(),
            c.getFullName(),
            z.getName(),
            sp.getCode(),
            t.getEntryTime(),
            t.getExitTime(),
            t.getFormattedDuration(),
            t.getTotalAmount(),
            t.getStatus(),
            t.getPaymentStatus(),
            t.getCreatedAt()
      );
   }

   // ========================= HELPERS DE PAGINACIÓN =========================

   private PagedResponse<ActiveTransactionDto> toActivePagedResponse(PageResult<Transaction> result) {
      List<ActiveTransactionDto> content = result.content().stream()
            .map(this::buildActiveDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   private PagedResponse<TransactionDto> toTransactionPagedResponse(PageResult<Transaction> result) {
      List<TransactionDto> content = result.content().stream()
            .map(this::buildTransactionDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   // ========================= HELPER DE FORMATO =========================

   private String formatDuration(int minutes) {
      if (minutes == 0) return "0min";
      int h = minutes / 60, m = minutes % 60;
      if (h > 0 && m > 0) return h + "h " + m + "min";
      return h > 0 ? h + "h" : m + "min";
   }
}