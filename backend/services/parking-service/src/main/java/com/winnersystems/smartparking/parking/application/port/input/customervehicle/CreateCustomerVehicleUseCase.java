package com.winnersystems.smartparking.parking.application.port.input.customervehicle;

import com.winnersystems.smartparking.parking.application.dto.command.CreateCustomerVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.query.CustomerVehicleDto;

/**
 * Puerto de entrada para registrar la relación entre un cliente y un vehículo.
 *
 * Responsabilidades:
 * - Validar que el cliente exista
 * - Validar que el vehículo exista
 * - Verificar que la relación no esté duplicada
 * - Crear registro CustomerVehicle
 * - Incrementar usageCount si ya existe (opcional)
 *
 * Reglas de negocio:
 * - La combinación customerId + vehicleId debe ser única
 * - Si ya existe, se puede incrementar usageCount
 * - Se registra automáticamente en RecordEntryUseCase
 *
 * Usado por:
 * - RecordEntryUseCase (automático al crear transacción)
 * - Registro manual de relaciones frecuentes
 * - Pre-carga de datos para sugerencias
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CreateCustomerVehicleUseCase {

   /**
    * Registra la relación entre un cliente y un vehículo.
    *
    * Si la relación ya existe, incrementa usageCount.
    *
    * @param command datos de la relación (customerId, vehicleId)
    * @return CustomerVehicleDto con la relación creada o actualizada
    *
    * @throws IllegalArgumentException
    *         si el cliente o vehículo no existen
    */
   CustomerVehicleDto createCustomerVehicle(CreateCustomerVehicleCommand command);
}