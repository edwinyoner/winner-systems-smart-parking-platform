import { Routes } from '@angular/router';

/**
 * Rutas del módulo de autenticación
 */
export const authRoutes: Routes = [
  {
    path: '',
    redirectTo: 'login',
    pathMatch: 'full'
  },
  {
    path: 'login',
    loadComponent: () => import('./login/login.component').then(m => m.LoginComponent),
    title: 'Iniciar Sesión - Smart Parking'
  },
  {
    path: 'register',
    loadComponent: () => import('./register/register.component').then(m => m.RegisterComponent),
    title: 'Crear Cuenta - Smart Parking'
  },
  {
    path: 'forgot-password',
    loadComponent: () => import('./forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent),
    title: 'Recuperar Contraseña - Smart Parking'
  },
  {
    path: 'reset-password',
    loadComponent: () => import('./reset-password/reset-password.component').then(m => m.ResetPasswordComponent),
    title: 'Restablecer Contraseña - Smart Parking'
  },
  {
    path: 'verify-email',
    loadComponent: () => import('./verify-email/verify-email.component').then(m => m.VerifyEmailComponent),
    title: 'Verificar Email - Smart Parking'
  }
];

// Exports individuales para usar en app.routes.ts
export const loginRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./login/login.component').then(m => m.LoginComponent),
    title: 'Iniciar Sesión - Smart Parking'
  }
];

export const registerRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./register/register.component').then(m => m.RegisterComponent),
    title: 'Crear Cuenta - Smart Parking'
  }
];

export const forgotPasswordRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent),
    title: 'Recuperar Contraseña - Smart Parking'
  }
];

export const resetPasswordRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./reset-password/reset-password.component').then(m => m.ResetPasswordComponent),
    title: 'Restablecer Contraseña - Smart Parking'
  }
];

export const verifyEmailRoutes: Routes = [
  {
    path: '',
    loadComponent: () => import('./verify-email/verify-email.component').then(m => m.VerifyEmailComponent),
    title: 'Verificar Email - Smart Parking'
  }
];