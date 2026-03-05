package com.winnersystems.smartparking.parking.application.port.input.customer;

import com.winnersystems.smartparking.parking.application.dto.query.CustomerDto;

/**
 * Puerto de entrada para obtener información de clientes.
 *
 * Responsabilidades:
 * - Buscar cliente por ID
 * - Buscar cliente por documento (tipo + número)
 * - Buscar cliente por email
 * - Buscar cliente por teléfono
 *
 * Usado por:
 * - Vista de detalle de cliente
 * - Validaciones en RecordEntryUseCase
 * - Búsquedas rápidas por operadores
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface GetCustomerUseCase {

   /**
    * Obtiene un cliente por su ID.
    *
    * @param customerId ID del cliente
    * @return CustomerDto con información del cliente
    *
    * @throws IllegalArgumentException si no existe cliente con ese ID
    */
   CustomerDto getCustomerById(Long customerId);

   /**
    * Obtiene un cliente por su documento.
    *
    * @param documentTypeId tipo de documento (DNI, CE, etc.)
    * @param documentNumber número de documento
    * @return CustomerDto si existe, null si no existe
    */
   CustomerDto getCustomerByDocument(Long documentTypeId, String documentNumber);

   /**
    * Obtiene un cliente por su email.
    *
    * @param email email del cliente
    * @return CustomerDto si existe, null si no existe
    */
   CustomerDto getCustomerByEmail(String email);

   /**
    * Obtiene un cliente por su teléfono.
    *
    * @param phone teléfono del cliente
    * @return CustomerDto si existe, null si no existe
    */
   CustomerDto getCustomerByPhone(String phone);
}