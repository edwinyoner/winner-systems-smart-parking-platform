package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.Rate;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Rate.
 *
 * @author Edwin Yoner - Winner Systems
 */
public interface RatePersistencePort {

   Rate save(Rate rate);

   Optional<Rate> findById(Long id);

   List<Rate> findAllActive();

   PageResult<Rate> findAll(PageRequest pageRequest, String search, Boolean status);

   void delete(Long id);

   boolean existsByName(String name);

   boolean existsByNameAndIdNot(String name, Long id);
}