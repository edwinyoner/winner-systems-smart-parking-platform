import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { environment } from '../../../environments/environment';
import { 
  User, 
  CreateUserRequest, 
  UpdateUserRequest, 
  UserFilters 
} from '../models/user.model';
import { PaginatedResponse } from '../models/pagination.model';

/**
 * Servicio para gestión de usuarios
 * CRUD completo de usuarios del sistema
 */
@Injectable({
  providedIn: 'root'
})
export class UserService {

  private readonly API_URL = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  // ========== LISTAR USUARIOS ==========

  /**
   * Obtiene lista paginada de usuarios con filtros
   */
  getUsers(filters?: UserFilters): Observable<PaginatedResponse<User>> {
    let params = new HttpParams();

    if (filters) {
      if (filters.search) params = params.set('search', filters.search);
      if (filters.roleId) params = params.set('roleId', filters.roleId.toString());
      if (filters.status !== undefined) params = params.set('status', filters.status.toString());
      if (filters.emailVerified !== undefined) params = params.set('emailVerified', filters.emailVerified.toString());
      if (filters.page !== undefined) params = params.set('page', filters.page.toString());
      if (filters.size !== undefined) params = params.set('size', filters.size.toString());
      if (filters.sortBy) params = params.set('sortBy', filters.sortBy);
      if (filters.sortDirection) params = params.set('sortDirection', filters.sortDirection);
    }

    return this.http.get<PaginatedResponse<User>>(this.API_URL, { params }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Obtiene todos los usuarios sin paginación (para selects)
   */
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/all`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== OBTENER USUARIO POR ID ==========

  /**
   * Obtiene un usuario por su ID
   */
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== CREAR USUARIO ==========

  /**
   * Crea un nuevo usuario
   */
  createUser(request: CreateUserRequest): Observable<User> {
    return this.http.post<User>(this.API_URL, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ACTUALIZAR USUARIO ==========

  /**
   * Actualiza un usuario existente
   */
  updateUser(id: number, request: UpdateUserRequest): Observable<User> {
    return this.http.put<User>(`${this.API_URL}/${id}`, request).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ELIMINAR USUARIO ==========

  /**
   * Elimina (soft delete) un usuario
   */
  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Restaura un usuario eliminado
   */
  restoreUser(id: number): Observable<User> {
    return this.http.post<User>(`${this.API_URL}/${id}/restore`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // ========== ACTIVAR/DESACTIVAR ==========

  /**
   * Activa un usuario
   */
  activateUser(id: number): Observable<User> {
    return this.http.patch<User>(`${this.API_URL}/${id}/activate`, {}).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Desactiva un usuario
   */
  deactivateUser(id: number): Observable<User> {
    return this.http.patch<User>(`${this.API_URL}/${id}/deactivate`, {}).pipe(
      catchError(this.handleError)
    );
  }

  // ========== GESTIÓN DE ROLES ==========

  /**
   * Asigna roles a un usuario
   */
  assignRoles(userId: number, roleIds: number[]): Observable<User> {
    return this.http.post<User>(`${this.API_URL}/${userId}/roles`, { roleIds }).pipe(
      catchError(this.handleError)
    );
  }

  /**
   * Remueve un rol de un usuario
   */
  removeRole(userId: number, roleId: number): Observable<User> {
    return this.http.delete<User>(`${this.API_URL}/${userId}/roles/${roleId}`).pipe(
      catchError(this.handleError)
    );
  }

  // ========== MANEJO DE ERRORES ==========

  private handleError(error: any): Observable<never> {
    console.error('❌ Error en UserService:', error);
    return throwError(() => error);
  }
}