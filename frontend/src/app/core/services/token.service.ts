import { Injectable } from '@angular/core';
import { environment } from '../../../environments/environment';

/**
 * Servicio para manejar tokens JWT
 * Gestiona el almacenamiento, recuperación y validación de tokens
 */
@Injectable({
  providedIn: 'root'
})
export class TokenService {
  
  private readonly TOKEN_KEY = environment.tokenKey;
  private readonly REFRESH_TOKEN_KEY = environment.refreshTokenKey;

  constructor() {}

  // ========== GUARDAR TOKENS ==========

  /**
   * Guarda el access token en localStorage
   */
  saveToken(token: string): void {
    localStorage.setItem(this.TOKEN_KEY, token);
  }

  /**
   * Guarda el refresh token en localStorage
   */
  saveRefreshToken(refreshToken: string): void {
    localStorage.setItem(this.REFRESH_TOKEN_KEY, refreshToken);
  }

  /**
   * Guarda ambos tokens
   */
  saveTokens(accessToken: string, refreshToken: string): void {
    this.saveToken(accessToken);
    this.saveRefreshToken(refreshToken);
  }

  // ========== OBTENER TOKENS ==========

  /**
   * Obtiene el access token
   */
  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  /**
   * Obtiene el refresh token
   */
  getRefreshToken(): string | null {
    return localStorage.getItem(this.REFRESH_TOKEN_KEY);
  }

  // ========== ELIMINAR TOKENS ==========

  /**
   * Elimina el access token
   */
  removeToken(): void {
    localStorage.removeItem(this.TOKEN_KEY);
  }

  /**
   * Elimina el refresh token
   */
  removeRefreshToken(): void {
    localStorage.removeItem(this.REFRESH_TOKEN_KEY);
  }

  /**
   * Elimina todos los tokens
   */
  clearTokens(): void {
    this.removeToken();
    this.removeRefreshToken();
  }

  // ========== VALIDACIÓN ==========

  /**
   * Verifica si existe un access token
   */
  hasToken(): boolean {
    return this.getToken() !== null;
  }

  /**
   * Verifica si el token ha expirado
   * @returns true si el token expiró, false si aún es válido
   */
  isTokenExpired(): boolean {
    const token = this.getToken();
    if (!token) {
      return true;
    }

    try {
      const payload = this.decodeToken(token);
      const expirationDate = payload.exp * 1000; // Convertir a milisegundos
      const now = Date.now();
      
      return now >= expirationDate;
    } catch (error) {
      console.error('Error al decodificar token:', error);
      return true;
    }
  }

  /**
   * Decodifica el payload del JWT
   */
  decodeToken(token: string): any {
    try {
      const base64Url = token.split('.')[1];
      const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
      const jsonPayload = decodeURIComponent(
        atob(base64)
          .split('')
          .map(c => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
          .join('')
      );
      return JSON.parse(jsonPayload);
    } catch (error) {
      console.error('Error al decodificar token:', error);
      throw error;
    }
  }

  /**
   * Obtiene información del usuario desde el token
   */
  getUserFromToken(): any {
    const token = this.getToken();
    if (!token) {
      return null;
    }

    try {
      return this.decodeToken(token);
    } catch (error) {
      console.error('Error al obtener usuario del token:', error);
      return null;
    }
  }

  /**
   * Verifica si el usuario está autenticado y el token es válido
   */
  isAuthenticated(): boolean {
    return this.hasToken() && !this.isTokenExpired();
  }
}