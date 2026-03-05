// src/app/core/models/parking/payment.model.ts

/**
 * Estados posibles de un Pago
 */
export type PaymentStatus = 'COMPLETED' | 'REFUNDED' | 'CANCELLED';

/**
 * DTO para Payment (solo consulta).
 * 
 * IMPORTANTE: Payment NO se crea manualmente.
 * Se crea automáticamente cuando se ejecuta Transaction.processPayment().
 */
export interface PaymentDto {
  id: number;
  transactionId: number;          // UNIQUE - relación 1:1
  
  // Tipo de pago
  paymentTypeId: number;
  paymentTypeName?: string;       // Expandido desde PaymentType
  
  // Montos
  amount: number;
  currency: string;               // "PEN"
  
  // Fecha y referencia
  paymentDate: string;
  referenceNumber?: string;
  
  // Operador que cobró
  operatorId: number;
  operatorName?: string;      
  
  // Estado
  status: PaymentStatus;
  
  // Devoluciones (refund)
  refundAmount?: number;
  refundDate?: string;
  refundReason?: string;
  refundOperatorId?: number;  
  netAmount?: number;             // Computed: amount - refundAmount
  
  // Auditoría
  createdAt: string;
}

/**
 * Request para procesar devolución (refund).
 */
export interface RefundPaymentRequest {
  refundAmount: number;
  reason: string;
}