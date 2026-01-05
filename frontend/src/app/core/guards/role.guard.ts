import { inject } from '@angular/core';
import { Router, CanActivateFn, ActivatedRouteSnapshot } from '@angular/router';
import { PermissionService } from '../services/permission.service';
import { CurrentUserService } from '../services/current-user.service';
import { environment } from '../../../environments/environment';

/**
 * Guard para proteger rutas por roles y permisos
 * 
 * Verifica que el usuario tenga los permisos o roles requeridos
 * definidos en la configuración de la ruta (route.data)
 * 
 * Si el usuario NO tiene permisos → Redirige a /403 o /dashboard
 * Si el usuario tiene permisos → Permite el acceso
 * 
 * Uso en routes:
 * {
 *   path: 'users',
 *   component: UserListComponent,
 *   canActivate: [AuthGuard, RoleGuard],
 *   data: { 
 *     requiredPermissions: ['users.read'],  // Requiere al menos uno
 *     requireAllPermissions: false,         // false = OR, true = AND
 *     requiredRoles: ['ADMIN'],            // Opcional
 *   }
 * }
 */
export const RoleGuard: CanActivateFn = (route: ActivatedRouteSnapshot, state) => {
  const permissionService = inject(PermissionService);
  const currentUserService = inject(CurrentUserService);
  const router = inject(Router);

  const currentUser = currentUserService.getCurrentUser();

  // Si no hay usuario autenticado, no debería llegar aquí
  // pero por seguridad verificamos
  if (!currentUser) {
    if (environment.features.showDebugInfo) {
      console.error('❌ RoleGuard: No hay usuario autenticado');
    }
    router.navigate(['/login']);
    return false;
  }

  // Verificar si es ADMIN (tiene acceso a todo)
  if (currentUserService.isAdmin()) {
    if (environment.features.showDebugInfo) {
      console.log('✅ RoleGuard: Usuario ADMIN, acceso total permitido');
    }
    return true;
  }

  // Obtener permisos requeridos de route.data
  const requiredPermissions = route.data['requiredPermissions'] as string[] | undefined;
  const requireAllPermissions = route.data['requireAllPermissions'] as boolean || false;
  const requiredRoles = route.data['requiredRoles'] as string[] | undefined;

  // Verificar roles (si están definidos)
  if (requiredRoles && requiredRoles.length > 0) {
    const hasRole = requiredRoles.some(role => currentUserService.hasRole(role));
    
    if (!hasRole) {
      if (environment.features.showDebugInfo) {
        console.warn(`🔒 RoleGuard: Usuario no tiene ninguno de los roles requeridos: ${requiredRoles.join(', ')}`);
      }
      handleUnauthorizedAccess(router, route);
      return false;
    }
  }

  // Verificar permisos (si están definidos)
  if (requiredPermissions && requiredPermissions.length > 0) {
    let hasPermission = false;

    if (requireAllPermissions) {
      // Requiere TODOS los permisos (AND)
      hasPermission = permissionService.hasAllPermissions(requiredPermissions);
    } else {
      // Requiere AL MENOS UNO de los permisos (OR)
      hasPermission = permissionService.hasAnyPermission(requiredPermissions);
    }

    if (!hasPermission) {
      if (environment.features.showDebugInfo) {
        const operator = requireAllPermissions ? 'todos' : 'al menos uno de';
        console.warn(`🔒 RoleGuard: Usuario no tiene ${operator} los permisos requeridos: ${requiredPermissions.join(', ')}`);
      }
      handleUnauthorizedAccess(router, route);
      return false;
    }
  }

  if (environment.features.showDebugInfo) {
    console.log('✅ RoleGuard: Acceso permitido');
  }

  return true;
};

/**
 * Maneja el acceso no autorizado
 */
function handleUnauthorizedAccess(router: Router, route: ActivatedRouteSnapshot): void {
  // TODO: Crear página de error 403 (No autorizado)
  // Por ahora redirigir al dashboard con mensaje
  
  router.navigate(['/dashboard'], {
    queryParams: { 
      error: 'unauthorized',
      message: 'No tienes permisos para acceder a esta página'
    }
  });
}