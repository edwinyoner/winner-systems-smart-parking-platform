// src/app/core/models/parking/infraction.model.ts

export interface Infraction {
  id?: number;
  
  infractionCode: string;           // UNIQUE - "INF-2026-001234"
  
  // Relaciones completas:
  parkingId: number;
  parkingName?: string;
  zoneId: number;
  zoneName?: string;
  spaceId?: number;
  spaceCode?: string;
  transactionId?: number;
  vehicleId: number;
  vehiclePlate?: string;
  customerId?: number;
  customerName?: string;
  
  // Tipo y severidad:
  infractionType: InfractionType;
  severity: InfractionSeverity;
  description: string;
  evidence?: string;                // URLs de fotos separadas por comas
  
  // Detección:
  detectedAt: string;
  detectedBy: number;               // Operador que detectó
  detectedByName?: string;
  detectionMethod?: string;         // MANUAL, CAMERA_AI, SENSOR
  
  // Multa:
  fineAmount?: number;
  currency?: string;
  fineDueDate?: string;
  finePaid?: boolean;
  finePaidAt?: string;
  
  // Estado y resolución:
  status: InfractionStatus;
  resolvedAt?: string;
  resolutionType?: string;          // PAID, DISMISSED, ESCALATED
  resolution?: string;
  
  // Notificación:
  notificationSent?: boolean;
  
  createdAt?: string;
  updatedAt?: string;
  deletedAt?: string;
}

export enum InfractionType {
  OVERSTAY = 'OVERSTAY',
  WRONG_SPACE = 'WRONG_SPACE',
  DOUBLE_PARKING = 'DOUBLE_PARKING',
  NO_PAYMENT = 'NO_PAYMENT',
  UNAUTHORIZED = 'UNAUTHORIZED',
  OTHER = 'OTHER'
}

export enum InfractionSeverity {
  MINOR = 'MINOR',
  MODERATE = 'MODERATE',
  SEVERE = 'SEVERE',
  CRITICAL = 'CRITICAL'
}

export enum InfractionStatus {
  PENDING = 'PENDING',
  IN_REVIEW = 'IN_REVIEW',
  RESOLVED = 'RESOLVED',
  ESCALATED = 'ESCALATED'
}

export interface InfractionRequest {
  parkingId: number;
  zoneId: number;
  spaceId?: number;
  transactionId?: number;
  vehicleId: number;
  customerId?: number;
  infractionType: InfractionType;
  severity: InfractionSeverity;
  description: string;
  evidence?: string;
}