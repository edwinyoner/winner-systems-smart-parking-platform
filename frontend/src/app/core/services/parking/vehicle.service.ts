// src/app/core/services/parking/vehicle.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Vehicle, VehicleUpdateRequest, VehicleFilters } from '../../models/parking/vehicle.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/vehicles`;

  constructor(private http: HttpClient) {}

  // ========================= LIST (paginado con filtros) =========================

  /**
   * Obtener todos los vehículos con filtros.
   * Soporta búsqueda por placa.
   */
  getAll(filters: VehicleFilters = {}): Observable<ParkingPagedResponse<Vehicle>> {
    let params = new HttpParams()
      .set('pageNumber', (filters.pageNumber ?? 0).toString())
      .set('pageSize', (filters.pageSize ?? 20).toString());

    if (filters.search && filters.search.trim()) {
      params = params.set('search', filters.search.trim());
    }
    if (filters.status && filters.status !== 'ALL') {
      params = params.set('status', filters.status);
    }

    return this.http.get<ParkingPagedResponse<Vehicle>>(this.apiUrl, { params });
  }

  /**
   * Lista todos los vehículos activos (sin paginación).
   * Útil para dropdowns/selectores.
   */
  getAllActive(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(`${this.apiUrl}/active`);
  }

  /**
   * Lista vehículos recurrentes (totalVisits > 1).
   */
  getRecurrent(pageNumber: number = 0, pageSize: number = 20): Observable<ParkingPagedResponse<Vehicle>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());
    
    return this.http.get<ParkingPagedResponse<Vehicle>>(`${this.apiUrl}/recurrent`, { params });
  }

  // ========================= GET (individual) =========================

  /**
   * Obtener vehículo por ID.
   */
  getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener vehículo por placa.
   */
  getByPlate(licensePlate: string): Observable<Vehicle> {
    const params = new HttpParams().set('licensePlate', licensePlate.trim().toUpperCase());
    return this.http.get<Vehicle>(`${this.apiUrl}/by-plate`, { params });
  }

  // ========================= UPDATE =========================

  /**
   * Actualizar datos de un vehículo.
   * NO crea vehículos - se crean automáticamente en transaction-entry.
   */
  update(id: number, vehicle: VehicleUpdateRequest): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/${id}`, vehicle);
  }

  // ========================= DELETE & RESTORE =========================

  /**
   * Eliminar vehículo (soft delete).
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Restaurar vehículo previamente eliminado.
   */
  restore(id: number): Observable<Vehicle> {
    return this.http.patch<Vehicle>(`${this.apiUrl}/${id}/restore`, {});
  }
}