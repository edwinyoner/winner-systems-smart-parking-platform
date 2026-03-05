package com.winnersystems.smartparking.parking.application.port.input.customer;

import com.winnersystems.smartparking.parking.application.dto.query.CustomerDto;

/**
 * Puerto de entrada para restaurar un cliente previamente eliminado.
 *
 * Responsabilidades:
 * - Restaurar cliente (deletedAt = null, deletedBy = null)
 * - Validar que el cliente exista
 * - Validar que esté efectivamente eliminado
 *
 * Usado por:
 * - Corrección de eliminaciones accidentales
 * - Reactivación de clientes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface RestoreCustomerUseCase {

   /**
    * Restaura un cliente previamente eliminado.
    *
    * @param customerId ID del cliente
    * @return CustomerDto con el cliente restaurado
    *
    * @throws IllegalArgumentException si no existe cliente con ese ID
    * @throws IllegalStateException si el cliente no está eliminado
    */
   CustomerDto restoreCustomer(Long customerId);
}