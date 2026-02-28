//src/environments/environment.development.ts
/**
 * Environment para desarrollo local
 * Usado cuando ejecutas: ng serve
 */

export const environment = {
  production: false,

  // URLs de servicios backend
  // apiUrl: 'http://localhost:8080/api',        // API Gateway (puerto principal)
  // authServiceUrl: 'http://localhost:8081',    // Auth Service directo (para testing)
  apiUrl: "http://192.168.1.4:8080/api",
  authServiceUrl: "http://192.168.1.4:8081",

  // reCAPTCHA v3 (clave de prueba de Google)
  recaptchaSiteKey: "6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI",

  // Información de la aplicación
  appName: "Smart Parking Platform",
  appVersion: "1.0.0-dev",
  company: "Winner Systems Corporation S.A.C.",

  // Configuración de tokens
  tokenKey: "smart_parking_token",
  refreshTokenKey: "smart_parking_refresh_token",

  // Timeouts (en milisegundos)
  httpTimeout: 30000, // 30 segundos

  // Features flags (para activar/desactivar funcionalidades)
  features: {
    enableRegistration: true, // Permitir registro de usuarios
    enableEmailVerification: true, // Requerir verificación de email
    enableRecaptcha: false, // reCAPTCHA (desactivado en dev)
    enableRememberMe: true, // Opción "Recordarme"
    showDebugInfo: true, // Mostrar info de debug en consola
  },

  // URLs del frontend (para emails y redirecciones)
  frontendUrl: "http://localhost:4200",

  // Paginación por defecto
  pagination: {
    defaultPageSize: 10,
    pageSizeOptions: [5, 10, 25, 50, 100],
  },
};
