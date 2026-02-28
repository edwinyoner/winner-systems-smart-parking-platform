// src/app/core/models/parking/rate.model.ts

/**
 * Modelo para Tarifa.
 * Representa el costo por hora de estacionamiento.
 */
export interface Rate {
  id?: number;
  name: string;                    // "Tarifa Estándar Huaraz"
  description?: string;
  amount: number;                  // 5.00
  currency: string;                // "PEN"
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Request para crear/actualizar Tarifa
 */
export interface RateRequest {
  name: string;
  description?: string;
  amount: number;
  currency: string;
  status?: boolean;
}