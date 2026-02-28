// src/app/core/models/parking/vehicle.model.ts

/**
 * Modelo para Vehículo.
 * Representa un vehículo que ingresa al estacionamiento.
 */
export interface Vehicle {
  id?: number;
  licensePlate: string;            // "ABC-123"
  brand?: string;
  model?: string;
  color?: string;
  year?: number;
  totalVisits?: number;
  firstSeenDate?: string;
  lastSeenDate?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Request para crear/actualizar Vehículo
 */
export interface VehicleRequest {
  licensePlate: string;
  brand?: string;
  model?: string;
  color?: string;
  year?: number;
}