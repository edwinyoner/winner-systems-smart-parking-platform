// ============================================================
// src/app/core/services/parking/space.service.ts
// ============================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Space, SpaceRequest } from '../../models/parking/space.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class SpaceService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/spaces`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string,
  ): Observable<ParkingPagedResponse<Space>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (status !== undefined && status !== '') {
      params = params.set('status', status);
    }

    return this.http.get<ParkingPagedResponse<Space>>(this.apiUrl, { params });
  }

  getByZoneId(zoneId: number): Observable<Space[]> {
    return this.http.get<Space[]>(`${this.apiUrl}/zone/${zoneId}`);
  }

  getAvailableByZoneId(zoneId: number): Observable<Space[]> {
    return this.http.get<Space[]>(`${this.apiUrl}/zone/${zoneId}/available`);
  }

  getById(id: number): Observable<Space> {
    return this.http.get<Space>(`${this.apiUrl}/${id}`);
  }

  create(space: SpaceRequest): Observable<Space> {
    return this.http.post<Space>(this.apiUrl, space);
  }

  createBatch(spaces: SpaceRequest[]): Observable<Space[]> {
    return this.http.post<Space[]>(`${this.apiUrl}/batch`, spaces);
  }

  update(id: number, space: SpaceRequest): Observable<Space> {
    return this.http.put<Space>(`${this.apiUrl}/${id}`, space);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<Space> {
    return this.http.patch<Space>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  markAsAvailable(id: number): Observable<Space> {
    return this.http.patch<Space>(`${this.apiUrl}/${id}/available`, {});
  }

  markAsOccupied(id: number): Observable<Space> {
    return this.http.patch<Space>(`${this.apiUrl}/${id}/occupied`, {});
  }

  setInMaintenance(id: number): Observable<Space> {
    return this.http.patch<Space>(`${this.apiUrl}/${id}/maintenance`, {});
  }
}