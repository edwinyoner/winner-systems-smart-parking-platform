package com.winnersystems.smartparking.parking.application.port.input.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;

public interface GetPaymentTypeUseCase {
   PaymentTypeDto getPaymentTypeById(Long paymentTypeId);
}