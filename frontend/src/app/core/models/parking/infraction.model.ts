// src/app/core/models/parking/infraction.model.ts

/**
 * Modelo para Infracción.
 * Representa una violación de las reglas del estacionamiento.
 */
export interface Infraction {
  id?: number;
  transactionId?: number;
  vehicleId?: number;
  customerId?: number;
  infractionType: string;          // "OVERSTAY", "NO_PAYMENT", "UNAUTHORIZED_PARKING"
  description: string;
  amount?: number;
  currency?: string;
  status: InfractionStatus;
  reportedAt: string;
  reportedBy: number;
  resolvedAt?: string;
  resolvedBy?: number;
  notes?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Estados posibles de una Infracción
 */
export enum InfractionStatus {
  PENDING = 'PENDING',
  RESOLVED = 'RESOLVED',
  CANCELLED = 'CANCELLED'
}

/**
 * Request para crear/actualizar Infracción
 */
export interface InfractionRequest {
  transactionId?: number;
  vehicleId?: number;
  customerId?: number;
  infractionType: string;
  description: string;
  amount?: number;
  currency?: string;
  reportedBy: number;
  notes?: string;
}