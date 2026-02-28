package com.winnersystems.smartparking.parking.application.service.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.command.CreatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.*;
import com.winnersystems.smartparking.parking.application.port.input.paymenttype.*;
import com.winnersystems.smartparking.parking.application.port.output.PaymentTypePersistencePort;
import com.winnersystems.smartparking.parking.domain.model.PaymentType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentTypeService implements
      CreatePaymentTypeUseCase,
      UpdatePaymentTypeUseCase,
      DeletePaymentTypeUseCase,
      GetPaymentTypeUseCase,
      ListPaymentTypesUseCase,
      TogglePaymentTypeStatusUseCase {

   private final PaymentTypePersistencePort persistencePort;

   public PaymentTypeService(PaymentTypePersistencePort persistencePort) {
      this.persistencePort = persistencePort;
   }

   // ========================= CREATE =========================

   @Override
   public PaymentTypeDto createPaymentType(CreatePaymentTypeCommand command) {
      if (persistencePort.existsByCode(command.getCode())) {
         throw new IllegalArgumentException("Ya existe un tipo de pago con el código: " + command.getCode());
      }

      PaymentType paymentType = new PaymentType(
            command.getCode().toUpperCase().trim(),
            command.getName()
      );
      paymentType.setDescription(command.getDescription());

      return toDto(persistencePort.save(paymentType));
   }

   // ========================= UPDATE =========================

   @Override
   public PaymentTypeDto updatePaymentType(Long paymentTypeId, UpdatePaymentTypeCommand command) {
      PaymentType paymentType = findOrThrow(paymentTypeId);

      paymentType.updateBasicInfo(command.getName(), command.getDescription());

      if (command.getStatus() != null) {
         if (command.getStatus()) {
            paymentType.activate();
         } else {
            paymentType.deactivate();
         }
      }

      return toDto(persistencePort.save(paymentType));
   }

   // ========================= DELETE =========================

   @Override
   public void deletePaymentType(Long paymentTypeId) {
      findOrThrow(paymentTypeId);
      persistencePort.delete(paymentTypeId);
   }

   // ========================= GET =========================

   @Override
   @Transactional(readOnly = true)
   public PaymentTypeDto getPaymentTypeById(Long paymentTypeId) {
      return toDto(findOrThrow(paymentTypeId));
   }

   // ========================= LIST =========================

   @Override
   @Transactional(readOnly = true)
   public PagedResponse<PaymentTypeDto> listAllPaymentTypes(int page, int size, String search, Boolean status) {
      PageRequest pageRequest = PageRequest.of(page, size, "id", "ASC");
      PageResult<PaymentType> result = persistencePort.findAll(pageRequest, search, status);
      return result.toPagedResponse(this::toDto);
   }

   @Override
   @Transactional(readOnly = true)
   public List<PaymentTypeDto> listAllActivePaymentTypes() {
      return persistencePort.findAllActive().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
   }

   // ========================= TOGGLE =========================

   @Override
   public PaymentTypeDto togglePaymentTypeStatus(Long paymentTypeId) {
      PaymentType paymentType = findOrThrow(paymentTypeId);
      if (paymentType.isActive()) {
         paymentType.deactivate();
      } else {
         paymentType.activate();
      }
      return toDto(persistencePort.save(paymentType));
   }

   @Override
   public PaymentTypeDto activatePaymentTypeDto(Long paymentTypeId) {
      PaymentType paymentType = findOrThrow(paymentTypeId);
      paymentType.activate();
      return toDto(persistencePort.save(paymentType));
   }

   @Override
   public PaymentTypeDto deactivatePaymentTypeDto(Long paymentTypeId) {
      PaymentType paymentType = findOrThrow(paymentTypeId);
      paymentType.deactivate();
      return toDto(persistencePort.save(paymentType));
   }

   // ========================= HELPERS =========================

   private PaymentType findOrThrow(Long id) {
      return persistencePort.findById(id)
            .orElseThrow(() -> new IllegalArgumentException(
                  "Tipo de pago no encontrado con ID: " + id));
   }

   private PaymentTypeDto toDto(PaymentType p) {
      return new PaymentTypeDto(
            p.getId(),
            p.getCode(),
            p.getName(),
            p.getDescription(),
            p.getStatus(),
            p.getCreatedAt(),
            p.getUpdatedAt()
      );
   }
}