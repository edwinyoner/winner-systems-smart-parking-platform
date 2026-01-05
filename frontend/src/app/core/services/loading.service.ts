import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

/**
 * Servicio para controlar el estado de carga global
 * Usado por el LoadingInterceptor
 */
@Injectable({
  providedIn: 'root'
})
export class LoadingService {
  
  private loadingSubject = new BehaviorSubject<boolean>(false);
  private loadingCount = 0;

  /**
   * Observable del estado de loading
   */
  public loading$: Observable<boolean> = this.loadingSubject.asObservable();

  /**
   * Muestra el spinner de carga
   */
  show(): void {
    this.loadingCount++;
    if (this.loadingCount === 1) {
      this.loadingSubject.next(true);
    }
  }

  /**
   * Oculta el spinner de carga
   */
  hide(): void {
    if (this.loadingCount > 0) {
      this.loadingCount--;
    }

    if (this.loadingCount === 0) {
      this.loadingSubject.next(false);
    }
  }

  /**
   * Verifica si está cargando (snapshot)
   */
  isLoading(): boolean {
    return this.loadingSubject.value;
  }

  /**
   * Resetea el contador de loading (útil para errores)
   */
  reset(): void {
    this.loadingCount = 0;
    this.loadingSubject.next(false);
  }
}