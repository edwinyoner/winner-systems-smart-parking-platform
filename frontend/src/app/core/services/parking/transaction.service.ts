// src/app/core/services/parking/transaction.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import {
  TransactionDetailResponse,
  ActiveTransactionDto,
  TransactionDto,
  TransactionEntryRequest,
  TransactionExitRequest,
  ProcessPaymentRequest
} from '../../models/parking/transaction.model';
import { ParkingPagedResponse } from '../../models/pagination.model';

@Injectable({
  providedIn: 'root'
})
export class TransactionService {
  private apiUrl = `${environment.apiUrl}/parking-service/transactions`;

  constructor(private http: HttpClient) {}

  // ========================= OPERACIONES DE TRANSACCIÓN =========================

  /**
   * Registra la ENTRADA de un vehículo al estacionamiento.
   * 
   * POST /transactions/entry
   */
  recordEntry(request: TransactionEntryRequest): Observable<TransactionDetailResponse> {
    return this.http.post<TransactionDetailResponse>(
      `${this.apiUrl}/entry`,
      request
    );
  }

  /**
   * Registra la SALIDA de un vehículo del estacionamiento.
   * 
   * POST /transactions/exit
   */
  recordExit(request: TransactionExitRequest): Observable<TransactionDetailResponse> {
    return this.http.post<TransactionDetailResponse>(
      `${this.apiUrl}/exit`,
      request
    );
  }

  /**
   * Procesa el PAGO de una transacción.
   * 
   * POST /transactions/{id}/payment
   */
  processPayment(
    transactionId: number,
    request: ProcessPaymentRequest
  ): Observable<TransactionDetailResponse> {
    return this.http.post<TransactionDetailResponse>(
      `${this.apiUrl}/${transactionId}/payment`,
      request
    );
  }

  // ========================= CONSULTAS INDIVIDUALES =========================

  /**
   * Obtiene los detalles COMPLETOS de una transacción.
   * 
   * GET /transactions/{id}
   */
  getById(id: number): Observable<TransactionDetailResponse> {
    return this.http.get<TransactionDetailResponse>(`${this.apiUrl}/${id}`);
  }

  /**
   * Busca transacción ACTIVA por placa de vehículo.
   * 
   * GET /transactions/active/plate/{plateNumber}
   */
  getActiveByPlate(plateNumber: string): Observable<TransactionDetailResponse | null> {
    return this.http.get<TransactionDetailResponse>(
      `${this.apiUrl}/active/plate/${plateNumber}`
    );
  }

  // ========================= LISTADOS - TRANSACCIONES ACTIVAS =========================

  /**
   * Lista TODAS las transacciones activas.
   * 
   * GET /transactions/active?pageNumber=0&pageSize=20
   */
  getActiveTransactions(
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<ActiveTransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<ActiveTransactionDto>>(
      `${this.apiUrl}/active`,
      { params }
    );
  }

  /**
   * Lista transacciones activas de una ZONA.
   * 
   * GET /transactions/active/zone/{zoneId}
   */
  getActiveByZone(
    zoneId: number,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<ActiveTransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<ActiveTransactionDto>>(
      `${this.apiUrl}/active/zone/${zoneId}`,
      { params }
    );
  }

  /**
   * Busca transacciones activas por PLACA.
   * 
   * GET /transactions/active/search?plateNumber=ABC
   */
  searchActiveByPlate(
    plateNumber: string,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<ActiveTransactionDto>> {
    const params = new HttpParams()
      .set('plateNumber', plateNumber)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<ActiveTransactionDto>>(
      `${this.apiUrl}/active/search`,
      { params }
    );
  }

  /**
   * Lista transacciones que EXCEDEN tiempo recomendado.
   * 
   * GET /transactions/active/overdue
   */
  getOverdueTransactions(
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<ActiveTransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<ActiveTransactionDto>>(
      `${this.apiUrl}/active/overdue`,
      { params }
    );
  }

  // ========================= LISTADOS - HISTORIAL COMPLETO =========================

  /**
   * Lista TODAS las transacciones.
   * 
   * GET /transactions?pageNumber=0&pageSize=20&sortBy=...&sortDirection=...
   */
  getAll(
    pageNumber: number = 0,
    pageSize: number = 20,
    sortBy: string = 'createdAt',
    sortDirection: string = 'DESC'
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString())
      .set('sortBy', sortBy)
      .set('sortDirection', sortDirection);

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      this.apiUrl,
      { params }
    );
  }

  /**
   * Lista transacciones por RANGO DE FECHAS.
   * 
   * GET /transactions/date-range
   */
  getByDateRange(
    startDate: string,
    endDate: string,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('startDate', startDate)
      .set('endDate', endDate)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      `${this.apiUrl}/date-range`,
      { params }
    );
  }

  /**
   * Lista transacciones por ESTADO.
   * 
   * GET /transactions/status/{status}
   */
  getByStatus(
    status: string,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      `${this.apiUrl}/status/${status}`,
      { params }
    );
  }

  /**
   * Lista transacciones por ESTADO DE PAGO.
   * 
   * GET /transactions/payment-status/{paymentStatus}
   */
  getByPaymentStatus(
    paymentStatus: string,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      `${this.apiUrl}/payment-status/${paymentStatus}`,
      { params }
    );
  }

  /**
   * Lista transacciones de una ZONA.
   * 
   * GET /transactions/zone/{zoneId}
   */
  getByZone(
    zoneId: number,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      `${this.apiUrl}/zone/${zoneId}`,
      { params }
    );
  }

  /**
   * Busca transacciones por PLACA (histórico).
   * 
   * GET /transactions/search?plateNumber=ABC
   */
  searchByPlate(
    plateNumber: string,
    pageNumber: number = 0,
    pageSize: number = 20
  ): Observable<ParkingPagedResponse<TransactionDto>> {
    const params = new HttpParams()
      .set('plateNumber', plateNumber)
      .set('pageNumber', pageNumber.toString())
      .set('pageSize', pageSize.toString());

    return this.http.get<ParkingPagedResponse<TransactionDto>>(
      `${this.apiUrl}/search`,
      { params }
    );
  }
}