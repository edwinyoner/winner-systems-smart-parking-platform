package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Parking;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Parking.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ParkingPersistencePort {

   Parking save(Parking parking);

   Optional<Parking> findById(Long id);

   PageResult<Parking> findAll(PageRequest pageRequest, String search, String status);

   List<Parking> findAllActive();

   Optional<Parking> findByCode(String code);

   boolean existsByCode(String code);

   boolean existsByCodeAndIdNot(String code, Long id);

   void delete(Long id);

   long count();
}