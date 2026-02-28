// src/app/app.routes.ts
import { Routes } from '@angular/router';
import { DefaultLayoutComponent } from './layout/default-layout/default-layout.component';
import { AuthLayoutComponent } from './layout/auth-layout/auth-layout.component';
import { AuthGuard } from './core/guards/auth.guard';
import { NoAuthGuard } from './core/guards/no-auth.guard';
import { RoleGuard } from './core/guards/role.guard';

export const routes: Routes = [
  // ========== REDIRECT INICIAL ==========
  {
    path: '',
    redirectTo: 'dashboard',
    pathMatch: 'full'
  },

  // ========== RUTAS PÚBLICAS (Auth Layout - Sin Sidebar) ==========
  {
    path: '',
    component: AuthLayoutComponent,
    canActivate: [NoAuthGuard],
    children: [
      {
        path: 'login',
        loadChildren: () => import('./views/auth/routes').then(m => m.loginRoutes),
        data: { title: 'Iniciar Sesión' }
      },
      {
        path: 'register',
        loadChildren: () => import('./views/auth/routes').then(m => m.registerRoutes),
        data: { title: 'Crear Cuenta' }
      },
      {
        path: 'forgot-password',
        loadChildren: () => import('./views/auth/routes').then(m => m.forgotPasswordRoutes),
        data: { title: 'Recuperar Contraseña' }
      },
      {
        path: 'reset-password',
        loadChildren: () => import('./views/auth/routes').then(m => m.resetPasswordRoutes),
        data: { title: 'Restablecer Contraseña' }
      },
      {
        path: 'verify-email',
        loadChildren: () => import('./views/auth/routes').then(m => m.verifyEmailRoutes),
        data: { title: 'Verificar Email' }
      }
    ]
  },

  // ========== RUTAS PRIVADAS (Default Layout - Con Sidebar) ==========
  {
    path: '',
    component: DefaultLayoutComponent,
    canActivate: [AuthGuard],
    children: [
      // ========== DASHBOARD ==========
      {
        path: 'dashboard',
        loadChildren: () => import('./views/dashboard/routes').then(m => m.routes),
        data: { title: 'Dashboard' }
      },

      // ========== GESTIÓN DE ACCESOS ==========
      {
        path: 'users',
        loadChildren: () => import('./views/users/routes').then(m => m.routes),
        canActivate: [RoleGuard],
        data: { 
          title: 'Usuarios',
          requiredPermissions: ['users.read']
        }
      },
      {
        path: 'roles',
        loadChildren: () => import('./views/roles/routes').then(m => m.routes),
        canActivate: [RoleGuard],
        data: { 
          title: 'Roles',
          requiredPermissions: ['roles.read']
        }
      },
      {
        path: 'permissions',
        loadChildren: () => import('./views/permissions/routes').then(m => m.routes),
        canActivate: [RoleGuard],
        data: { 
          title: 'Permisos',
          requiredPermissions: ['roles.read']
        }
      },
      {
        path: 'profile',
        loadChildren: () => import('./views/profile/routes').then(m => m.routes),
        data: { title: 'Mi Perfil' }
      },

      // ========== PARKING SERVICE - TURNOS ==========
      {
        path: 'shifts',
        loadChildren: () => import('./views/parking-service/shifts/routes').then(m => m.routes),
        data: { title: 'Gestión de Turnos' }
      },

      // ========== PARKING SERVICE - TARIFAS ==========
      {
        path: 'rates',
        loadChildren: () => import('./views/parking-service/rates/routes').then(m => m.routes),
        data: { title: 'Gestión de Tarifas' }
      },

      // ========== PARKING SERVICE - TRANSACCIONES ==========
      {
        path: 'transactions',
        loadChildren: () => import('./views/parking-service/transactions/routes').then(m => m.routes),
        data: { title: 'Transacciones' }
      },

      // ========== PARKING SERVICE - ZONAS ==========
      {
        path: 'zones',
        loadChildren: () => import('./views/parking-service/zones/routes').then(m => m.routes),
        data: { title: 'Gestión de Zonas' }
      },

      // ========== PARKING SERVICE - TIPOS DE ESPACIO ==========
      {
        path: 'space-types',
        loadChildren: () => import('./views/parking-service/space-types/routes').then(m => m.routes),
        data: { title: 'Tipos de Espacio' }
      },

      // ========== PARKING SERVICE - ESPACIOS ==========
      {
        path: 'spaces',
        loadChildren: () => import('./views/parking-service/spaces/routes').then(m => m.routes),
        data: { title: 'Gestión de Espacios' }
      },

      // ========== PARKING SERVICE - TIPOS DE DOCUMENTO ==========
      {
        path: 'document-types',
        loadChildren: () => import('./views/parking-service/document-types/routes').then(m => m.routes),
        data: { title: 'Tipos de Documento' }
      },

      // ========== PARKING SERVICE - CLIENTES ==========
      {
        path: 'customers',
        loadChildren: () => import('./views/parking-service/customers/routes').then(m => m.routes),
        data: { title: 'Gestión de Clientes' }
      },

      // ========== PARKING SERVICE - VEHÍCULOS ==========
      {
        path: 'vehicles',
        loadChildren: () => import('./views/parking-service/vehicles/routes').then(m => m.routes),
        data: { title: 'Gestión de Vehículos' }
      },

      // ========== PARKING SERVICE - INFRACCIONES ==========
      {
        path: 'infractions',
        loadChildren: () => import('./views/parking-service/infractions/routes').then(m => m.routes),
        data: { title: 'Gestión de Infracciones' }
      },

      // ========== PARKING SERVICE - TIPOS DE PAGO ==========
      {
        path: 'payment-types',
        loadChildren: () => import('./views/parking-service/payment-types/routes').then(m => m.routes),
        data: { title: 'Tipos de Pago' }
      },

      // ========== PARKING SERVICE - PAGOS ==========
      {
        path: 'payments',
        loadChildren: () => import('./views/parking-service/payments/routes').then(m => m.routes),
        data: { title: 'Gestión de Pagos' }
      },

      // ========== PARKING SERVICE - REPORTES ==========
      {
        path: 'reports',
        loadChildren: () => import('./views/parking-service/reports/routes').then(m => m.routes),
        data: { title: 'Reportes' }
      }
    ]
  },

  // ========== PÁGINAS DE ERROR (Futuro) ==========
  // {
  //   path: '403',
  //   loadComponent: () => import('./views/errors/page403/page403.component').then(m => m.Page403Component),
  //   data: { title: 'Acceso Denegado' }
  // },
  // {
  //   path: '404',
  //   loadComponent: () => import('./views/errors/page404/page404.component').then(m => m.Page404Component),
  //   data: { title: 'Página No Encontrada' }
  // },
  // {
  //   path: '500',
  //   loadComponent: () => import('./views/errors/page500/page500.component').then(m => m.Page500Component),
  //   data: { title: 'Error del Servidor' }
  // },

  // ========== FALLBACK (404) ==========
  {
    path: '**',
    redirectTo: 'dashboard'
  }
];