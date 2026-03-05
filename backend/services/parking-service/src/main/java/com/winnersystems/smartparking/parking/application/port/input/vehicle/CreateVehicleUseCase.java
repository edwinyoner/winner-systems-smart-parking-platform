package com.winnersystems.smartparking.parking.application.port.input.vehicle;

import com.winnersystems.smartparking.parking.application.dto.command.CreateVehicleCommand;
import com.winnersystems.smartparking.parking.application.dto.query.VehicleDto;

/**
 * Puerto de entrada para crear un nuevo vehículo.
 *
 * Responsabilidades:
 * - Validar que la placa no esté duplicada
 * - Normalizar placa a mayúsculas sin espacios
 * - Crear Vehicle con datos básicos
 * - Inicializar contadores (totalVisits = 0)
 * - Registrar fecha de primera visita
 *
 * Reglas de negocio:
 * - La placa (licensePlate) debe ser única
 * - La placa se normaliza automáticamente (mayúsculas, sin espacios)
 * - Color y marca son opcionales (ayuda visual para operadores)
 * - Si la placa ya existe, se retorna IllegalArgumentException
 *
 * Usado por:
 * - RecordEntryUseCase cuando detecta vehículo nuevo
 * - Registro manual de vehículos frecuentes
 *
 * @author Edwin Yoner - Winner Systems - Smart Parking Platform
 * @version 1.0
 */
public interface CreateVehicleUseCase {

   /**
    * Crea un nuevo vehículo en el sistema.
    *
    * @param command datos del vehículo a crear
    * @return VehicleDto con el vehículo creado
    *
    * @throws IllegalArgumentException
    *         si ya existe un vehículo con esa placa
    */
   VehicleDto createVehicle(CreateVehicleCommand command);
}