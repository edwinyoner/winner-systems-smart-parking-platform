/**
 * Respuesta paginada genérica
 */
export interface PaginatedResponse<T> {
  content: T[];
  page: PageInfo;
}

/**
 * Información de paginación
 */
export interface PageInfo {
  number: number;        // Número de página actual (0-indexed)
  size: number;          // Tamaño de página
  totalElements: number; // Total de elementos
  totalPages: number;    // Total de páginas
  first: boolean;        // Es la primera página
  last: boolean;         // Es la última página
  empty: boolean;        // La página está vacía
}

/**
 * Parámetros de paginación para requests
 */
export interface PageRequest {
  page?: number;
  size?: number;
  sort?: string; // formato: "field,direction" ej: "createdAt,desc"
}

/**
 * Parámetros de ordenamiento
 */
export interface SortParams {
  field: string;
  direction: 'asc' | 'desc';
}