package com.winnersystems.smartparking.parking.application.port.input.transaction;

import com.winnersystems.smartparking.parking.application.dto.command.RecordEntryCommand;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDetailDto;

/**
 * Puerto de entrada para registrar la ENTRADA de un vehículo al estacionamiento.
 *
 * Responsabilidades:
 * - Validar disponibilidad del espacio
 * - Buscar o crear vehículo por placa
 * - Buscar o crear cliente por documento
 * - Verificar que el vehículo no esté ya dentro
 * - Crear Transaction en estado ACTIVE
 * - Actualizar estado del espacio a OCCUPIED
 * - Registrar evidencia fotográfica (si existe)
 *
 * Reglas de negocio:
 * - El espacio debe estar AVAILABLE
 * - La zona debe estar operativa (horario, status)
 * - El vehículo NO debe tener otra transacción ACTIVE
 * - Debe existir una tarifa configurada para zona+turno
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RecordEntryUseCase {

   /**
    * Registra la entrada de un vehículo al estacionamiento.
    *
    * @param command datos de la entrada
    * @return TransactionDetailDto con todos los detalles de la transacción creada
    *
    * @throws com.winnersystems.smartparking.parking.domain.exception.VehicleAlreadyInsideException
    *         si el vehículo ya tiene una transacción activa
    * @throws com.winnersystems.smartparking.parking.domain.exception.SpaceNotAvailableException
    *         si el espacio no está disponible
    * @throws com.winnersystems.smartparking.parking.domain.exception.ZoneNotOperationalException
    *         si la zona no está operativa
    */
   TransactionDetailDto recordEntry(RecordEntryCommand command);
}