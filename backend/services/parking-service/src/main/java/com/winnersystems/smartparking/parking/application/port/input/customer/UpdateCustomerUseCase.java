package com.winnersystems.smartparking.parking.application.port.input.customer;

import com.winnersystems.smartparking.parking.application.dto.command.UpdateCustomerCommand;
import com.winnersystems.smartparking.parking.application.dto.query.CustomerDto;

/**
 * Puerto de entrada para actualizar información de un cliente existente.
 *
 * Responsabilidades:
 * - Actualizar datos personales (nombre, apellido)
 * - Actualizar información de contacto (phone, email, address)
 * - Validar que el cliente exista
 * - Validar que no esté eliminado (soft delete)
 *
 * Campos NO actualizables:
 * - documentTypeId y documentNumber (identificación legal, no cambia)
 * - totalVisits, firstSeenDate, lastSeenDate (calculados automáticamente)
 * - authExternalId (solo por LinkMobileAccountUseCase en Fase 2)
 *
 * Usado por:
 * - Clientes que actualizan su perfil
 * - Operadores que corrigen datos incorrectos
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface UpdateCustomerUseCase {

   /**
    * Actualiza la información de un cliente.
    *
    * @param customerId ID del cliente
    * @param command datos a actualizar
    * @return CustomerDto con información actualizada
    *
    * @throws IllegalArgumentException si no existe cliente con ese ID
    * @throws IllegalStateException si el cliente está eliminado
    */
   CustomerDto updateCustomer(Long customerId, UpdateCustomerCommand command);
}