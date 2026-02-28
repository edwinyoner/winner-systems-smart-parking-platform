package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Space;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Space.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface SpacePersistencePort {

   Space save(Space space);

   Optional<Space> findById(Long id);

   Optional<Space> findByCode(String code);

   List<Space> findByZoneId(Long zoneId);

   List<Space> findAvailableByZoneId(Long zoneId);

   List<Space> findAllActive();

   PageResult<Space> findAll(PageRequest pageRequest, String search, String status);

   long countAvailableByZoneId(Long zoneId);

   List<Space> findByZoneIdAndType(Long zoneId, String type);

   long countByZoneId(Long zoneId);

   boolean existsByCode(String code);

   boolean existsByCodeAndIdNot(String code, Long id);

   void delete(Long id);
}
