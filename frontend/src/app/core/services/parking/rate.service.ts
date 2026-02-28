import { Injectable } from "@angular/core";
import { HttpClient, HttpParams } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "../../../../environments/environment";
import { Rate, RateRequest } from "../../models/parking/rate.model";
// ParkingPagedResponse para parking-service
import { ParkingPagedResponse } from "../../models/pagination.model";

@Injectable({
  providedIn: "root",
})
export class RateService {
  private apiUrl = `${environment.apiUrl}/parking-service/v1/rates`;

  constructor(private http: HttpClient) {}

  getAll(
    page: number = 0,
    size: number = 20,
    search?: string,
    status?: string, // "true" | "false" | undefined
  ): Observable<ParkingPagedResponse<Rate>> {
    let params = new HttpParams()
      .set("page", page.toString())
      .set("size", size.toString());

    if (search && search.trim()) {
      params = params.set("search", search.trim());
    }
    if (status !== undefined && status !== "") {
      params = params.set("status", status);
    }

    return this.http.get<ParkingPagedResponse<Rate>>(this.apiUrl, { params });
  }

  getActive(): Observable<Rate[]> {
    return this.http.get<Rate[]>(`${this.apiUrl}/active`);
  }

  getById(id: number): Observable<Rate> {
    return this.http.get<Rate>(`${this.apiUrl}/${id}`);
  }

  create(rate: RateRequest): Observable<Rate> {
    return this.http.post<Rate>(this.apiUrl, rate);
  }

  update(id: number, rate: RateRequest): Observable<Rate> {
    return this.http.put<Rate>(`${this.apiUrl}/${id}`, rate);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  toggleActive(id: number): Observable<Rate> {
    return this.http.patch<Rate>(`${this.apiUrl}/${id}/toggle-status`, {});
  }
}
