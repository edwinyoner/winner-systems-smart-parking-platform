// src/app/core/models/parking/customer.model.ts

/**
 * Modelo para Cliente.
 * Representa a una persona que usa el estacionamiento.
 */
export interface Customer {
  id?: number;
  documentTypeId: number;
  documentNumber: string;
  firstName: string;
  lastName?: string;
  phone?: string;
  email?: string;
  address?: string;
  registrationDate?: string;
  totalVisits?: number;
  authExternalId?: string;         // ID del usuario en auth-service (si tiene cuenta)
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Request para crear/actualizar Cliente
 */
export interface CustomerRequest {
  documentTypeId: number;
  documentNumber: string;
  firstName: string;
  lastName?: string;
  phone?: string;
  email?: string;
  address?: string;
}