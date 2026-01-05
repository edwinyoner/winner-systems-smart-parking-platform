import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError, map } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Permission, 
  CreatePermissionRequest, 
  UpdatePermissionRequest, 
  PermissionFilters,
  PermissionGroup 
} from '../models/permission.model';
import { PaginatedResponse } from '../models/pagination.model';
import { TokenService } from './token.service';

/**
 * Servicio para gestión de permisos
 * CRUD completo de permisos y verificación de autorización
 */
@Injectable({
  providedIn: 'root'
})
export class PermissionService {

  private readonly API_URL = `${environment.apiUrl}/permissions`;

  constructor(
    private http: HttpClient,
    private tokenService: TokenService
  ) {}

  // ========== LISTAR PERMISOS ==========

  /**
   * Obtiene lista paginada de permisos con filtros
   */
  getPermissions(filters?: PermissionFilters): Observable<PaginatedResponse<Permission>> {
    let params = new HttpParams();

    if (filters) {
      if (filters.search) params = params.set('search', filters.search);
      if (filters.module) params = params.set('module', filters.module);
      if (filters.status !== undefined) params = params.set('status', filters.status.toString());
      if (filters.page !== undefined) params = params.set('page', filters.page.toString());
      if (filters.size !== undefined) params = params.set('size', filters.size.toString());
      if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
      if (filters.sortDirection) params = params.set('sortDirection', filters.sortDirection);
    }

    return this.http.get<PaginatedResponse<Permission>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene todos los permisos sin paginación
   */
  getAllPermissions(): Observable<Permission[]> {
    return this.http.get<Permission[]>(`${this.API_URL}/all`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene permisos agrupados por módulo
   */
  getPermissionsGrouped(): Observable<PermissionGroup[]> {
    return this.getAllPermissions().pipe(
      map(permissions => this.groupPermissionsByModule(permissions))
    );
  }

  // ========== OBTENER PERMISO POR ID ==========

  /**
   * Obtiene un permiso por su ID
   */
  getPermissionById(id: number): Observable<Permission> {
    return this.http.get<Permission>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== CREAR PERMISO ==========

  /**
   * Crea un nuevo permiso
   */
  createPermission(request: CreatePermissionRequest): Observable<Permission> {
    return this.http.post<Permission>(this.API_URL, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ACTUALIZAR PERMISO ==========

  /**
   * Actualiza un permiso existente
   */
  updatePermission(id: number, request: UpdatePermissionRequest): Observable<Permission> {
    return this.http.put<Permission>(`${this.API_URL}/${id}`, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ELIMINAR PERMISO ==========

  /**
   * Elimina un permiso (solo si no está asignado a ningún rol)
   */
  deletePermission(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== VERIFICACIÓN DE PERMISOS ==========

  /**
   * Verifica si el usuario actual tiene un permiso específico
   */
  hasPermission(permission: string): boolean {
    const userData = this.tokenService.getUserFromToken();
    
    if (!userData || !userData.permissions) {
      return false;
    }

    return userData.permissions.includes(permission);
  }

  /**
   * Verifica si el usuario tiene al menos uno de los permisos
   */
  hasAnyPermission(permissions: string[]): boolean {
    return permissions.some(permission => this.hasPermission(permission));
  }

  /**
   * Verifica si el usuario tiene todos los permisos
   */
  hasAllPermissions(permissions: string[]): boolean {
    return permissions.every(permission => this.hasPermission(permission));
  }

  /**
   * Verifica si el usuario tiene un rol específico
   */
  hasRole(role: string): boolean {
    const userData = this.tokenService.getUserFromToken();
    
    if (!userData || !userData.roles) {
      return false;
    }

    return userData.roles.includes(role);
  }

  /**
   * Verifica si el usuario tiene al menos uno de los roles
   */
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  // ========== UTILIDADES ==========

  /**
   * Agrupa permisos por módulo
   */
  private groupPermissionsByModule(permissions: Permission[]): PermissionGroup[] {
    const grouped = permissions.reduce((acc, permission) => {
      const module = permission.module || 'general';
      
      if (!acc[module]) {
        acc[module] = [];
      }
      
      acc[module].push(permission);
      return acc;
    }, {} as { [key: string]: Permission[] });

    return Object.entries(grouped).map(([module, perms]) => ({
      module,
      permissions: perms
    }));
  }

  // ========== MANEJO DE ERRORES ==========

  private handleError(error: any): Observable<never> {
    console.error('❌ Error en PermissionService:', error);
    return throwError(() => error);
  }
}