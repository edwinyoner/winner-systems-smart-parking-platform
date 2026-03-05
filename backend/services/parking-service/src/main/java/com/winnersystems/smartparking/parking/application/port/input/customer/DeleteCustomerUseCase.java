package com.winnersystems.smartparking.parking.application.port.input.customer;

/**
 * Puerto de entrada para eliminar (soft delete) un cliente.
 *
 * Responsabilidades:
 * - Marcar cliente como eliminado (deletedAt, deletedBy)
 * - Validar que el cliente exista
 * - Validar que no tenga transacciones activas
 *
 * Reglas de negocio:
 * - Solo soft delete (no eliminación física)
 * - No se puede eliminar si tiene transacciones ACTIVE
 * - Los datos eliminados se mantienen para auditoría
 *
 * Usado por:
 * - Corrección de errores (cliente registrado por error)
 * - Solicitudes de eliminación por privacidad
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface DeleteCustomerUseCase {

   /**
    * Elimina (soft delete) un cliente.
    *
    * @param customerId ID del cliente
    * @param deletedBy ID del usuario que elimina
    *
    * @throws IllegalArgumentException si no existe cliente con ese ID
    * @throws IllegalStateException si tiene transacciones activas
    */
   void deleteCustomer(Long customerId, Long deletedBy);
}