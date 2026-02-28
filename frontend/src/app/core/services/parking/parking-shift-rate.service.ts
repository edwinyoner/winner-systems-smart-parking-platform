import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { 
  ParkingShiftRate, 
  ConfigureParkingShiftRatesRequest 
} from '../../models/parking/parking-shift-rate.model';

@Injectable({
  providedIn: 'root'
})
export class ParkingShiftRateService {

  private apiUrl = `${environment.apiUrl}/parking-service/v1/parkings`;

  constructor(private http: HttpClient) {}

  /**
   * Configura las tarifas por turno para un parqueo.
   * Paso 4 del Stepper.
   */
  configure(
    parkingId: number, 
    request: ConfigureParkingShiftRatesRequest
  ): Observable<ParkingShiftRate[]> {
    return this.http.post<ParkingShiftRate[]>(
      `${this.apiUrl}/${parkingId}/shift-rates`,
      request
    );
  }

  /**
   * Obtiene las configuraciones de un parqueo.
   */
  getByParkingId(parkingId: number): Observable<ParkingShiftRate[]> {
    return this.http.get<ParkingShiftRate[]>(
      `${this.apiUrl}/${parkingId}/shift-rates`
    );
  }

  /**
   * Elimina una configuración específica.
   */
  delete(configId: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/shift-rates/${configId}`
    );
  }

  /**
   * Activa/desactiva una configuración.
   */
  toggleStatus(configId: number): Observable<ParkingShiftRate> {
    return this.http.patch<ParkingShiftRate>(
      `${this.apiUrl}/shift-rates/${configId}/toggle`,
      {}
    );
  }
}