package com.winnersystems.smartparking.parking.application.port.input.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.command.UpdatePaymentTypeCommand;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;

public interface UpdatePaymentTypeUseCase {
   PaymentTypeDto updatePaymentType(Long paymentTypeId, UpdatePaymentTypeCommand command);
}