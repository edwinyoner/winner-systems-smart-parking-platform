import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  Role, 
  CreateRoleRequest, 
  UpdateRoleRequest, 
  RoleFilters 
} from '../models/role.model';
import { PaginatedResponse } from '../models/pagination.model';

/**
 * Servicio para gestión de roles
 * CRUD completo de roles del sistema
 */
@Injectable({
  providedIn: 'root'
})
export class RoleService {

  private readonly API_URL = `${environment.apiUrl}/roles`;

  constructor(private http: HttpClient) {}

  // ========== LISTAR ROLES ==========

  /**
   * Obtiene lista paginada de roles con filtros
   */
  getRoles(filters?: RoleFilters): Observable<PaginatedResponse<Role>> {
    let params = new HttpParams();

    if (filters) {
      if (filters.search) params = params.set('search', filters.search);
      if (filters.status !== undefined) params = params.set('status', filters.status.toString());
      if (filters.page !== undefined) params = params.set('page', filters.page.toString());
      if (filters.size !== undefined) params = params.set('size', filters.size.toString());
      if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
      if (filters.sortDirection) params = params.set('sortDirection', filters.sortDirection);
    }

    return this.http.get<PaginatedResponse<Role>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene todos los roles sin paginación (para selects)
   */
  getAllRoles(): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.API_URL}/all`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== OBTENER ROL POR ID ==========

  /**
   * Obtiene un rol por su ID
   */
  getRoleById(id: number): Observable<Role> {
    return this.http.get<Role>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== CREAR ROL ==========

  /**
   * Crea un nuevo rol
   */
  createRole(request: CreateRoleRequest): Observable<Role> {
    return this.http.post<Role>(this.API_URL, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ACTUALIZAR ROL ==========

  /**
   * Actualiza un rol existente
   */
  updateRole(id: number, request: UpdateRoleRequest): Observable<Role> {
    return this.http.put<Role>(`${this.API_URL}/${id}`, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ELIMINAR ROL ==========

  /**
   * Elimina un rol (solo si no tiene usuarios asignados)
   */
  deleteRole(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== GESTIÓN DE PERMISOS ==========

  /**
   * Asigna permisos a un rol
   */
  assignPermissions(roleId: number, permissionIds: number[]): Observable<Role> {
    return this.http.post<Role>(`${this.API_URL}/${roleId}/permissions`, { permissionIds }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Remueve un permiso de un rol
   */
  removePermission(roleId: number, permissionId: number): Observable<Role> {
    return this.http.delete<Role>(`${this.API_URL}/${roleId}/permissions/${permissionId}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== MANEJO DE ERRORES ==========

  private handleError(error: any): Observable<never> {
    console.error('❌ Error en RoleService:', error);
    return throwError(() => error);
  }
}