/**
 * Modelo de Rol
 * Representa un rol con sus permisos asociados
 */
export interface Role {
  id: number;
  name: string;
  description: string;
  status: boolean;
  permissions: Permission[];
  userCount?: number; // Cantidad de usuarios con este rol
  createdAt: string;
  updatedAt: string;
  createdBy?: string;
  updatedBy?: string;
}

/**
 * Interface simplificada de Permission para evitar imports circulares
 */
export interface Permission {
  id: number;
  name: string;
  description?: string;
  status: boolean;
}

/**
 * DTO para crear un nuevo rol
 */
export interface CreateRoleRequest {
  name: string;
  description: string;
  permissionIds: number[];
}

/**
 * DTO para actualizar un rol existente
 */
export interface UpdateRoleRequest {
  name?: string;
  description?: string;
  status?: boolean;
  permissionIds?: number[];
}

/**
 * Filtros para búsqueda de roles
 */
export interface RoleFilters {
  search?: string;
  status?: boolean;
  page?: number;
  size?: number;
  sortBy?: string;
  sortDirection?: 'asc' | 'desc';
}