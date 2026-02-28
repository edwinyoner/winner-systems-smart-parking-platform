import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { TokenService } from '../services/token.service';
import { environment } from '../../../environments/environment';

/**
 * Guard para proteger rutas que requieren autenticación
 * 
 * Si el usuario NO está autenticado → Redirige a /login
 * Si el usuario está autenticado → Permite el acceso
 * 
 * Uso en routes:
 * {
 *   path: 'dashboard',
 *   component: DashboardComponent,
 *   canActivate: [AuthGuard]
 * }
 */
export const AuthGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const isAuthenticated = tokenService.isAuthenticated();

  if (!isAuthenticated) {
    if (environment.features.showDebugInfo) {
      console.warn('AuthGuard: Usuario no autenticado, redirigiendo a /login');
    }

    // Guardar la URL intentada para redirigir después del login
    const returnUrl = state.url;
    
    // Redirigir al login con returnUrl como query param
    router.navigate(['/login'], { 
      queryParams: { returnUrl },
      queryParamsHandling: 'merge'
    });

    return false;
  }

  if (environment.features.showDebugInfo) {
    console.log('AuthGuard: Acceso permitido');
  }

  return true;
};