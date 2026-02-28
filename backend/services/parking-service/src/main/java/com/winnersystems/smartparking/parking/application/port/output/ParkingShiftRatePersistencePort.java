package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.ParkingShiftRate;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de ParkingShiftRate.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ParkingShiftRatePersistencePort {

   ParkingShiftRate save(ParkingShiftRate parkingShiftRate);

   Optional<ParkingShiftRate> findById(Long id);

   List<ParkingShiftRate> findByParkingId(Long parkingId);

   /**
    * Busca configuración por parqueo y turno.
    * Solo puede haber UNA tarifa por combinación parking+shift.
    */
   Optional<ParkingShiftRate> findByParkingIdAndShiftId(Long parkingId, Long shiftId);

   void deleteByParkingId(Long parkingId);

   void deleteById(Long id);

   /**
    * Verifica si ya existe configuración para este parking+shift.
    * Útil para evitar duplicados.
    */
   boolean existsByParkingIdAndShiftId(Long parkingId, Long shiftId);
}