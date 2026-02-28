package com.winnersystems.smartparking.parking.application.port.input.parkingshiftrate;

import com.winnersystems.smartparking.parking.application.dto.command.ConfigureParkingShiftRatesCommand;
import com.winnersystems.smartparking.parking.application.dto.query.ParkingShiftRateDto;

import java.util.List;

/**
 * Caso de uso para configurar tarifas por turno en un parqueo.
 * Esta configuración se aplica a TODAS las zonas del parqueo.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface ConfigureParkingShiftRatesUseCase {

   /**
    * Configura las tarifas por turno para un parqueo.
    * Reemplaza las configuraciones existentes.
    * Esta configuración se hereda automáticamente a todas las zonas del parqueo.
    *
    * @param command comando con configuraciones
    * @return lista de configuraciones guardadas
    */
   List<ParkingShiftRateDto> configureShiftRates(ConfigureParkingShiftRatesCommand command);

   /**
    * Obtiene las configuraciones de un parqueo.
    *
    * @param parkingId ID del parqueo
    * @return lista de configuraciones
    */
   List<ParkingShiftRateDto> getParkingShiftRates(Long parkingId);

   /**
    * Elimina una configuración específica.
    *
    * @param configId ID de la configuración
    */
   void deleteShiftRateConfig(Long configId);

   /**
    * Activa/desactiva una configuración.
    *
    * @param configId ID de la configuración
    * @return configuración actualizada
    */
   ParkingShiftRateDto toggleShiftRateStatus(Long configId);
}