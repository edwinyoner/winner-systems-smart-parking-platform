// src/app/core/models/parking/customer-vehicle.model.ts

/**
 * Modelo para relación Cliente-Vehículo (histórico).
 * Tabla M:N que registra qué clientes han usado qué vehículos.
 */
export interface CustomerVehicle {
  id: number;
  customerId: number;
  customerName?: string;            // Computed - firstName + lastName
  customerDocument?: string;        // Computed - documentNumber
  vehicleId: number;
  vehiclePlate?: string;            // Computed - licensePlate
  usageCount?: number;              // Cuántas veces esta combinación
  createdAt?: string;               // Primera vez que se registró
  createdBy?: number;               // ID del operador
}

/**
 * Filtros de búsqueda.
 */
export interface CustomerVehicleFilters {
  pageNumber?: number;
  pageSize?: number;
  customerId?: number;              // Filtrar por cliente
  vehicleId?: number;               // Filtrar por vehículo
  minUsageCount?: number;           // Solo frecuentes (default 5)
}