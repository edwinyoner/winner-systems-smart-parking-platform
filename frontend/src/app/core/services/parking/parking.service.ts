// ============================================================
// src/app/core/services/parking/parking.service.ts
// ============================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Parking, ParkingRequest } from '../../models/parking/parking.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class ParkingService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/parkings`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string,
  ): Observable<ParkingPagedResponse<Parking>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (status !== undefined && status !== '') {
      params = params.set('status', status);
    }

    return this.http.get<ParkingPagedResponse<Parking>>(this.apiUrl, { params });
  }

  getActive(): Observable<Parking[]> {
    return this.http.get<Parking[]>(`${this.apiUrl}/active`);
  }

  getById(id: number): Observable<Parking> {
    return this.http.get<Parking>(`${this.apiUrl}/${id}`);
  }

  create(parking: ParkingRequest): Observable<Parking> {
    return this.http.post<Parking>(this.apiUrl, parking);
  }

  update(id: number, parking: ParkingRequest): Observable<Parking> {
    return this.http.put<Parking>(`${this.apiUrl}/${id}`, parking);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<Parking> {
    return this.http.patch<Parking>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  activate(id: number): Observable<Parking> {
    return this.http.patch<Parking>(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivate(id: number): Observable<Parking> {
    return this.http.patch<Parking>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}