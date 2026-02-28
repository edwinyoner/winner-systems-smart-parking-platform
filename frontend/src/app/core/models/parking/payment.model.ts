// src/app/core/models/parking/payment.model.ts

/**
 * Modelo para Pago.
 * Representa un pago realizado por una transacción.
 */
export interface Payment {
  id?: number;
  transactionId: number;
  paymentTypeId: number;
  amount: number;
  currency: string;                // "PEN"
  referenceNumber?: string;
  paymentDate: string;
  operatorId: number;
  status: PaymentStatus;
  notes?: string;
  refundAmount?: number;
  refundDate?: string;
  refundOperatorId?: number;
  refundReason?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Estados posibles de un Pago
 */
export enum PaymentStatus {
  COMPLETED = 'COMPLETED',
  PENDING = 'PENDING',
  REFUNDED = 'REFUNDED',
  CANCELLED = 'CANCELLED'
}

/**
 * Request para procesar Pago
 */
export interface PaymentRequest {
  transactionId: number;
  paymentTypeId: number;
  amountPaid: number;
  referenceNumber?: string;
  operatorId: number;
  notes?: string;
  sendReceipt?: boolean;
}