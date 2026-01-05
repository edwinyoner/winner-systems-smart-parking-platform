/**
 * Respuesta genérica de la API (Success)
 */
export interface ApiResponse<T = any> {
  success: boolean;
  message?: string;
  data?: T;
  timestamp?: string;
}

/**
 * Respuesta de error de la API
 */
export interface ApiErrorResponse {
  timestamp: string;
  status: number;
  error: string;
  message: string;
  path: string;
  errors?: ValidationError[]; // Para errores de validación
}

/**
 * Error de validación (campo específico)
 */
export interface ValidationError {
  field: string;
  message: string;
  rejectedValue?: any;
}

/**
 * Tipos de errores comunes
 */
export enum ErrorType {
  VALIDATION_ERROR = 'VALIDATION_ERROR',
  AUTHENTICATION_ERROR = 'AUTHENTICATION_ERROR',
  AUTHORIZATION_ERROR = 'AUTHORIZATION_ERROR',
  NOT_FOUND = 'NOT_FOUND',
  CONFLICT = 'CONFLICT',
  SERVER_ERROR = 'SERVER_ERROR',
  NETWORK_ERROR = 'NETWORK_ERROR'
}

/**
 * Códigos de estado HTTP comunes
 */
export enum HttpStatus {
  OK = 200,
  CREATED = 201,
  NO_CONTENT = 204,
  BAD_REQUEST = 400,
  UNAUTHORIZED = 401,
  FORBIDDEN = 403,
  NOT_FOUND = 404,
  CONFLICT = 409,
  UNPROCESSABLE_ENTITY = 422,
  INTERNAL_SERVER_ERROR = 500,
  SERVICE_UNAVAILABLE = 503
}