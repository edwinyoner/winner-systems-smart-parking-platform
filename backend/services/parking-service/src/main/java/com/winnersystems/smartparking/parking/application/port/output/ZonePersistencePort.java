package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Zone;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Zone.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ZonePersistencePort {

   Zone save(Zone zone);

   Optional<Zone> findById(Long id);

   Optional<Zone> findByCode(String code);

   List<Zone> findAllActive();

   List<Zone> findByParkingId(Long parkingId);

   PageResult<Zone> findAll(PageRequest pageRequest, String search, String status);

   void delete(Long id);

   boolean existsByCode(String code);

   boolean existsByCodeAndIdNot(String code, Long id);

   boolean existsByName(String name);
}