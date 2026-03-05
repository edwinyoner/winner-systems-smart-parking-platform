package com.winnersystems.smartparking.parking.application.port.input.infraction;

import com.winnersystems.smartparking.parking.application.dto.command.CreateInfractionCommand;
import com.winnersystems.smartparking.parking.application.dto.query.InfractionDto;

/**
 * Puerto de entrada para crear una nueva infracción.
 *
 * Responsabilidades:
 * - Validar que parking, zone y vehicle existan
 * - Crear Infraction con estado PENDING
 * - Generar código único de infracción (INF-YYYY-NNNNNN)
 * - Registrar método de detección (MANUAL, CAMERA_AI, SENSOR, SYSTEM)
 * - Asociar evidencia fotográfica si existe
 *
 * Reglas de negocio:
 * - El vehículo debe existir (vehicleId NOT NULL)
 * - Parking y Zone deben existir
 * - Transaction es opcional (puede haber infracciones sin transacción)
 * - El código se genera automáticamente después del save
 * - Estado inicial siempre es PENDING
 *
 * Usado por:
 * - Operadores al detectar infracciones manualmente
 * - Sistema automático al detectar vehículos vencidos (OVERSTAY)
 * - IA-SERVICE al detectar infracciones por cámara
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CreateInfractionUseCase {

   /**
    * Crea una nueva infracción en el sistema.
    *
    * @param command datos de la infracción a crear
    * @return InfractionDto con la infracción creada y código generado
    *
    * @throws IllegalArgumentException
    *         si el parking, zone o vehicle no existen
    */
   InfractionDto createInfraction(CreateInfractionCommand command);
}