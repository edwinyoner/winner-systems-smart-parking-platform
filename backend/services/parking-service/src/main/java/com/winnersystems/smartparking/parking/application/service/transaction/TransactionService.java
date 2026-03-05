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
 * Implementa todos los casos de uso relacionados con el ciclo completo:
 * Entrada → Salida → Pago
 *
 * Responsabilidades:
 * - Orquestar operaciones entre múltiples agregados (Vehicle, Customer, Space, etc.)
 * - Validar reglas de negocio complejas
 * - Coordinar persistencia de múltiples entidades
 * - Calcular montos y tiempos
 * - Construir DTOs con datos relacionados
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
   private final CustomerVehiclePersistencePort customerVehiclePersistencePort;
   private final ParkingPersistencePort parkingPersistencePort;
   private final SpacePersistencePort spacePersistencePort;
   private final ZonePersistencePort zonePersistencePort;
   private final RatePersistencePort ratePersistencePort;
   private final PaymentPersistencePort paymentPersistencePort;
   private final ParkingShiftRatePersistencePort parkingShiftRatePersistencePort;

   // ========================= CONSTANTES =========================

   private static final int MAX_RECOMMENDED_MINUTES = 480; // 8 horas

   // ========================= CONSTRUCTOR =========================

   public TransactionService(
         TransactionPersistencePort transactionPersistencePort,
         VehiclePersistencePort vehiclePersistencePort,
         CustomerPersistencePort customerPersistencePort,
         CustomerVehiclePersistencePort customerVehiclePersistencePort,
         ParkingPersistencePort parkingPersistencePort,
         SpacePersistencePort spacePersistencePort,
         ZonePersistencePort zonePersistencePort,
         RatePersistencePort ratePersistencePort,
         PaymentPersistencePort paymentPersistencePort,
         ParkingShiftRatePersistencePort parkingShiftRatePersistencePort) {
      this.transactionPersistencePort = transactionPersistencePort;
      this.vehiclePersistencePort = vehiclePersistencePort;
      this.customerPersistencePort = customerPersistencePort;
      this.customerVehiclePersistencePort = customerVehiclePersistencePort;
      this.parkingPersistencePort = parkingPersistencePort;
      this.spacePersistencePort = spacePersistencePort;
      this.zonePersistencePort = zonePersistencePort;
      this.ratePersistencePort = ratePersistencePort;
      this.paymentPersistencePort = paymentPersistencePort;
      this.parkingShiftRatePersistencePort = parkingShiftRatePersistencePort;
   }

   // ========================= RecordEntryUseCase =========================

   @Override
   public TransactionDetailDto recordEntry(RecordEntryCommand command) {
      // 1. Validar y cargar parking
      Parking parking = loadParking(command.parkingId());
      validateParkingOperational(parking);

      // 2. Validar y cargar zona
      Zone zone = loadZone(command.zoneId());
      validateZoneOperational(zone);

      // 3. Validar y cargar espacio
      Space space = loadSpace(command.spaceId());
      validateSpaceAvailable(space);

      // 4. Buscar o crear vehículo
      Vehicle vehicle = findOrCreateVehicle(command);

      // 5. Verificar que el vehículo no esté ya dentro
      validateVehicleNotInside(vehicle);

      // 6. Buscar o crear cliente
      Customer customer = findOrCreateCustomer(command);

      // 7. Registrar o actualizar relación Cliente-Vehículo
      registerCustomerVehicleRelation(customer.getId(), vehicle.getId());

      // 8. Obtener tarifa aplicable
      Rate rate = findApplicableRate(parking.getId(), zone.getId());

      // 9. Crear y guardar transacción
      Transaction transaction = buildEntryTransaction(command, vehicle, customer, zone, space, rate);
      Transaction saved = transactionPersistencePort.save(transaction);

      // 10. Marcar espacio como ocupado
      space.markAsOccupied();
      spacePersistencePort.save(space);

      // 11. Actualizar contadores de vehículo y cliente
      vehicle.recordVisit();
      vehiclePersistencePort.save(vehicle);
      customer.recordVisit();
      customerPersistencePort.save(customer);

      return buildTransactionDetailDto(saved, vehicle, customer, parking, zone, space, rate, null);
   }

   // ========================= RecordExitUseCase =========================

   @Override
   public TransactionDetailDto recordExit(RecordExitCommand command) {
      // 1. Buscar transacción activa
      Transaction transaction = findActiveTransaction(command);

      // 2. Validar estado
      validateTransactionActive(transaction);

      // 3. Cargar entidades relacionadas
      Vehicle vehicle = loadVehicle(transaction.getVehicleId());
      Customer customer = loadCustomer(transaction.getCustomerId());
      Parking parking = loadParking(transaction.getParkingId());
      Zone zone = loadZone(transaction.getZoneId());
      Space space = loadSpace(transaction.getSpaceId());
      Rate rate = loadRate(transaction.getRateId());

      // 4. Registrar salida y validar documentos (anti-robo)
      processExit(transaction, command);
      validateDocumentMatch(transaction);

      // 5. Calcular monto a pagar
      calculateTransactionAmount(transaction, rate);

      // 6. Guardar transacción actualizada
      transaction.setUpdatedBy(command.operatorId());
      Transaction saved = transactionPersistencePort.save(transaction);

      // 7. Liberar espacio
      space.markAsAvailable();
      spacePersistencePort.save(space);

      return buildTransactionDetailDto(saved, vehicle, customer, parking, zone, space, rate, null);
   }

   // ========================= ProcessPaymentUseCase =========================

   @Override
   public TransactionDetailDto processPayment(ProcessPaymentCommand command) {
      // 1. Cargar transacción
      Transaction transaction = loadTransaction(command.transactionId());

      // 2. Validar estado para pago
      validateTransactionForPayment(transaction, command);

      // 3. Crear y guardar pago
      Payment payment = buildPayment(command);
      Payment savedPayment = paymentPersistencePort.save(payment);

      // 4. Actualizar transacción como pagada
      transaction.markAsPaid();
      transaction.setUpdatedBy(command.operatorId());

      // 5. Enviar comprobante si está configurado
      if (Boolean.TRUE.equals(command.sendReceipt())) {
         markReceiptForSending(transaction);
      }

      Transaction saved = transactionPersistencePort.save(transaction);

      // 6. Cargar entidades relacionadas y construir DTO
      return buildTransactionDetailDtoWithPayment(saved, savedPayment);
   }

   // ========================= GetTransactionUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public TransactionDetailDto getTransactionById(Long transactionId) {
      Transaction transaction = loadTransaction(transactionId);
      return loadTransactionDetailDto(transaction);
   }

   @Override
   @Transactional(readOnly = true)
   public TransactionDetailDto getActiveTransactionByPlate(String plateNumber) {
      return transactionPersistencePort.findActiveByPlateNumber(plateNumber)
            .map(this::loadTransactionDetailDto)
            .orElse(null);
   }

   // ========================= ListActiveTransactionsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listAllActiveTransactions(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findAllActive(request);
      return toActiveTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listActiveTransactionsByZone(Long zoneId, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findActiveByZoneId(zoneId, request);
      return toActiveTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> searchActiveTransactionsByPlate(String plateNumber, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.searchActiveByPlate(plateNumber, request);
      return toActiveTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<ActiveTransactionDto> listOverdueTransactions(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "ASC");
      PageResult<Transaction> result = transactionPersistencePort.findOverdue(MAX_RECOMMENDED_MINUTES, request);
      return toActiveTransactionPagedResponse(result);
   }

   // ========================= ListTransactionsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listAllTransactions(int pageNumber, int pageSize, String sortBy, String sortDirection) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, sortBy, sortDirection);
      PageResult<Transaction> result = transactionPersistencePort.findAll(request);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listTransactionsByDateRange(LocalDateTime startDate, LocalDateTime endDate,
                                                                    int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByDateRange(startDate, endDate, request);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listTransactionsByStatus(String status, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByStatus(status, request);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listTransactionsByPaymentStatus(String paymentStatus, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "createdAt", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByPaymentStatus(paymentStatus, request);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> listTransactionsByZone(Long zoneId, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.findByZoneId(zoneId, request);
      return toTransactionPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<TransactionDto> searchTransactionsByPlate(String plateNumber, int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "entryTime", "DESC");
      PageResult<Transaction> result = transactionPersistencePort.searchByPlate(plateNumber, request);
      return toTransactionPagedResponse(result);
   }

   // ========================= HELPERS - CARGA DE ENTIDADES =========================

   private Parking loadParking(Long parkingId) {
      return parkingPersistencePort.findById(parkingId)
            .orElseThrow(() -> new IllegalArgumentException("Parking no encontrado: " + parkingId));
   }

   private Transaction loadTransaction(Long transactionId) {
      return transactionPersistencePort.findById(transactionId)
            .orElseThrow(() -> new IllegalArgumentException("Transacción no encontrada: " + transactionId));
   }

   private Zone loadZone(Long zoneId) {
      return zonePersistencePort.findById(zoneId)
            .orElseThrow(() -> new IllegalArgumentException("Zona no encontrada: " + zoneId));
   }

   private Space loadSpace(Long spaceId) {
      return spacePersistencePort.findById(spaceId)
            .orElseThrow(() -> new IllegalArgumentException("Espacio no encontrado: " + spaceId));
   }

   private Vehicle loadVehicle(Long vehicleId) {
      return vehiclePersistencePort.findById(vehicleId)
            .orElseThrow(() -> new IllegalArgumentException("Vehículo no encontrado: " + vehicleId));
   }

   private Customer loadCustomer(Long customerId) {
      return customerPersistencePort.findById(customerId)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado: " + customerId));
   }

   private Rate loadRate(Long rateId) {
      return ratePersistencePort.findById(rateId)
            .orElseThrow(() -> new IllegalArgumentException("Tarifa no encontrada: " + rateId));
   }

   // ========================= HELPERS - VALIDACIONES =========================

   private void validateParkingOperational(Parking parking) {
      if (!parking.isOperational()) {
         throw new ParkingNotOperationalException(parking.getName(), "El parking no está operativo");
      }
   }

   private void validateZoneOperational(Zone zone) {
      if (!zone.isOperational()) {
         throw new ZoneNotOperationalException(zone.getName(), "La zona no está operativa");
      }
   }

   private void validateSpaceAvailable(Space space) {
      if (!space.isAvailable()) {
         throw new SpaceNotAvailableException(space.getCode(), space.getStatus());
      }
   }

   private void validateVehicleNotInside(Vehicle vehicle) {
      if (transactionPersistencePort.existsActiveByVehicleId(vehicle.getId())) {
         Transaction activeTransaction = transactionPersistencePort
               .findActiveByVehicleId(vehicle.getId())
               .orElseThrow();
         throw new VehicleAlreadyInsideException(vehicle.getLicensePlate(), activeTransaction.getId());
      }
   }

   private void validateTransactionActive(Transaction transaction) {
      if (!transaction.isActive()) {
         throw new InvalidTransactionStateException(
               transaction.getId(),
               transaction.getStatus(),
               Transaction.STATUS_ACTIVE,
               "registrar salida"
         );
      }
   }

   private void validateDocumentMatch(Transaction transaction) {
      if (!transaction.verifyDocumentMatch()) {
         throw new DocumentMismatchException(
               transaction.getEntryDocumentNumber(),
               transaction.getExitDocumentNumber(),
               transaction.getId()
         );
      }
   }

   private void validateTransactionForPayment(Transaction transaction, ProcessPaymentCommand command) {
      if (!transaction.isCompleted()) {
         throw new InvalidTransactionStateException(
               transaction.getId(),
               transaction.getStatus(),
               Transaction.STATUS_COMPLETED,
               "procesar pago"
         );
      }

      if (!transaction.isPending()) {
         throw new InvalidTransactionStateException(
               transaction.getId(),
               transaction.getPaymentStatus(),
               Transaction.PAYMENT_STATUS_PENDING,
               "procesar pago"
         );
      }

      if (command.amountPaid().compareTo(transaction.getTotalAmount()) < 0) {
         throw new IllegalArgumentException(
               String.format("Monto insuficiente. Requerido: %s, recibido: %s",
                     transaction.getTotalAmount(), command.amountPaid())
         );
      }
   }

   // ========================= HELPERS - BUSCAR O CREAR =========================

   private Vehicle findOrCreateVehicle(RecordEntryCommand command) {
      String normalizedPlate = command.plateNumber().trim().toUpperCase();

      return vehiclePersistencePort.findByPlateNumber(normalizedPlate)
            .orElseGet(() -> {
               Vehicle vehicle = new Vehicle(normalizedPlate);
               vehicle.setCreatedBy(command.operatorId());
               return vehiclePersistencePort.save(vehicle);
            });
   }

   private Customer findOrCreateCustomer(RecordEntryCommand command) {
      String normalizedDocument = command.documentNumber().trim().toUpperCase();

      return customerPersistencePort.findByDocument(command.documentTypeId(), normalizedDocument)
            .orElseGet(() -> {
               Customer customer = new Customer(
                     command.documentTypeId(),
                     normalizedDocument,
                     command.customerFirstName(),
                     command.customerLastName()
               );

               if (command.customerPhone() != null) {
                  customer.setPhone(command.customerPhone().trim());
               }
               if (command.customerEmail() != null) {
                  customer.setEmail(command.customerEmail().trim().toLowerCase());
               }

               customer.setCreatedBy(command.operatorId());
               return customerPersistencePort.save(customer);
            });
   }

   // ========================= HELPERS - TRANSACCIONES =========================

   private Transaction findActiveTransaction(RecordExitCommand command) {
      if (command.transactionId() != null) {
         return loadTransaction(command.transactionId());
      }

      if (command.plateNumber() != null) {
         return transactionPersistencePort.findActiveByPlateNumber(command.plateNumber())
               .orElseThrow(() -> new IllegalArgumentException(
                     "No hay transacción activa para la placa: " + command.plateNumber()
               ));
      }

      throw new IllegalArgumentException("Debe proporcionar ID de transacción o placa");
   }

   private Transaction buildEntryTransaction(RecordEntryCommand command, Vehicle vehicle,
                                             Customer customer, Zone zone, Space space, Rate rate) {
      Transaction transaction = new Transaction(
            vehicle.getId(),
            customer.getId(),
            command.parkingId(),
            zone.getId(),
            space.getId(),
            rate.getId(),
            command.documentTypeId(),
            command.documentNumber()
      );

      transaction.recordEntry(command.operatorId());
      transaction.setEntryMethod(
            command.entryMethod() != null ? command.entryMethod() : Transaction.METHOD_MANUAL
      );

      if (command.photoUrl() != null) {
         transaction.recordEntryPhoto(command.photoUrl(), command.plateConfidence());
      }

      if (command.notes() != null) {
         transaction.setNotes(command.notes());
      }

      transaction.setCreatedBy(command.operatorId());

      return transaction;
   }

   private void processExit(Transaction transaction, RecordExitCommand command) {
      transaction.recordExit(
            command.exitDocumentTypeId(),
            command.exitDocumentNumber(),
            command.operatorId()
      );

      transaction.setExitMethod(
            command.exitMethod() != null ? command.exitMethod() : Transaction.METHOD_MANUAL
      );

      if (command.photoUrl() != null) {
         transaction.recordExitPhoto(command.photoUrl(), command.plateConfidence());
      }

      if (command.notes() != null) {
         String existingNotes = transaction.getNotes();
         String newNotes = existingNotes != null
               ? existingNotes + " | " + command.notes()
               : command.notes();
         transaction.setNotes(newNotes);
      }
   }

   private void calculateTransactionAmount(Transaction transaction, Rate rate) {
      transaction.calculateAmount(rate.getAmount());
   }

   private Payment buildPayment(ProcessPaymentCommand command) {
      Payment payment = new Payment(
            command.transactionId(),
            command.paymentTypeId(),
            command.amountPaid(),
            command.operatorId()
      );

      if (command.referenceNumber() != null) {
         payment.setReferenceNumber(command.referenceNumber());
      }

      if (command.notes() != null) {
         payment.setNotes(command.notes());
      }

      payment.setCreatedBy(command.operatorId());

      return payment;
   }

   private void markReceiptForSending(Transaction transaction) {
      transaction.markReceiptAsSent();
      transaction.updateWhatsAppStatus("PENDING");
      transaction.updateEmailStatus("PENDING");
   }

   // ========================= HELPERS - TARIFAS =========================

   /**
    * Obtiene la tarifa aplicable para un parking y zona.
    *
    * FASE 1: Usa la primera tarifa activa del sistema.
    * FASE 2: Usará ParkingShiftRate basado en turno actual.
    */
   private Rate findApplicableRate(Long parkingId, Long zoneId) {
      // TODO FASE 2: Implementar lógica con ParkingShiftRate
      // 1. Determinar turno actual basado en hora
      // 2. Buscar configuración parking + shift
      // 3. Obtener rate de la configuración

      // FASE 1: Usar primera tarifa activa
      List<Rate> activeRates = ratePersistencePort.findAllActive();
      if (activeRates.isEmpty()) {
         throw new IllegalArgumentException("No hay tarifas activas configuradas en el sistema");
      }

      return activeRates.get(0);
   }

   // ========================= HELPERS - CUSTOMER-VEHICLE =========================

   private void registerCustomerVehicleRelation(Long customerId, Long vehicleId) {
      customerVehiclePersistencePort.findByCustomerAndVehicle(customerId, vehicleId)
            .ifPresentOrElse(
                  // Si existe, incrementar usageCount
                  relation -> {
                     relation.incrementUsage();
                     customerVehiclePersistencePort.save(relation);
                  },
                  // Si no existe, crear nueva relación
                  () -> {
                     CustomerVehicle relation = new CustomerVehicle(customerId, vehicleId);
                     relation.incrementUsage(); // Inicializa en 1
                     customerVehiclePersistencePort.save(relation);
                  }
            );
   }

   // ========================= BUILDERS - DTOs =========================

   /**
    * Construye TransactionDetailDto con todos los datos relacionados.
    *
    * IMPORTANTE: Incluye ParkingInfo completo.
    */
   private TransactionDetailDto buildTransactionDetailDto(Transaction t, Vehicle v, Customer c,
                                                          Parking p, Zone z, Space sp, Rate r, Payment pay) {
      return new TransactionDetailDto(
            t.getId(),
            t.getStatus(),
            t.getPaymentStatus(),

            // VehicleInfo - SOLO placa
            new TransactionDetailDto.VehicleInfo(
                  v.getId(),
                  v.getLicensePlate()
            ),

            // CustomerInfo
            new TransactionDetailDto.CustomerInfo(
                  c.getId(),
                  null,                                     // documentType - nullable
                  c.getDocumentNumber(),
                  c.getFullName(),
                  c.getPhone(),
                  c.getEmail()
            ),

            // ParkingInfo
            new TransactionDetailDto.ParkingInfo(
                  p.getId(),
                  p.getName(),
                  p.getCode()
            ),

            // ZoneInfo
            new TransactionDetailDto.ZoneInfo(
                  z.getId(),
                  z.getName(),
                  z.getCode()
            ),

            // SpaceInfo
            new TransactionDetailDto.SpaceInfo(
                  sp.getId(),
                  sp.getCode(),
                  sp.getType()
            ),

            // Tiempos
            t.getEntryTime(),
            t.getExitTime(),
            t.getDurationMinutes(),
            t.getFormattedDuration(),

            // Documentos
            new TransactionDetailDto.DocumentInfo(null, t.getEntryDocumentNumber()),
            t.getExitDocumentNumber() != null
                  ? new TransactionDetailDto.DocumentInfo(null, t.getExitDocumentNumber())
                  : null,

            // RateInfo
            new TransactionDetailDto.RateInfo(
                  r.getId(),
                  r.getName(),
                  r.getAmount()
            ),

            // Montos
            t.getCalculatedAmount(),
            t.getDiscountAmount(),
            t.getTotalAmount(),
            t.getCurrency(),

            // PaymentInfo (nullable)
            pay != null ? new TransactionDetailDto.PaymentInfo(
                  pay.getId(),
                  null,                                      // paymentType - nullable
                  pay.getAmount(),
                  pay.getReferenceNumber(),
                  pay.getPaymentDate(),
                  pay.getStatus()
            ) : null,

            // OperatorInfo - nullable (auth-service en futuro)
            new TransactionDetailDto.OperatorInfo(t.getEntryOperatorId(), null, null),
            t.getExitOperatorId() != null
                  ? new TransactionDetailDto.OperatorInfo(t.getExitOperatorId(), null, null)
                  : null,

            // Evidencia
            t.getEntryPhotoUrl(),
            t.getExitPhotoUrl(),
            t.getEntryPlateConfidence(),
            t.getExitPlateConfidence(),

            // Comprobante
            t.getReceiptSent(),
            t.getReceiptSentAt(),
            t.getReceiptWhatsAppStatus(),
            t.getReceiptEmailStatus(),

            // Observaciones
            t.getNotes(),
            t.getCancellationReason(),

            // Auditoría
            t.getCreatedAt(),
            t.getUpdatedAt()
      );
   }

   /**
    * Carga todos los datos relacionados y construye TransactionDetailDto.
    */
   private TransactionDetailDto loadTransactionDetailDto(Transaction t) {
      Vehicle vehicle = loadVehicle(t.getVehicleId());
      Customer customer = loadCustomer(t.getCustomerId());
      Parking parking = loadParking(t.getParkingId());
      Zone zone = loadZone(t.getZoneId());
      Space space = loadSpace(t.getSpaceId());
      Rate rate = loadRate(t.getRateId());
      Payment payment = paymentPersistencePort.findByTransactionId(t.getId()).orElse(null);

      return buildTransactionDetailDto(t, vehicle, customer, parking, zone, space, rate, payment);
   }

   /**
    * Construye TransactionDetailDto cargando payment.
    */
   private TransactionDetailDto buildTransactionDetailDtoWithPayment(Transaction transaction, Payment payment) {
      Vehicle vehicle = loadVehicle(transaction.getVehicleId());
      Customer customer = loadCustomer(transaction.getCustomerId());
      Parking parking = loadParking(transaction.getParkingId());
      Zone zone = loadZone(transaction.getZoneId());
      Space space = loadSpace(transaction.getSpaceId());
      Rate rate = loadRate(transaction.getRateId());

      return buildTransactionDetailDto(transaction, vehicle, customer, parking, zone, space, rate, payment);
   }

   /**
    * Construye ActiveTransactionDto para monitoreo en tiempo real.
    */
   private ActiveTransactionDto buildActiveTransactionDto(Transaction t) {
      Vehicle vehicle = loadVehicle(t.getVehicleId());
      Customer customer = loadCustomer(t.getCustomerId());
      Parking parking = loadParking(t.getParkingId());
      Zone zone = loadZone(t.getZoneId());
      Space space = loadSpace(t.getSpaceId());
      Rate rate = loadRate(t.getRateId());

      // Calcular tiempo transcurrido y monto actual
      int elapsedMinutes = (int) Duration.between(t.getEntryTime(), LocalDateTime.now()).toMinutes();
      BigDecimal currentAmount = rate.getAmount()
            .multiply(BigDecimal.valueOf(elapsedMinutes / 60.0))
            .setScale(2, RoundingMode.HALF_UP);

      boolean isOverdue = elapsedMinutes > MAX_RECOMMENDED_MINUTES;

      return ActiveTransactionDto.of(
            t.getId(),
            vehicle.getId(), vehicle.getLicensePlate(),
            customer.getId(), customer.getFullName(), customer.getPhone(), customer.getEmail(),
            null, customer.getDocumentNumber(),
            parking.getId(), parking.getName(),
            zone.getId(), zone.getName(),
            space.getId(), space.getCode(),
            t.getEntryTime(), elapsedMinutes, formatDuration(elapsedMinutes),
            rate.getAmount(), currentAmount,
            isOverdue, MAX_RECOMMENDED_MINUTES, isOverdue,
            t.getEntryOperatorId(), null, t.getEntryMethod(),
            t.getEntryPhotoUrl(), t.getEntryPlateConfidence(),
            t.getNotes(), t.getCreatedAt()
      );
   }

   /**
    * Construye TransactionDto simplificado para listados.
    */
   private TransactionDto buildTransactionDto(Transaction t) {
      Vehicle vehicle = loadVehicle(t.getVehicleId());
      Customer customer = loadCustomer(t.getCustomerId());
      Parking parking = loadParking(t.getParkingId());
      Zone zone = loadZone(t.getZoneId());
      Space space = loadSpace(t.getSpaceId());

      return new TransactionDto(
            t.getId(),
            vehicle.getLicensePlate(),
            customer.getFullName(),
            parking.getName(),
            zone.getName(),
            space.getCode(),
            t.getEntryTime(),
            t.getExitTime(),
            t.getFormattedDuration(),
            t.getTotalAmount(),
            t.getStatus(),
            t.getPaymentStatus(),
            t.getCreatedAt()
      );
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private PagedResponse<ActiveTransactionDto> toActiveTransactionPagedResponse(PageResult<Transaction> result) {
      List<ActiveTransactionDto> content = result.content().stream()
            .map(this::buildActiveTransactionDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   private PagedResponse<TransactionDto> toTransactionPagedResponse(PageResult<Transaction> result) {
      List<TransactionDto> content = result.content().stream()
            .map(this::buildTransactionDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   // ========================= HELPERS - FORMATO =========================

   private String formatDuration(int minutes) {
      if (minutes == 0) return "0min";
      int hours = minutes / 60;
      int mins = minutes % 60;

      if (hours > 0 && mins > 0) {
         return hours + "h " + mins + "min";
      }
      return hours > 0 ? hours + "h" : mins + "min";
   }
}