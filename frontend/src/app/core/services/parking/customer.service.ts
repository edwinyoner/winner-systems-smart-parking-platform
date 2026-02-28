// src/app/core/services/parking/customer.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Customer, CustomerRequest } from '../../models/parking/customer.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/customers`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todos los clientes (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<Customer>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Customer>>(this.apiUrl, { params });
  }

  /**
   * Buscar clientes por nombre o documento
   */
  search(query: string, page: number = 0, size: number = 20): Observable<PaginatedResponse<Customer>> {
    const params = new HttpParams()
      .set('query', query)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Customer>>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Obtener cliente por ID
   */
  getById(id: number): Observable<Customer> {
    return this.http.get<Customer>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener cliente por documento
   */
  getByDocument(documentTypeId: number, documentNumber: string): Observable<Customer> {
    const params = new HttpParams()
      .set('documentTypeId', documentTypeId.toString())
      .set('documentNumber', documentNumber);
    return this.http.get<Customer>(`${this.apiUrl}/by-document`, { params });
  }

  /**
   * Crear cliente
   */
  create(customer: CustomerRequest): Observable<Customer> {
    return this.http.post<Customer>(this.apiUrl, customer);
  }

  /**
   * Actualizar cliente
   */
  update(id: number, customer: CustomerRequest): Observable<Customer> {
    return this.http.put<Customer>(`${this.apiUrl}/${id}`, customer);
  }

  /**
   * Eliminar cliente (soft delete)
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}