// src/app/core/services/parking/vehicle.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Vehicle, VehicleRequest } from '../../models/parking/vehicle.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/vehicles`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todos los vehículos (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<Vehicle>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Vehicle>>(this.apiUrl, { params });
  }

  /**
   * Buscar vehículos por placa
   */
  search(plateNumber: string, page: number = 0, size: number = 20): Observable<PaginatedResponse<Vehicle>> {
    const params = new HttpParams()
      .set('plateNumber', plateNumber)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Vehicle>>(`${this.apiUrl}/search`, { params });
  }

  /**
   * Obtener vehículo por ID
   */
  getById(id: number): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener vehículo por placa
   */
  getByPlate(plateNumber: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(`${this.apiUrl}/plate/${plateNumber}`);
  }

  /**
   * Crear vehículo
   */
  create(vehicle: VehicleRequest): Observable<Vehicle> {
    return this.http.post<Vehicle>(this.apiUrl, vehicle);
  }

  /**
   * Actualizar vehículo
   */
  update(id: number, vehicle: VehicleRequest): Observable<Vehicle> {
    return this.http.put<Vehicle>(`${this.apiUrl}/${id}`, vehicle);
  }

  /**
   * Eliminar vehículo (soft delete)
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}