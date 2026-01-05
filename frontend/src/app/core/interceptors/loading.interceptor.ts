import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { finalize } from 'rxjs';
import { LoadingService } from '../services/loading.service';

/**
 * Interceptor de loading
 * 
 * Muestra/oculta un spinner global mientras se realizan peticiones HTTP
 * 
 * Nota: Requiere crear LoadingService primero
 */
export const loadingInterceptor: HttpInterceptorFn = (req, next) => {
  const loadingService = inject(LoadingService);

  // Verificar si la petición debe mostrar loading
  // Puedes excluir ciertas URLs si es necesario
  const showLoading = !req.url.includes('/no-loading');

  if (showLoading) {
    loadingService.show();
  }

  return next(req).pipe(
    finalize(() => {
      if (showLoading) {
        loadingService.hide();
      }
    })
  );
};