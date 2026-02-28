import { inject } from '@angular/core';
import { Router, CanActivateFn } from '@angular/router';
import { TokenService } from '../services/token.service';
import { environment } from '../../../environments/environment';

/**
 * Guard para proteger rutas públicas (login, register, etc.)
 * 
 * Si el usuario YA está autenticado → Redirige a /dashboard
 * Si el usuario NO está autenticado → Permite el acceso
 * 
 * Uso en routes:
 * {
 *   path: 'login',
 *   component: LoginComponent,
 *   canActivate: [NoAuthGuard]
 * }
 */
export const NoAuthGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(TokenService);
  const router = inject(Router);

  const isAuthenticated = tokenService.isAuthenticated();

  if (isAuthenticated) {
    if (environment.features.showDebugInfo) {
      console.log('NoAuthGuard: Usuario ya autenticado, redirigiendo a /dashboard');
    }

    // Usuario ya autenticado, redirigir al dashboard
    router.navigate(['/dashboard']);
    return false;
  }

  if (environment.features.showDebugInfo) {
    console.log('NoAuthGuard: Acceso permitido a ruta pública');
  }

  return true;
};