import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';
import { environment } from '../../../environments/environment';

/**
 * Interceptor de autenticación
 * 
 * Agrega automáticamente el token JWT a todas las peticiones HTTP
 * que vayan hacia el backend (apiUrl)
 * 
 * Header agregado: Authorization: Bearer {token}
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const tokenService = inject(TokenService);

  // Verificar si la petición es hacia nuestro API
  const isApiRequest = req.url.startsWith(environment.apiUrl) || 
                       req.url.startsWith(environment.authServiceUrl);

  // Si no es una petición al API, continuar sin modificar
  if (!isApiRequest) {
    return next(req);
  }

  // Obtener token
  const token = tokenService.getToken();

  // Si no hay token, continuar sin modificar
  if (!token) {
    return next(req);
  }

  // Clonar la petición y agregar el header Authorization
  const authReq = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`
    }
  });

  if (environment.features.showDebugInfo) {
    console.log(`🔐 AuthInterceptor: Token agregado a ${req.method} ${req.url}`);
  }

  return next(authReq);
};