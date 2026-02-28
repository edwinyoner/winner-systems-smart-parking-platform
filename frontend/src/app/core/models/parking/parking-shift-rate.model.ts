/**
 * Modelo para la configuración de tarifas por turno en un parqueo.
 * Esta configuración se aplica a TODAS las zonas del parqueo.
 * 
 * Un parqueo puede tener 1, 2 o 3 turnos (Mañana, Tarde, Noche).
 * Cada turno tiene UNA tarifa asignada.
 */
export interface ParkingShiftRate {
  id?: number;
  parkingId: number;
  shiftId: number;
  shiftName?: string;      // Expandido desde backend
  shiftCode?: string;      // Expandido desde backend
  rateId: number;
  rateName?: string;       // Expandido desde backend
  rateAmount?: number;     // Expandido desde backend
  rateCurrency?: string;   // Expandido desde backend
  status: boolean;         // Activo/Inactivo
  createdAt?: string;
  updatedAt?: string;
}

/**
 * Request para configurar tarifas por turno.
 */
export interface ConfigureParkingShiftRatesRequest {
  configurations: {
    shiftId: number;
    rateId: number;
    status?: boolean;
  }[];
}