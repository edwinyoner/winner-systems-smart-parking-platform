// src/app/core/services/parking/customer-vehicle.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { CustomerVehicle, CustomerVehicleFilters } from '../../models/parking/customer-vehicle.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerVehicleService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/customer-vehicles`;

  constructor(private http: HttpClient) {}

  // ========================= LIST =========================

  /**
   * Lista todas las relaciones con paginación.
   */
  getAll(filters: CustomerVehicleFilters = {}): Observable<ParkingPagedResponse<CustomerVehicle>> {
    let params = new HttpParams()
      .set('pageNumber', (filters.pageNumber ?? 0).toString())
      .set('pageSize', (filters.pageSize ?? 20).toString());

    return this.http.get<ParkingPagedResponse<CustomerVehicle>>(this.apiUrl, { params });
  }

  /**
   * Lista todos los vehículos usados por un cliente.
   */
  getVehiclesByCustomer(customerId: number): Observable<CustomerVehicle[]> {
    return this.http.get<CustomerVehicle[]>(`${this.apiUrl}/by-customer/${customerId}`);
  }

  /**
   * Lista todos los clientes que han usado un vehículo.
   */
  getCustomersByVehicle(vehicleId: number): Observable<CustomerVehicle[]> {
    return this.http.get<CustomerVehicle[]>(`${this.apiUrl}/by-vehicle/${vehicleId}`);
  }

  /**
   * Lista combinaciones frecuentes (usageCount >= minUsageCount).
   */
  getFrequentCombinations(
    minUsageCount: number = 5,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<CustomerVehicle>> {
    const params = new HttpParams()
      .set('minUsageCount', minUsageCount.toString())
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<CustomerVehicle>>(`${this.apiUrl}/frequent`, { params });
  }

  // ========================= GET =========================

  /**
   * Obtener relación específica por ID.
   */
  getById(id: number): Observable<CustomerVehicle> {
    return this.http.get<CustomerVehicle>(`${this.apiUrl}/${id}`);
  }

  /**
   * Obtener relación específica por customerId + vehicleId.
   */
  getByCustomerAndVehicle(customerId: number, vehicleId: number): Observable<CustomerVehicle> {
    const params = new HttpParams()
      .set('customerId', customerId.toString())
      .set('vehicleId', vehicleId.toString());

    return this.http.get<CustomerVehicle>(`${this.apiUrl}/by-relation`, { params });
  }

  /**
   * Verificar si existe relación.
   */
  exists(customerId: number, vehicleId: number): Observable<boolean> {
    const params = new HttpParams()
      .set('customerId', customerId.toString())
      .set('vehicleId', vehicleId.toString());

    return this.http.get<boolean>(`${this.apiUrl}/exists`, { params });
  }
}