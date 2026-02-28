package com.winnersystems.smartparking.parking.application.port.input.paymenttype;

import com.winnersystems.smartparking.parking.application.dto.query.*;

import java.util.List;

public interface ListPaymentTypesUseCase {
   PagedResponse<PaymentTypeDto> listAllPaymentTypes(int page, int size, String search, Boolean status);
   List<PaymentTypeDto> listAllActivePaymentTypes();
}