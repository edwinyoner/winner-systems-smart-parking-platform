export interface Space {
  id?: number;
  zoneId: number;
  type: 'PARALLEL' | 'DIAGONAL' | 'PERPENDICULAR';
  code: string;
  description?: string;
  width?: number;
  length?: number;
  hasSensor?: boolean;
  sensorId?: string;
  hasCameraCoverage?: boolean;
  status?: 'AVAILABLE' | 'OCCUPIED' | 'MAINTENANCE' | 'OUT_OF_SERVICE';
  createdAt?: string;
  updatedAt?: string;
}

export interface SpaceRequest {
  zoneId: number;
  type: string;
  code: string;
  description?: string;
  width?: number;
  length?: number;
  hasSensor?: boolean;
  sensorId?: string;
  hasCameraCoverage?: boolean;
}