import { Injectable } from '@angular/core';
import { TokenService } from './token.service';

/**
 * Servicio para manejar el contexto de autenticación del usuario actual.
 * 
 * Responsabilidades:
 * - Obtener información del usuario autenticado
 * - Obtener el rol activo (con el que inició sesión)
 * - Verificar permisos de acceso a menús
 * - Proporcionar datos para la UI (sidebar, header)
 */
@Injectable({
  providedIn: 'root'
})
export class AuthContextService {

  constructor(private tokenService: TokenService) {}

  // ========== INFORMACIÓN DEL USUARIO ==========

  /**
   * Obtiene la información completa del usuario desde el token
   */
  getCurrentUser(): any {
    return this.tokenService.getUserFromToken();
  }

  /**
   * Obtiene el ID del usuario
   */
  getUserId(): number | null {
    const user = this.getCurrentUser();
    return user?.userId || null;
  }

  /**
   * Obtiene el email del usuario
   */
  getUserEmail(): string | null {
    const user = this.getCurrentUser();
    return user?.email || null;
  }

  /**
   * Obtiene el nombre completo del usuario
   */
  getFullName(): string {
    const user = this.getCurrentUser();
    if (!user) return '';
    return `${user.firstName || ''} ${user.lastName || ''}`.trim();
  }

  /**
   * Obtiene las iniciales del usuario (para avatares)
   */
  getUserInitials(): string {
    const user = this.getCurrentUser();
    if (!user) return '';
    
    const firstInitial = user.firstName?.charAt(0) || '';
    const lastInitial = user.lastName?.charAt(0) || '';
    return `${firstInitial}${lastInitial}`.toUpperCase();
  }

  /**
   * Obtiene la foto de perfil del usuario
   */
  getProfilePicture(): string | null {
    const user = this.getCurrentUser();
    return user?.profilePicture || null;
  }

  // ========== ROLES ==========

  /**
   * Obtiene el rol activo (con el que inició sesión)
   */
  getActiveRole(): string | null {
    const user = this.getCurrentUser();
    return user?.activeRole || null;
  }

  /**
   * Obtiene TODOS los roles del usuario
   */
  getAllRoles(): string[] {
    const user = this.getCurrentUser();
    return user?.roles || [];
  }

  /**
   * Verifica si el usuario tiene un rol específico (en su lista de roles)
   */
  hasRole(role: string): boolean {
    const roles = this.getAllRoles();
    return roles.includes(role);
  }

  /**
   * Verifica si el rol activo coincide
   */
  isActiveRole(role: string): boolean {
    return this.getActiveRole() === role;
  }

  /**
   * Verifica si el usuario tiene al menos uno de los roles
   */
  hasAnyRole(roles: string[]): boolean {
    return roles.some(role => this.hasRole(role));
  }

  // ========== PERMISOS ==========

  /**
   * Obtiene todos los permisos del usuario
   */
  getAllPermissions(): string[] {
    const user = this.getCurrentUser();
    return user?.permissions || [];
  }

  /**
   * Verifica si el usuario tiene un permiso específico
   */
  hasPermission(permission: string): boolean {
    const permissions = this.getAllPermissions();
    return permissions.includes(permission);
  }

  /**
   * Verifica si el usuario tiene al menos uno de los permisos
   */
  hasAnyPermission(permissions: string[]): boolean {
    return permissions.some(permission => this.hasPermission(permission));
  }

  /**
   * Verifica si el usuario tiene todos los permisos
   */
  hasAllPermissions(permissions: string[]): boolean {
    return permissions.every(permission => this.hasPermission(permission));
  }

  // ========== ACCESO A MENÚS ==========

  /**
   * Verifica si el usuario puede acceder a un item del menú
   * basándose en el ROL ACTIVO (no en todos los roles)
   */
  canAccessMenuItem(allowedRoles?: string[]): boolean {
    // Si no hay restricción de roles, todos pueden acceder
    if (!allowedRoles || allowedRoles.length === 0) {
      return true;
    }

    // Verificar si el ROL ACTIVO está en la lista permitida
    const activeRole = this.getActiveRole();
    return activeRole ? allowedRoles.includes(activeRole) : false;
  }

  /**
   * Verifica si el usuario puede acceder a una ruta específica
   * basándose en permisos
   */
  canAccessRoute(requiredPermissions?: string[]): boolean {
    if (!requiredPermissions || requiredPermissions.length === 0) {
      return true;
    }

    return this.hasAnyPermission(requiredPermissions);
  }

  // ========== UTILIDADES PARA UI ==========

  /**
   * Obtiene la clase CSS del badge según el rol activo
   */
  getRoleBadgeClass(): string {
    const activeRole = this.getActiveRole();
    
    switch (activeRole) {
      case 'ADMIN':
        return 'badge-admin';
      case 'AUTORIDAD':
        return 'badge-autoridad';
      case 'OPERADOR':
        return 'badge-operador';
      default:
        return 'badge-default';
    }
  }

  /**
   * Obtiene el icono según el rol activo
   */
  getRoleIcon(): string {
    const activeRole = this.getActiveRole();
    
    switch (activeRole) {
      case 'ADMIN':
        return 'fas fa-user-shield';
      case 'AUTORIDAD':
        return 'fas fa-user-tie';
      case 'OPERADOR':
        return 'fas fa-user-cog';
      default:
        return 'fas fa-user';
    }
  }

  /**
   * Obtiene el nombre legible del rol
   */
  getRoleDisplayName(): string {
    const activeRole = this.getActiveRole();
    
    switch (activeRole) {
      case 'ADMIN':
        return 'Administrador';
      case 'AUTORIDAD':
        return 'Autoridad';
      case 'OPERADOR':
        return 'Operador';
      default:
        return 'Usuario';
    }
  }

  // ========== ESTADO ==========

  /**
   * Verifica si el usuario está activo
   */
  isUserActive(): boolean {
    const user = this.getCurrentUser();
    return user?.status === true;
  }

  /**
   * Verifica si el email está verificado
   */
  isEmailVerified(): boolean {
    const user = this.getCurrentUser();
    return user?.emailVerified === true;
  }
}