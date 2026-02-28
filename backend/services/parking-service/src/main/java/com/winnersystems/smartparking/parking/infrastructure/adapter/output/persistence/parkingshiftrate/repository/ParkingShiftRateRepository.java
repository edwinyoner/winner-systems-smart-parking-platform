package com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.repository;

import com.winnersystems.smartparking.parking.infrastructure.adapter.output.persistence.parkingshiftrate.entity.ParkingShiftRateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para ParkingShiftRate.
 * Configurado para Oracle Database.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
@Repository
public interface ParkingShiftRateRepository extends JpaRepository<ParkingShiftRateEntity, Long> {

   /**
    * Lista todas las configuraciones de un parqueo.
    *
    * @param parkingId ID del parqueo
    * @return lista de configuraciones (máximo 3: Mañana, Tarde, Noche)
    */
   List<ParkingShiftRateEntity> findByParkingId(Long parkingId);

   /**
    * Busca configuración específica por parqueo y turno.
    * Solo puede haber UNA configuración por combinación parking+shift.
    *
    * @param parkingId ID del parqueo
    * @param shiftId ID del turno
    * @return configuración si existe
    */
   Optional<ParkingShiftRateEntity> findByParkingIdAndShiftId(
         Long parkingId,
         Long shiftId
   );

   /**
    * Elimina todas las configuraciones de un parqueo.
    * Útil al reconfigurar completamente un parqueo.
    *
    * @param parkingId ID del parqueo
    */
   @Modifying
   @Query("DELETE FROM ParkingShiftRateEntity p WHERE p.parkingId = :parkingId")
   void deleteByParkingId(@Param("parkingId") Long parkingId);

   /**
    * Verifica si existe configuración para un parqueo y turno específicos.
    *
    * @param parkingId ID del parqueo
    * @param shiftId ID del turno
    * @return true si ya existe (evita duplicados)
    */
   boolean existsByParkingIdAndShiftId(Long parkingId, Long shiftId);
}