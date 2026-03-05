// src/app/core/models/parking/customer.model.ts

/**
 * Modelo para Cliente/Conductor.
 * Los clientes se crean automáticamente en transaction-entry.
 */
export interface Customer {
  id: number;
  documentTypeId: number;
  documentTypeName?: string;    
  documentNumber: string;
  firstName: string;
  lastName: string;
  fullName?: string;                // Computed en backend
  phone?: string;
  email?: string;
  address?: string;
  
  registrationDate?: string;        // Fecha de registro inicial
  firstSeenDate?: string;           // Primera vez en el sistema
  lastSeenDate?: string;            // Última vez visto
  totalVisits?: number;             // Contador de visitas
  isRecurrent?: boolean;            // totalVisits > 1
  
  authExternalId?: number;          // App móvil (Fase 2)
  hasMobileAccount?: boolean;       // Computed
  
  createdAt?: string;
  updatedAt?: string;
  deletedAt?: string;               // Soft delete
}

/**
 * Request para ACTUALIZAR cliente.
 * NO se usa para crear (se crean en transaction-entry).
 */
export interface CustomerUpdateRequest {
  firstName: string;
  lastName: string;
  phone?: string;
  email?: string;
  address?: string;
}

/**
 * Filtros de búsqueda para listado.
 */
export interface CustomerFilters {
  pageNumber?: number;
  pageSize?: number;
  search?: string;      // Busca en nombre, apellido, documento
  status?: 'ACTIVE' | 'DELETED' | 'ALL';
}