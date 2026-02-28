package com.winnersystems.smartparking.parking.application.port.input.transaction;

import com.winnersystems.smartparking.parking.application.dto.command.RecordExitCommand;
import com.winnersystems.smartparking.parking.application.dto.query.TransactionDetailDto;

/**
 * Puerto de entrada para registrar la SALIDA de un vehículo del estacionamiento.
 *
 * Responsabilidades:
 * - Buscar transacción activa por ID o placa
 * - Validar documento de salida vs entrada (seguridad anti-robo)
 * - Calcular duración del estacionamiento
 * - Calcular monto a pagar según tarifa y duración
 * - Actualizar Transaction a estado COMPLETED
 * - Liberar espacio (cambiar a AVAILABLE)
 * - Registrar evidencia fotográfica de salida (si existe)
 *
 * Reglas de negocio:
 * - La transacción debe estar en estado ACTIVE
 * - El documento de salida DEBE coincidir con el de entrada
 * - El monto se calcula con la tarifa vigente al momento de ENTRADA
 * - El espacio se libera automáticamente
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RecordExitUseCase {

   /**
    * Registra la salida de un vehículo del estacionamiento.
    *
    * @param command datos de la salida
    * @return TransactionDetailDto con todos los detalles actualizados incluyendo monto calculado
    *
    * @throws com.winnersystems.smartparking.parking.domain.exception.InvalidTransactionStateException
    *         si la transacción no está en estado ACTIVE
    * @throws com.winnersystems.smartparking.parking.domain.exception.DocumentMismatchException
    *         si el documento de salida no coincide con el de entrada
    */
   TransactionDetailDto recordExit(RecordExitCommand command);
}