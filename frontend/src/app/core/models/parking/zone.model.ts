export interface Zone {
  id?: number;
  parkingId: number;
  name: string;
  code: string;
  address: string;
  description?: string;
  totalSpaces?: number;
  availableSpaces?: number;
  occupancyPercentage?: number;
  latitude?: number;
  longitude?: number;
  hasCamera?: boolean;
  cameraIds?: string[];
  cameraCount?: number;
  status?: 'ACTIVE' | 'INACTIVE' | 'MAINTENANCE' | 'OUT_OF_SERVICE';
  createdAt?: string;
  updatedAt?: string;
}

export interface ZoneRequest {
  parkingId: number;
  name: string;
  code: string;
  address: string;
  description?: string;
  totalSpaces?: number;
  latitude?: number;
  longitude?: number;
  hasCamera?: boolean;
  cameraIds?: string[];
}