// src/app/core/services/parking/customer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Customer, CustomerUpdateRequest, CustomerFilters } from '../../models/parking/customer.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/customers`;

  constructor(private http: HttpClient) {}

  // ========================= LIST (paginado con filtros) =========================

  /**
   * Obtener todos los clientes con filtros.
   * Soporta búsqueda por nombre, apellido, documento.
   */
  getAll(filters: CustomerFilters = {}): Observable<ParkingPagedResponse<Customer>> {
    let params = new HttpParams()
      .set('pageNumber', (filters.pageNumber ?? 0).toString())
      .set('pageSize', (filters.pageSize ?? 20).toString());

    if (filters.search && filters.search.trim()) {
      params = params.set('search', filters.search.trim());
    }
    if (filters.status && filters.status !== 'ALL') {
      params = params.set('status', filters.status);
    }

    return this.http.get<ParkingPagedResponse<Customer>>(this.apiUrl, { params });
  }

  /**
   * Lista todos los clientes activos (sin paginación).
   * Útil para dropdowns/selectores.
   */
  getAllActive(): Observable<Customer[]> {
    return this.http.get<Customer[]>(`${this.apiUrl}/active`);
  }

  /**
   * Lista clientes recurrentes (totalVisits > 1).
   */
  getRecurrent(pageNumber: number = 0, pageSize: number = 20): Observable<ParkingPagedResponse<Customer>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());
    
    return this.http.get<ParkingPagedResponse<Customer>>(`${this.apiUrl}/recurrent`, { params });
  }

  // ========================= GET (individual) =========================

  /**
   * Obtener cliente por ID.
   */
  getById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener cliente por documento.
   */
  getByDocument(documentTypeId: number, documentNumber: string): Observable<Customer> {
    const params = new HttpParams()
      .set('documentTypeId', documentTypeId.toString())
      .set('documentNumber', documentNumber);
    
    return this.http.get<Customer>(`${this.apiUrl}/by-document`, { params });
  }

  /**
   * Obtener cliente por email.
   */
  getByEmail(email: string): Observable<Customer> {
    const params = new HttpParams().set('email', email);
    return this.http.get<Customer>(`${this.apiUrl}/by-email`, { params });
  }

  /**
   * Obtener cliente por teléfono.
   */
  getByPhone(phone: string): Observable<Customer> {
    const params = new HttpParams().set('phone', phone);
    return this.http.get<Customer>(`${this.apiUrl}/by-phone`, { params });
  }

  // ========================= UPDATE =========================

  /**
   * Actualizar datos de un cliente.
   * NO crea clientes - se crean automáticamente en transaction-entry.
   */
  update(id: number, customer: CustomerUpdateRequest): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer);
  }

  // ========================= DELETE & RESTORE =========================

  /**
   * Eliminar cliente (soft delete).
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Restaurar cliente previamente eliminado.
   */
  restore(id: number): Observable<Customer> {
    return this.http.patch<Customer>(`${this.apiUrl}/${id}/restore`, {});
  }
}