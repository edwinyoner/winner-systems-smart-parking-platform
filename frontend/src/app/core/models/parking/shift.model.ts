// src/app/core/models/parking/shift.model.ts
export interface Shift {
  id?: number;
  name: string;
  code: string;
  description?: string;
  startTime: string;  // "HH:mm:ss" formato
  endTime: string;    // "HH:mm:ss" formato
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface ShiftRequest {
  name: string;
  code: string;
  description?: string;
  startTime: string;
  endTime: string;
  status?: boolean;
}