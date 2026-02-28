// src/app/core/models/parking/payment-type.model.ts

export interface PaymentType {
  id?: number;
  code: string;
  name: string;
  description?: string;
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface PaymentTypeRequest {
  code: string;
  name: string;
  description?: string;
  status?: boolean;
}