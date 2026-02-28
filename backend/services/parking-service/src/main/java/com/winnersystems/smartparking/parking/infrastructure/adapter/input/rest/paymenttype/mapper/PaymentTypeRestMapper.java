package com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.mapper;

import com.winnersystems.smartparking.parking.application.dto.command.CreatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.command.UpdatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.request.CreatePaymentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.request.UpdatePaymentTypeRequest;
import com.winnersystems.smartparking.parking.infrastructure.adapter.input.rest.paymenttype.dto.response.PaymentTypeResponse;
import org.springframework.stereotype.Component;

@Component
public class PaymentTypeRestMapper {

   public CreatePaymentTypeCommand toCommand(CreatePaymentTypeRequest request) {
      return CreatePaymentTypeCommand.builder()
            .code(request.getCode().toUpperCase().trim())
            .name(request.getName())
            .description(request.getDescription())
            .build();
   }

   public UpdatePaymentTypeCommand toCommand(UpdatePaymentTypeRequest request) {
      return UpdatePaymentTypeCommand.builder()
            .name(request.getName())
            .description(request.getDescription())
            .status(request.getStatus())
            .build();
   }

   public PaymentTypeResponse toResponse(PaymentTypeDto dto) {
      return new PaymentTypeResponse(
            dto.id(),
            dto.code(),
            dto.name(),
            dto.description(),
            dto.status(),
            dto.createdAt(),
            dto.updatedAt()
      );
   }
}