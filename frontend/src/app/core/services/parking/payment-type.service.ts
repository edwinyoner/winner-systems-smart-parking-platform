// src/app/core/services/parking/payment-type.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { PaymentType, PaymentTypeRequest } from '../../models/parking/payment-type.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root',
})
export class PaymentTypeService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/payment-types`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string,
  ): Observable<ParkingPagedResponse<PaymentType>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());

    if (search && search.trim()) {
      params = params.set('search', search.trim());
    }
    if (status !== undefined && status !== '') {
      params = params.set('status', status);
    }

    return this.http.get<ParkingPagedResponse<PaymentType>>(this.apiUrl, { params });
  }

  getActive(): Observable<PaymentType[]> {
    return this.http.get<PaymentType[]>(`${this.apiUrl}/active`);
  }

  getById(id: number): Observable<PaymentType> {
    return this.http.get<PaymentType>(`${this.apiUrl}/${id}`);
  }

  create(paymentType: PaymentTypeRequest): Observable<PaymentType> {
    return this.http.post<PaymentType>(this.apiUrl, paymentType);
  }

  update(id: number, paymentType: PaymentTypeRequest): Observable<PaymentType> {
    return this.http.put<PaymentType>(`${this.apiUrl}/${id}`, paymentType);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleActive(id: number): Observable<PaymentType> {
    return this.http.patch<PaymentType>(`${this.apiUrl}/${id}/toggle-status`, {});
  }

  activate(id: number): Observable<PaymentType> {
    return this.http.patch<PaymentType>(`${this.apiUrl}/${id}/activate`, {});
  }

  deactivate(id: number): Observable<PaymentType> {
    return this.http.patch<PaymentType>(`${this.apiUrl}/${id}/deactivate`, {});
  }
}