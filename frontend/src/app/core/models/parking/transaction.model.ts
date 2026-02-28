// src/app/core/models/parking/transaction.model.ts

/**
 * Modelo para Transacción.
 * Representa el ciclo completo de entrada-salida-pago de un vehículo.
 */
export interface Transaction {
  id?: number;
  vehicleId: number;
  customerId: number;
  parkingSpaceId: number;
  zoneId: number;
  rateId: number;
  entryDocumentTypeId: number;
  entryDocumentNumber: string;
  exitDocumentTypeId?: number;
  exitDocumentNumber?: string;
  entryTime: string;
  exitTime?: string;
  durationMinutes?: number;
  entryOperatorId: number;
  exitOperatorId?: number;
  entryMethod?: string;            // "MANUAL", "CAMERA_AI", "SENSOR"
  exitMethod?: string;
  entryPhotoUrl?: string;
  exitPhotoUrl?: string;
  entryPlateConfidence?: number;
  exitPlateConfidence?: number;
  calculatedAmount?: number;
  discountAmount?: number;
  totalAmount?: number;
  currency: string;
  status: TransactionStatus;
  paymentStatus: TransactionPaymentStatus;
  receiptSent?: boolean;
  receiptSentAt?: string;
  receiptWhatsAppStatus?: string;
  receiptEmailStatus?: string;
  notes?: string;
  cancellationReason?: string;
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Estados posibles de una Transacción
 */
export enum TransactionStatus {
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED'
}

/**
 * Estados de Pago de una Transacción
 */
export enum TransactionPaymentStatus {
  PENDING = 'PENDING',
  PAID = 'PAID',
  OVERDUE = 'OVERDUE'
}

/**
 * Request para registrar Entrada
 */
export interface TransactionEntryRequest {
  zoneId: number;
  spaceId: number;
  plateNumber: string;
  documentTypeId: number;
  documentNumber: string;
  customerName?: string;
  customerEmail?: string;
  customerPhone?: string;
  operatorId: number;
  entryMethod?: string;
  photoUrl?: string;
  plateConfidence?: number;
  notes?: string;
}

/**
 * Request para registrar Salida
 */
export interface TransactionExitRequest {
  transactionId?: number;
  plateNumber?: string;
  exitDocumentTypeId: number;
  exitDocumentNumber: string;
  operatorId: number;
  exitMethod?: string;
  photoUrl?: string;
  plateConfidence?: number;
  notes?: string;
}

/**
 * Response detallado de Transacción
 */
export interface TransactionDetailResponse {
  id: number;
  status: TransactionStatus;
  paymentStatus: TransactionPaymentStatus;
  vehicle: {
    id: number;
    plateNumber: string;
    brand?: string;
    color?: string;
  };
  customer: {
    id: number;
    documentNumber: string;
    name: string;
    phone?: string;
    email?: string;
  };
  zone: {
    id: number;
    name: string;
    code: string;
  };
  space: {
    id: number;
    code: string;
  };
  entryTime: string;
  exitTime?: string;
  durationMinutes?: number;
  durationFormatted: string;
  entryDocument: {
    number: string;
  };
  exitDocument?: {
    number: string;
  };
  rate: {
    id: number;
    name: string;
    hourlyRate: number;
  };
  calculatedAmount?: number;
  discountAmount: number;
  totalAmount?: number;
  currency: string;
  payment?: {
    id: number;
    amount: number;
    referenceNumber?: string;
    paymentDate: string;
    status: string;
  };
  entryPhotoUrl?: string;
  exitPhotoUrl?: string;
  entryPlateConfidence?: number;
  exitPlateConfidence?: number;
  receiptSent: boolean;
  receiptSentAt?: string;
  receiptWhatsAppStatus?: string;
  receiptEmailStatus?: string;
  notes?: string;
  cancellationReason?: string;
  createdAt: string;
  updatedAt: string;
}