package com.winnersystems.smartparking.parking.application.port.input.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.query.DocumentTypeDto;
import com.winnersystems.smartparking.parking.application.dto.query.PaymentTypeDto;

public interface TogglePaymentTypeStatusUseCase {
   PaymentTypeDto togglePaymentTypeStatus(Long paymentTypeId);
   PaymentTypeDto activatePaymentTypeDto(Long paymentTypeId);
   PaymentTypeDto deactivatePaymentTypeDto(Long paymentTypeId);
}