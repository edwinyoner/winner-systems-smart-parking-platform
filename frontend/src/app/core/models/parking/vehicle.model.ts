// src/app/core/models/parking/vehicle.model.ts

/**
 * Modelo para Vehículo.
 * Identificado SOLAMENTE por placa (licensePlate).
 * NO incluye marca/modelo/color - se agregarán con integración SUNARP.
 * Los vehículos se crean automáticamente en transaction-entry.
 */
export interface Vehicle {
  id: number;
  licensePlate: string;             // UNIQUE - placa del vehículo
  displayName?: string;          
  totalVisits?: number;             // Contador de visitas
  isRecurrent?: boolean;     
  firstSeenDate?: string;           // Primera vez registrado
  lastSeenDate?: string;            // Última vez visto
  createdAt?: string;
  updatedAt?: string;
  deletedAt?: string;               // Soft delete
}

/**
 * Request para ACTUALIZAR vehículo.
 * NO se usa para crear (se crean en transaction-entry).
 */
export interface VehicleUpdateRequest {
  licensePlate: string;             // Solo para normalización
}

/**
 * Filtros de búsqueda para listado.
 */
export interface VehicleFilters {
  pageNumber?: number;
  pageSize?: number;
  search?: string;      // Busca en placa
  status?: 'ACTIVE' | 'DELETED' | 'ALL';
}