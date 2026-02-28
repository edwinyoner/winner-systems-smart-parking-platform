// src/app/core/services/parking/space-type.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { SpaceType, SpaceTypeRequest } from '../../models/parking/space-type.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class SpaceTypeService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/space-types`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todos los tipos de espacio (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<SpaceType>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<SpaceType>>(this.apiUrl, { params });
  }

  /**
   * Obtener tipos activos (sin paginación)
   */
  getActive(): Observable<SpaceType[]> {
    return this.http.get<SpaceType[]>(`${this.apiUrl}/active`);
  }

  /**
   * Obtener tipo por ID
   */
  getById(id: number): Observable<SpaceType> {
    return this.http.get<SpaceType>(`${this.apiUrl}/${id}`);
  }

  /**
   * Crear tipo de espacio
   */
  create(spaceType: SpaceTypeRequest): Observable<SpaceType> {
    return this.http.post<SpaceType>(this.apiUrl, spaceType);
  }

  /**
   * Actualizar tipo de espacio
   */
  update(id: number, spaceType: SpaceTypeRequest): Observable<SpaceType> {
    return this.http.put<SpaceType>(`${this.apiUrl}/${id}`, spaceType);
  }

  /**
   * Eliminar tipo de espacio (soft delete)
   */
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  /**
   * Activar/Desactivar tipo de espacio
   */
  toggleActive(id: number): Observable<SpaceType> {
    return this.http.patch<SpaceType>(`${this.apiUrl}/${id}/toggle-active`, {});
  }
}