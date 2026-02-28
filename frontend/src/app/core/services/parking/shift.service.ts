import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { Shift, ShiftRequest } from "../../models/parking/shift.model";
// Importar ParkingPagedResponse en lugar de PaginatedResponse
import { ParkingPagedResponse } from "../../models/pagination.model";

@Injectable({
  providedIn: "root",
})
export class ShiftService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/shifts`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string,
  ): Observable<ParkingPagedResponse<Shift>> {
    let params = new HttpParams()
      .set("page", page.toString())
      .set("size", size.toString());

    if (search && search.trim()) {
      params = params.set("search", search.trim());
    }
    if (status !== undefined && status !== "") {
      params = params.set("status", status);
    }
      
    return this.http.get<ParkingPagedResponse<Shift>>(this.apiUrl, { params });
  }

  getActive(): Observable<Shift[]> {
    return this.http.get<Shift[]>(`${this.apiUrl}/active`);
  }

  getById(id: number): Observable<Shift> {
    return this.http.get<Shift>(`${this.apiUrl}/${id}`);
  }

  create(shift: ShiftRequest): Observable<Shift> {
    return this.http.post<Shift>(this.apiUrl, shift);
  }

  update(id: number, shift: ShiftRequest): Observable<Shift> {
    return this.http.put<Shift>(`${this.apiUrl}/${id}`, shift);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleActive(id: number): Observable<Shift> {
    return this.http.patch<Shift>(`${this.apiUrl}/${id}/toggle-status`, {});
  }
}
