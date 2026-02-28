// src/app/core/services/parking/payment.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { Payment, PaymentRequest, PaymentStatus } from '../../models/parking/payment.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/payments`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todos los pagos (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<Payment>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Payment>>(this.apiUrl, { params });
  }

  /**
   * Obtener pagos por transacción
   */
  getByTransaction(transactionId: number): Observable<Payment[]> {
    return this.http.get<Payment[]>(`${this.apiUrl}/transaction/${transactionId}`);
  }

  /**
   * Obtener pago por ID
   */
  getById(id: number): Observable<Payment> {
    return this.http.get<Payment>(`${this.apiUrl}/${id}`);
  }

  /**
   * Procesar pago
   */
  processPayment(payment: PaymentRequest): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/process`, payment);
  }

  /**
   * Reembolsar pago
   */
  refund(id: number, refundData: {
    refundAmount: number;
    refundReason: string;
    operatorId: number;
  }): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/${id}/refund`, refundData);
  }

  /**
   * Cancelar pago
   */
  cancel(id: number, cancelData: {
    cancelReason: string;
    operatorId: number;
  }): Observable<Payment> {
    return this.http.post<Payment>(`${this.apiUrl}/${id}/cancel`, cancelData);
  }

  /**
   * Obtener reporte de pagos por rango de fechas
   */
  getReportByDateRange(startDate: string, endDate: string): Observable<any> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate);
    return this.http.get<any>(`${this.apiUrl}/report/date-range`, { params });
  }
}