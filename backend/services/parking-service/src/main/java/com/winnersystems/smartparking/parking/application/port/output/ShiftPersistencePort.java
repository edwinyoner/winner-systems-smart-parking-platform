package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.Shift;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Shift.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface ShiftPersistencePort {

   Shift save(Shift shift);

   Optional<Shift> findById(Long id);

   Optional<Shift> findByCode(String code);

   List<Shift> findAllActive();

   PageResult<Shift> findAll(PageRequest pageRequest, String search, Boolean status);

   void delete(Long id);

   boolean existsByCode(String code);

   boolean existsByCodeAndIdNot(String code, Long id);
}