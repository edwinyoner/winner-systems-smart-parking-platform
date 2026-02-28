package com.winnersystems.smartparking.parking.application.port.input.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.command.CreatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;

public interface CreatePaymentTypeUseCase {
   PaymentTypeDto createPaymentType(CreatePaymentTypeCommand command);
}