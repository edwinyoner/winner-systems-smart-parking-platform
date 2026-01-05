/**
 * Modelo de Usuario
 * Representa un usuario del sistema Smart Parking
 */
export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNumber?: string;
  profilePicture?: string;
  emailVerified: boolean;
  status: boolean;
  roles: Role[];
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}

/**
 * Interface simplificada de Role para evitar imports circulares
 */
export interface Role {
  id: number;
  name: string;
  description?: string;
  status: boolean;
}

/**
 * DTO para crear un nuevo usuario
 */
export interface CreateUserRequest {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  phoneNumber?: string;
  roleIds: number[];
}

/**
 * DTO para actualizar un usuario existente
 */
export interface UpdateUserRequest {
  firstName?: string;
  lastName?: string;
  email?: string;
  phoneNumber?: string;
  profilePicture?: string;
  status?: boolean;
  roleIds?: number[];
}

/**
 * Filtros para búsqueda de usuarios
 */
export interface UserFilters {
  search?: string;
  roleId?: number;
  status?: boolean;
  emailVerified?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}