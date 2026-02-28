package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.domain.model.Customer;

import java.util.Optional;

/**
 * Puerto de salida para persistencia de Customer.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CustomerPersistencePort {

   /**
    * Guarda un cliente (crear o actualizar).
    *
    * @param customer cliente a guardar
    * @return cliente guardado
    */
   Customer save(Customer customer);

   /**
    * Busca un cliente por su ID.
    *
    * @param id ID del cliente
    * @return Optional con el cliente si existe
    */
   Optional<Customer> findById(Long id);

   /**
    * Busca un cliente por tipo y número de documento.
    *
    * @param documentTypeId tipo de documento
    * @param documentNumber número de documento
    * @return Optional con el cliente si existe
    */
   Optional<Customer> findByDocument(Long documentTypeId, String documentNumber);

   /**
    * Busca un cliente por email.
    *
    * @param email email del cliente
    * @return Optional con el cliente si existe
    */
   Optional<Customer> findByEmail(String email);

   /**
    * Busca un cliente por teléfono.
    *
    * @param phone teléfono del cliente
    * @return Optional con el cliente si existe
    */
   Optional<Customer> findByPhone(String phone);
}