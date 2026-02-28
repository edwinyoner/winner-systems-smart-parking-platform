// src/app/core/services/parking/infraction.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Infraction, InfractionRequest, InfractionStatus } from '../../models/parking/infraction.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class InfractionService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/infractions`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todas las infracciones (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<Infraction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Infraction>>(this.apiUrl, { params });
  }

  /**
   * Obtener infracciones pendientes
   */
  getPending(page: number = 0, size: number = 20): Observable<PaginatedResponse<Infraction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Infraction>>(`${this.apiUrl}/pending`, { params });
  }

  /**
   * Obtener infracción por ID
   */
  getById(id: number): Observable<Infraction> {
    return this.http.get<Infraction>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener infracciones por transacción
   */
  getByTransaction(transactionId: number): Observable<Infraction[]> {
    return this.http.get<Infraction[]>(`${this.apiUrl}/transaction/${transactionId}`);
  }

  /**
   * Obtener infracciones por vehículo
   */
  getByVehicle(vehicleId: number, page: number = 0, size: number = 20): Observable<PaginatedResponse<Infraction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Infraction>>(`${this.apiUrl}/vehicle/${vehicleId}`, { params });
  }

  /**
   * Obtener infracciones por cliente
   */
  getByCustomer(customerId: number, page: number = 0, size: number = 20): Observable<PaginatedResponse<Infraction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Infraction>>(`${this.apiUrl}/customer/${customerId}`, { params });
  }

  /**
   * Crear infracción
   */
  create(infraction: InfractionRequest): Observable<Infraction> {
    return this.http.post<Infraction>(this.apiUrl, infraction);
  }

  /**
   * Actualizar infracción
   */
  update(id: number, infraction: InfractionRequest): Observable<Infraction> {
    return this.http.put<Infraction>(`${this.apiUrl}/${id}`, infraction);
  }

  /**
   * Resolver infracción
   */
  resolve(id: number, resolveData: {
    resolvedBy: number;
    notes?: string;
  }): Observable<Infraction> {
    return this.http.post<Infraction>(`${this.apiUrl}/${id}/resolve`, resolveData);
  }

  /**
   * Cancelar infracción
   */
  cancel(id: number, cancelData: {
    cancelledBy: number;
    notes?: string;
  }): Observable<Infraction> {
    return this.http.post<Infraction>(`${this.apiUrl}/${id}/cancel`, cancelData);
  }

  /**
   * Eliminar infracción (soft delete)
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener estadísticas de infracciones
   */
  getStatistics(startDate?: string, endDate?: string): Observable<any> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<any>(`${this.apiUrl}/statistics`, { params });
  }
}