// src/app/core/services/parking/payment.service.ts

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';

/**
 * Servicio para gestión de PAGOS.
 * 
 * ⚠️ IMPORTANTE: ESTE SERVICIO NO FUNCIONA ACTUALMENTE
 * 
 * El backend NO tiene endpoints REST para Payment.
 * Payment se crea automáticamente en TransactionService.processPayment().
 * 
 * Para obtener información de pagos:
 * - Usa TransactionService.getById() → response.payment
 * - O TransactionService.getActiveByPlate() → response.payment
 * 
 * Este servicio está preparado para cuando se implementen
 * endpoints REST de consulta de Payment en el futuro.
 */
@Injectable({
  providedIn: 'root'
})
export class PaymentService {
  
  constructor(private http: HttpClient) {
    console.warn(
      '⚠️ PaymentService: Backend NO tiene endpoints REST para Payment. ' +
      'Usa TransactionService.getById() para obtener payment info.'
    );
  }

  // ========================= FUTURO - NO IMPLEMENTADO =========================

  /**
   * ⚠️ ENDPOINT NO EXISTE EN BACKEND
   * Usar TransactionService.getById() → response.payment
   */
  getById(id: number): Observable<never> {
    return throwError(() => new Error(
      'Payment endpoints no implementados. Usa TransactionService.getById()'
    ));
  }

  /**
   * ⚠️ ENDPOINT NO EXISTE EN BACKEND
   * Usar TransactionService.getById() → response.payment
   */
  getByTransaction(transactionId: number): Observable<never> {
    return throwError(() => new Error(
      'Payment endpoints no implementados. Usa TransactionService.getById()'
    ));
  }

  // TODO: Implementar cuando backend tenga REST adapter para Payment
}