/**
 * Environment para producción
 * Usado cuando ejecutas: ng build --configuration production
 */

export const environment = {
  production: true,
  
  // URLs de servicios backend (CAMBIAR A TUS URLs REALES)
  apiUrl: 'https://api.smartparking.winner-systems.com',
  authServiceUrl: 'https://api.smartparking.winner-systems.com/auth',
  
  // reCAPTCHA v3 (⚠️ DEBES OBTENER TU PROPIA CLAVE EN: https://www.google.com/recaptcha/admin)
  recaptchaSiteKey: 'TU_RECAPTCHA_SITE_KEY_PRODUCCION_AQUI',
  
  // Información de la aplicación
  appName: 'Smart Parking Platform',
  appVersion: '1.0.0',
  company: 'Winner Systems Corporation S.A.C.',
  
  // Configuración de tokens
  tokenKey: 'smart_parking_token',
  refreshTokenKey: 'smart_parking_refresh_token',
  
  // Timeouts (en milisegundos)
  httpTimeout: 30000,  // 30 segundos
  
  // Features flags
  features: {
    enableRegistration: true,      // Permitir registro de usuarios
    enableEmailVerification: true, // Requerir verificación de email
    enableRecaptcha: true,         // reCAPTCHA activado en producción
    enableRememberMe: true,        // Opción "Recordarme"
    showDebugInfo: false           // NO mostrar debug en producción
  },
  
  // URLs del frontend (CAMBIAR A TU DOMINIO REAL)
  frontendUrl: 'https://smartparking.winner-systems.com',
  
  // Paginación por defecto
  pagination: {
    defaultPageSize: 10,
    pageSizeOptions: [5, 10, 25, 50, 100]
  }
};