package com.winnersystems.smartparking.parking.application.port.output;

import com.winnersystems.smartparking.parking.application.dto.query.PageRequest;
import com.winnersystems.smartparking.parking.application.dto.query.PageResult;
import com.winnersystems.smartparking.parking.domain.model.Customer;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de salida para persistencia de Customer.
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CustomerPersistencePort {

   // ========================= WRITE =========================

   Customer save(Customer customer);

   void delete(Long id);

   // ========================= FIND ÚNICO =========================

   Optional<Customer> findById(Long id);

   Optional<Customer> findByDocument(Long documentTypeId, String documentNumber);

   Optional<Customer> findByEmail(String email);

   Optional<Customer> findByPhone(String phone);

   // ========================= EXISTS =========================

   boolean existsByDocument(Long documentTypeId, String documentNumber);

   boolean existsByEmail(String email);

   boolean existsByPhone(String phone);

   // ========================= LIST (paginado) =========================

   /**
    * Lista todos los clientes con búsqueda y filtro de estado.
    *
    * @param pageRequest parámetros de paginación
    * @param search búsqueda en nombre, apellido, documento (opcional)
    * @param status filtro: "ACTIVE", "DELETED", "ALL" (opcional)
    * @return PageResult con clientes
    */
   PageResult<Customer> findAll(PageRequest pageRequest, String search, String status);

   /**
    * Lista clientes recurrentes (totalVisits > 1).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con clientes recurrentes ordenados por totalVisits DESC
    */
   PageResult<Customer> findRecurrent(PageRequest pageRequest);

   /**
    * Lista clientes con cuenta móvil vinculada (authExternalId NOT NULL).
    *
    * @param pageRequest parámetros de paginación
    * @return PageResult con clientes que tienen app móvil
    */
   PageResult<Customer> findWithMobileAccount(PageRequest pageRequest);

   // ========================= LIST (sin paginación) =========================

   /**
    * Lista todos los clientes activos (deletedAt IS NULL).
    *
    * @return Lista completa de clientes activos
    */
   List<Customer> findAllActive();

   // ========================= COUNT =========================

   long count();

   long countActive();
}