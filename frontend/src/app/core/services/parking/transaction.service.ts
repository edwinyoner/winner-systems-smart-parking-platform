// src/app/core/services/parking/transaction.service.ts
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { 
  Transaction, 
  TransactionEntryRequest, 
  TransactionExitRequest,
  TransactionDetailResponse,
  TransactionStatus,
  TransactionPaymentStatus
} from '../../models/parking/transaction.model';
import { PaginatedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/parking-service/api/v1/transactions`;

  constructor(private http: HttpClient) {}

  /**
   * Obtener todas las transacciones (con paginación)
   */
  getAll(page: number = 0, size: number = 20): Observable<PaginatedResponse<Transaction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Transaction>>(this.apiUrl, { params });
  }

  /**
   * Obtener transacciones activas (vehículos dentro)
   */
  getActive(page: number = 0, size: number = 50): Observable<PaginatedResponse<TransactionDetailResponse>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<TransactionDetailResponse>>(`${this.apiUrl}/active`, { params });
  }

  /**
   * Obtener transacción por ID
   */
  getById(id: number): Observable<TransactionDetailResponse> {
    return this.http.get<TransactionDetailResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Buscar transacción por placa
   */
  searchByPlate(plateNumber: string): Observable<Transaction[]> {
    const params = new HttpParams().set('plateNumber', plateNumber);
    return this.http.get<Transaction[]>(`${this.apiUrl}/search/plate`, { params });
  }

  /**
   * Buscar transacción activa por placa
   */
  getActiveByPlate(plateNumber: string): Observable<TransactionDetailResponse> {
    return this.http.get<TransactionDetailResponse>(`${this.apiUrl}/active/plate/${plateNumber}`);
  }

  /**
   * Registrar entrada de vehículo
   */
  registerEntry(entry: TransactionEntryRequest): Observable<TransactionDetailResponse> {
    return this.http.post<TransactionDetailResponse>(`${this.apiUrl}/entry`, entry);
  }

  /**
   * Registrar salida de vehículo
   */
  registerExit(exit: TransactionExitRequest): Observable<TransactionDetailResponse> {
    return this.http.post<TransactionDetailResponse>(`${this.apiUrl}/exit`, exit);
  }

  /**
   * Calcular monto de transacción
   */
  calculateAmount(transactionId: number): Observable<{
    transactionId: number;
    durationMinutes: number;
    calculatedAmount: number;
    discountAmount: number;
    totalAmount: number;
    currency: string;
  }> {
    return this.http.get<any>(`${this.apiUrl}/${transactionId}/calculate`);
  }

  /**
   * Cancelar transacción
   */
  cancel(id: number, cancelData: {
    cancellationReason: string;
    operatorId: number;
  }): Observable<Transaction> {
    return this.http.post<Transaction>(`${this.apiUrl}/${id}/cancel`, cancelData);
  }

  /**
   * Enviar recibo por WhatsApp
   */
  sendReceiptWhatsApp(transactionId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${transactionId}/receipt/whatsapp`, {});
  }

  /**
   * Enviar recibo por Email
   */
  sendReceiptEmail(transactionId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/${transactionId}/receipt/email`, {});
  }

  /**
   * Obtener transacciones por zona
   */
  getByZone(zoneId: number, page: number = 0, size: number = 20): Observable<PaginatedResponse<Transaction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Transaction>>(`${this.apiUrl}/zone/${zoneId}`, { params });
  }

  /**
   * Obtener transacciones por cliente
   */
  getByCustomer(customerId: number, page: number = 0, size: number = 20): Observable<PaginatedResponse<Transaction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Transaction>>(`${this.apiUrl}/customer/${customerId}`, { params });
  }

  /**
   * Obtener transacciones por vehículo
   */
  getByVehicle(vehicleId: number, page: number = 0, size: number = 20): Observable<PaginatedResponse<Transaction>> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Transaction>>(`${this.apiUrl}/vehicle/${vehicleId}`, { params });
  }

  /**
   * Obtener transacciones por rango de fechas
   */
  getByDateRange(startDate: string, endDate: string, page: number = 0, size: number = 20): Observable<PaginatedResponse<Transaction>> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<PaginatedResponse<Transaction>>(`${this.apiUrl}/date-range`, { params });
  }

  /**
   * Obtener estadísticas de transacciones
   */
  getStatistics(startDate?: string, endDate?: string): Observable<any> {
    let params = new HttpParams();
    if (startDate) params = params.set('startDate', startDate);
    if (endDate) params = params.set('endDate', endDate);
    return this.http.get<any>(`${this.apiUrl}/statistics`, { params });
  }
}