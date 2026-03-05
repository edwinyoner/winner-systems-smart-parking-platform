package com.winnersystems.smartparking.parking.application.service.payment;

import com.winnersystems.smartparking.parking.application.dto.command.RefundPaymentCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.payment.*;
import com.winnersystems.smartparking.parking.application.port.output.*;
import com.winnersystems.smartparking.parking.domain.model.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Servicio de aplicación para gestión de pagos.
 *
 * Responsabilidades:
 * - Consultar información de pagos
 * - Procesar devoluciones (refunds)
 * - Listar pagos con filtros y paginación
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Service
@Transactional
public class PaymentService implements
      GetPaymentUseCase,
      ListPaymentsUseCase,
      ProcessRefundUseCase {

   // ========================= PUERTOS DE SALIDA =========================

   private final PaymentPersistencePort paymentPersistencePort;
   private final PaymentTypePersistencePort paymentTypePersistencePort;
   private final TransactionPersistencePort transactionPersistencePort;

   // ========================= CONSTRUCTOR =========================

   public PaymentService(
         PaymentPersistencePort paymentPersistencePort,
         PaymentTypePersistencePort paymentTypePersistencePort,
         TransactionPersistencePort transactionPersistencePort) {
      this.paymentPersistencePort = paymentPersistencePort;
      this.paymentTypePersistencePort = paymentTypePersistencePort;
      this.transactionPersistencePort = transactionPersistencePort;
   }

   // ========================= GetPaymentUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PaymentDto getPaymentById(Long paymentId) {
      Payment payment = loadPayment(paymentId);
      return buildPaymentDto(payment);
   }

   @Override
   @Transactional(readOnly = true)
   public PaymentDto getPaymentByTransaction(Long transactionId) {
      return paymentPersistencePort.findByTransactionId(transactionId)
            .map(this::buildPaymentDto)
            .orElse(null);
   }

   @Override
   @Transactional(readOnly = true)
   public PaymentDto getPaymentByReferenceNumber(String referenceNumber) {
      return paymentPersistencePort.findByReferenceNumber(referenceNumber)
            .map(this::buildPaymentDto)
            .orElse(null);
   }

   // ========================= ListPaymentsUseCase =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentDto> listAllPayments(int pageNumber, int pageSize,
                                                    String search, String status) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "paymentDate", "DESC");
      PageResult<Payment> result = paymentPersistencePort.findAll(request, search, status);
      return toPaymentPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentDto> listPaymentsByType(Long paymentTypeId,
                                                       int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "paymentDate", "DESC");
      PageResult<Payment> result = paymentPersistencePort.findByPaymentType(paymentTypeId, request);
      return toPaymentPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentDto> listPaymentsByDateRange(LocalDateTime startDate,
                                                            LocalDateTime endDate,
                                                            int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "paymentDate", "DESC");
      PageResult<Payment> result = paymentPersistencePort.findByDateRange(startDate, endDate, request);
      return toPaymentPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentDto> listPaymentsWithRefunds(int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "refundDate", "DESC");
      PageResult<Payment> result = paymentPersistencePort.findWithRefunds(request);
      return toPaymentPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentDto> listPaymentsByOperator(Long operatorId,
                                                           int pageNumber, int pageSize) {
      PageRequest request = PageRequest.of(pageNumber, pageSize, "paymentDate", "DESC");
      PageResult<Payment> result = paymentPersistencePort.findByOperator(operatorId, request);
      return toPaymentPagedResponse(result);
   }

   @Override
   @Transactional(readOnly = true)
   public List<PaymentDto> listAllCompletedPayments() {
      return paymentPersistencePort.findAllCompleted().stream()
            .map(this::buildPaymentDto)
            .toList();
   }

   // ========================= ProcessRefundUseCase =========================

   @Override
   public PaymentDto processFullRefund(Long paymentId, RefundPaymentCommand command) {
      Payment payment = loadPayment(paymentId);
      validatePaymentForRefund(payment);

      // Procesar devolución completa
      payment.processRefund(command.reason(), getCurrentUserId());

      Payment saved = paymentPersistencePort.save(payment);

      // Actualizar Transaction a PENDING si el refund es completo
      updateTransactionAfterRefund(saved, true);

      return buildPaymentDto(saved);
   }

   @Override
   public PaymentDto processPartialRefund(Long paymentId, RefundPaymentCommand command) {
      Payment payment = loadPayment(paymentId);
      validatePaymentForRefund(payment);
      validateRefundAmount(payment, command.refundAmount());

      // Procesar devolución parcial
      payment.processPartialRefund(command.refundAmount(), command.reason(), getCurrentUserId());

      Payment saved = paymentPersistencePort.save(payment);

      // No actualizar Transaction en refund parcial
      updateTransactionAfterRefund(saved, false);

      return buildPaymentDto(saved);
   }

   // ========================= HELPERS - CARGA =========================

   private Payment loadPayment(Long paymentId) {
      return paymentPersistencePort.findById(paymentId)
            .orElseThrow(() -> new IllegalArgumentException("Pago no encontrado: " + paymentId));
   }

   // ========================= HELPERS - VALIDACIONES =========================

   private void validatePaymentForRefund(Payment payment) {
      if (!payment.isCompleted()) {
         throw new IllegalStateException(
               String.format("El pago #%d no está completado. Estado actual: %s",
                     payment.getId(), payment.getStatus())
         );
      }

      if (payment.hasRefund()) {
         throw new IllegalStateException(
               String.format("El pago #%d ya tiene una devolución registrada", payment.getId())
         );
      }
   }

   private void validateRefundAmount(Payment payment, java.math.BigDecimal refundAmount) {
      if (refundAmount.compareTo(payment.getAmount()) > 0) {
         throw new IllegalArgumentException(
               String.format("El monto de devolución (%s) no puede ser mayor al monto pagado (%s)",
                     refundAmount, payment.getAmount())
         );
      }
   }

   // ========================= HELPERS - ACTUALIZACIÓN TRANSACTION =========================

   private void updateTransactionAfterRefund(Payment payment, boolean isFullRefund) {
      if (isFullRefund) {
         Transaction transaction = transactionPersistencePort
               .findById(payment.getTransactionId())
               .orElseThrow();

         // Volver a PENDING si es refund completo
         transaction.setPaymentStatus(Transaction.PAYMENT_STATUS_PENDING);
         transaction.setUpdatedBy(getCurrentUserId());
         transactionPersistencePort.save(transaction);
      }
   }

   // ========================= BUILDERS - DTOs =========================

   private PaymentDto buildPaymentDto(Payment p) {
      String paymentTypeName = p.getPaymentTypeId() != null
            ? paymentTypePersistencePort.findById(p.getPaymentTypeId())
            .map(PaymentType::getName)
            .orElse(null)
            : null;

      return new PaymentDto(
            p.getId(),
            p.getTransactionId(),
            p.getPaymentTypeId(),
            paymentTypeName,
            p.getAmount(),
            p.getCurrency(),
            p.getPaymentDate(),
            p.getReferenceNumber(),
            p.getOperatorId(),
            null, // operatorName - TODO: cargar desde auth-service
            p.getStatus(),
            p.getRefundAmount(),
            p.getRefundDate(),
            p.getRefundReason(),
            p.getNetAmount(),
            p.getCreatedAt()
      );
   }

   // ========================= HELPERS - PAGINACIÓN =========================

   private PagedResponse<PaymentDto> toPaymentPagedResponse(PageResult<Payment> result) {
      List<PaymentDto> content = result.content().stream()
            .map(this::buildPaymentDto)
            .toList();
      return PagedResponse.of(content, result.pageNumber(), result.pageSize(), result.totalElements());
   }

   // ========================= HELPERS - UTILIDADES =========================

   private Long getCurrentUserId() {
      // TODO: Obtener desde SecurityContext cuando se integre Spring Security
      return 1L; // Placeholder
   }
}