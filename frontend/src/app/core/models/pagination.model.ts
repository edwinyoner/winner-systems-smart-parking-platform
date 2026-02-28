// ============================================================
// MODELO PARA AUTH-SERVICE
// Spring Page serializa: number, size (NO modificar, funciona)
// ============================================================

/**
 * Respuesta paginada del auth-service.
 * Usa la serialización estándar de Spring Page.
 */
export interface PaginatedResponse<T> {
  content:       T[];
  totalElements: number;
  totalPages:    number;
  number:        number;   // página actual (base 0)
  size:          number;   // tamaño de página
  first:         boolean;
  last:          boolean;
  empty:         boolean;
}

// ============================================================
// MODELO PARA PARKING-SERVICE
// Record custom serializa: pageNumber, pageSize
// ============================================================

/**
 * Respuesta paginada del parking-service.
 * Usa el record personalizado PagedResponse<T> de Java.
 *
 * Backend devuelve:
 *   pageNumber   → número de página actual (base 0)
 *   pageSize     → tamaño de página
 *   totalElements
 *   totalPages
 *   first / last / hasNext / hasPrevious
 */
export interface ParkingPagedResponse<T> {
  content:       T[];
  pageNumber:    number;   // base 0
  pageSize:      number;
  totalElements: number;
  totalPages:    number;
  first:         boolean;
  last:          boolean;
  hasNext:       boolean;
  hasPrevious:   boolean;
}

// ============================================================
// UTILIDADES COMUNES
// ============================================================

/**
 * Parámetros de paginación para requests
 */
export interface PageRequest {
  page?: number;
  size?: number;
  sort?: string; // "field,direction" → ej: "createdAt,desc"
}

/**
 * Parámetros de ordenamiento
 */
export interface SortParams {
  field:     string;
  direction: 'asc' | 'desc';
}