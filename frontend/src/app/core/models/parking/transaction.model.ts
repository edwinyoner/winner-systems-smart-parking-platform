// src/app/core/models/parking/transaction.model.ts

/**
 * Estados posibles de una Transacción
 */
export type TransactionStatus = 'ACTIVE' | 'COMPLETED' | 'CANCELLED';

/**
 * Estados de Pago de una Transacción
 */
export type TransactionPaymentStatus = 'PENDING' | 'PAID' | 'OVERDUE';

/**
 * Métodos de registro (entrada/salida)
 */
export type TransactionMethod = 'MANUAL' | 'CAMERA_AI' | 'SENSOR';

// ========================= REQUEST DTOs =========================

/**
 * Request para registrar ENTRADA de vehículo.
 * 
 * IMPORTANTE: Solo se registra la PLACA. 
 * Información adicional del vehículo vendrá de SUNARP en futuras versiones.
 */
export interface TransactionEntryRequest {
  // Ubicación
  parkingId: number;              // ✅ AGREGADO - Backend lo requiere
  zoneId: number;
  spaceId: number;                // ✅ CORREGIDO (era parkingSpaceId)

  // Vehículo - SOLO PLACA
  plateNumber: string;

  // Documento
  documentTypeId: number;
  documentNumber: string;

  // Cliente - NOMBRES SEPARADOS
  customerFirstName: string;      // ✅ CORREGIDO (antes era customerName)
  customerLastName: string;       // ✅ AGREGADO
  customerEmail?: string;
  customerPhone?: string;

  // Operador
  operatorId: number;

  // Evidencia (opcional)
  entryMethod?: TransactionMethod;
  photoUrl?: string;
  plateConfidence?: number;

  // Observaciones
  notes?: string;
}

/**
 * Request para registrar SALIDA de vehículo.
 */
export interface TransactionExitRequest {
  // Identificar transacción (uno u otro)
  transactionId?: number;
  plateNumber?: string;

  // Documento de salida (SEGURIDAD - debe coincidir con entrada)
  exitDocumentTypeId: number;
  exitDocumentNumber: string;

  // Operador
  operatorId: number;

  // Evidencia (opcional)
  exitMethod?: TransactionMethod;
  photoUrl?: string;
  plateConfidence?: number;

  // Observaciones
  notes?: string;
}

/**
 * Request para PROCESAR PAGO de transacción.
 */
export interface ProcessPaymentRequest {
  // transactionId se setea desde el path /{id}/payment
  paymentTypeId: number;
  amountPaid: number;
  operatorId: number;

  // Opcionales
  referenceNumber?: string;
  sendReceipt?: boolean;          // Default true
  notes?: string;
}

// ========================= RESPONSE DTOs =========================

/**
 * DTO simplificado para LISTADOS de transacciones.
 */
export interface TransactionDto {
  id: number;
  plateNumber: string;
  customerName: string;
  parkingName: string;            // ✅ AGREGADO
  zoneName: string;
  spaceCode: string;
  entryTime: string;
  exitTime?: string;
  duration: string;               // Formato: "3h 30min"
  totalAmount?: number;
  status: TransactionStatus;
  paymentStatus: TransactionPaymentStatus;
  createdAt: string;
}

/**
 * DTO completo con TODOS los detalles de una transacción.
 */
export interface TransactionDetailResponse {
  id: number;
  status: TransactionStatus;
  paymentStatus: TransactionPaymentStatus;

  // ========================= ENTIDADES ANIDADAS =========================

  vehicle: {
    id: number;
    plateNumber: string;
    // brand, color no existen todavía (futuro SUNARP)
  };

  customer: {
    id: number;
    documentNumber: string;
    name: string;                 // fullName del backend
    phone?: string;
    email?: string;
  };

  parking: {                      // ✅ AGREGADO - Backend sí lo retorna
    id: number;
    name: string;
    code: string;
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

  // ========================= TIEMPOS =========================

  entryTime: string;
  exitTime?: string;
  durationMinutes?: number;
  durationFormatted: string;      // "3h 30min"

  // ========================= DOCUMENTOS =========================

  entryDocument: {
    number: string;
  };
  exitDocument?: {
    number: string;
  };

  // ========================= TARIFA =========================

  rate: {
    id: number;
    name: string;
    hourlyRate: number;
  };

  // ========================= MONTOS =========================

  calculatedAmount?: number;
  discountAmount: number;
  totalAmount?: number;
  currency: string;

  // ========================= PAGO =========================

  payment?: {
    id: number;
    amount: number;
    referenceNumber?: string;
    paymentDate: string;
    status: string;
  };

  // ========================= EVIDENCIA =========================

  entryPhotoUrl?: string;
  exitPhotoUrl?: string;
  entryPlateConfidence?: number;
  exitPlateConfidence?: number;

  // ========================= COMPROBANTE =========================

  receiptSent: boolean;
  receiptSentAt?: string;
  receiptWhatsAppStatus?: string;
  receiptEmailStatus?: string;

  // ========================= OBSERVACIONES =========================

  notes?: string;
  cancellationReason?: string;

  // ========================= AUDITORÍA =========================

  createdAt: string;
  updatedAt: string;
}

/**
 * DTO para transacciones ACTIVAS (monitoreo en tiempo real).
 */
export interface ActiveTransactionDto {
  id: number;

  // Vehículo
  vehicleId: number;
  plateNumber: string;

  // Cliente
  customerId: number;
  customerName: string;
  customerPhone?: string;
  customerEmail?: string;
  documentNumber: string;

  // Ubicación
  parkingId: number;           
  parkingName: string;
  zoneId: number;
  zoneName: string;
  spaceId: number;
  spaceCode: string;

  // Tiempos
  entryTime: string;
  elapsedMinutes: number;
  elapsedFormatted: string;       // "2h 15min"

  // Cálculo en tiempo real
  hourlyRate: number;
  currentAmount: number;          // Monto acumulado ACTUAL
  currency: string;

  // Alertas
  maxRecommendedMinutes: number;
  isOverdue: boolean;
  requiresAttention: boolean;

  // Operador
  entryOperatorId: number;
  entryMethod: TransactionMethod;

  // Evidencia
  entryPhotoUrl?: string;
  plateConfidence?: number;

  // Observaciones
  notes?: string;

  // Auditoría
  createdAt: string;
}