package com.winnersystems.smartparking.parking.application.port.input.customer;

import com.winnersystems.smartparking.parking.application.dto.command.CreateCustomerCommand;
import com.winnersystems.smartparking.parking.application.dto.query.CustomerDto;

/**
 * Puerto de entrada para crear un nuevo cliente/conductor.
 *
 * Responsabilidades:
 * - Validar que el documento no esté duplicado
 * - Crear Customer con datos básicos
 * - Inicializar contadores (totalVisits = 0)
 * - Registrar fecha de primera visita
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CreateCustomerUseCase {

   /**
    * Crea un nuevo cliente en el sistema.
    *
    * @param command datos del cliente a crear
    * @return CustomerDto con el cliente creado
    *
    * @throws IllegalArgumentException
    *         si ya existe un cliente con ese documento
    */
   CustomerDto createCustomer(CreateCustomerCommand command);
}