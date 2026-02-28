// ============================================================
// src/app/core/models/parking/parking.model.ts
// ============================================================
export interface Parking {
  id?: number;
  name: string;
  code: string;
  description?: string;
  address: string;
  latitude?: number;
  longitude?: number;
  managerId?: number;
  managerName?: string;
  totalZones?: number;
  totalSpaces?: number;
  availableSpaces?: number;
  occupiedSpaces?: number;      
  occupancyPercentage?: number;  
  status: 'ACTIVE' | 'INACTIVE' | 'MAINTENANCE' | 'OUT_OF_SERVICE';  
  createdAt?: string;
  updatedAt?: string;
}

export interface ParkingRequest {
  name: string;
  code: string;
  description?: string;
  address: string;
  latitude?: number;
  longitude?: number;
  managerId?: number;
}