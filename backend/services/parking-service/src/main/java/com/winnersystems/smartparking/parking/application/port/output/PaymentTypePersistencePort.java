package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.PaymentType;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;

import java.util.List;
import java.util.Optional;

public interface PaymentTypePersistencePort {
   PaymentType save(PaymentType paymentType);
   Optional<PaymentType> findById(Long id);
   Optional<PaymentType> findByCode(String code);
   List<PaymentType> findAllActive();
   PageResult<PaymentType> findAll(PageRequest pageRequest, String search, Boolean status);
   void delete(Long id);
   boolean existsByCode(String code);
   boolean existsByCodeAndIdNot(String code, Long id);
}