// ============================================================
// src/app/core/services/parking/zone.service.ts
// ============================================================
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Zone, ZoneRequest } from '../../models/parking/zone.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class ZoneService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/zones`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string,
  ): Observable<ParkingPagedResponse<Zone>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (status !== undefined && status !== '') {
      params = params.set('status', status);
    }

    return this.http.get<ParkingPagedResponse<Zone>>(this.apiUrl, { params });
  }

  getActive(): Observable<Zone[]> {
    return this.http.get<Zone[]>(`${this.apiUrl}/active`);
  }

  getByParkingId(parkingId: number): Observable<Zone[]> {
    return this.http.get<Zone[]>(`${this.apiUrl}/parking/${parkingId}`);
  }

  getById(id: number): Observable<Zone> {
    return this.http.get<Zone>(`${this.apiUrl}/${id}`);
  }

  create(zone: ZoneRequest): Observable<Zone> {
    return this.http.post<Zone>(this.apiUrl, zone);
  }

  update(id: number, zone: ZoneRequest): Observable<Zone> {
    return this.http.put<Zone>(`${this.apiUrl}/${id}`, zone);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleStatus(id: number): Observable<Zone> {
    return this.http.patch<Zone>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  activate(id: number): Observable<Zone> {
    return this.http.patch<Zone>(`${this.apiUrl}/${id}/activate`, {});
  }

  configureShiftRates(zoneId: number, body: {
    configurations: { shiftId: number; rateId: number }[]
  }): Observable<any> {
    return this.http.post(`${this.apiUrl}/${zoneId}/shift-rates`, body);
  }

  deactivate(id: number): Observable<Zone> {
    return this.http.patch<Zone>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}