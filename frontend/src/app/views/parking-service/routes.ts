// src/app/views/parking-service/routes.ts
import { Routes } from "@angular/router";
import { AuthGuard } from "../../core/guards/auth.guard";

export const parkingRoutes: Routes = [
  // TURNOS
  {
    path: "shifts",
    loadChildren: () => import("./shifts/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  //TARIFAS
  {
    path: "rates",
    loadChildren: () => import("./rates/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // PARQUEOS
  {
    path: "parkings",
    loadChildren: () => import("./parkings/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },
  // ZONAS
  // {
  //   path: 'zones',
  //   loadChildren: () => import('./zones/routes').then(m => m.routes),
  //   canActivate: [AuthGuard]
  // },

  //   // ESPACIOS
  //   {
  //     path: 'spaces',
  //     loadChildren: () => import('./spaces/routes').then(m => m.spaceRoutes),
  //     canActivate: [AuthGuard]
  //   },

  // TIPOS DE DOCUMENTO
  {
    path: "document-types",
    loadChildren: () => import("./document-types/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // TIPOS DE PAGO
  {
    path: "payment-types",
    loadChildren: () => import("./payment-types/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  //   // CLIENTES
  //   {
  //     path: 'customers',
  //     loadChildren: () => import('./customers/routes').then(m => m.customerRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // VEHÍCULOS
  //   {
  //     path: 'vehicles',
  //     loadChildren: () => import('./vehicles/routes').then(m => m.vehicleRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // INFRACCIONES
  //   {
  //     path: 'infractions',
  //     loadChildren: () => import('./infractions/routes').then(m => m.infractionRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // TIPOS DE PAGO
  //   {
  //     path: 'payment-types',
  //     loadChildren: () => import('./payment-types/routes').then(m => m.paymentTypeRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // PAGOS
  //   {
  //     path: 'payments',
  //     loadChildren: () => import('./payments/routes').then(m => m.paymentRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // TRANSACCIONES
  //   {
  //     path: 'transactions',
  //     loadChildren: () => import('./transactions/routes').then(m => m.transactionRoutes),
  //     canActivate: [AuthGuard]
  //   },

  //   // REPORTES
  //   {
  //     path: 'reports',
  //     loadChildren: () => import('./reports/routes').then(m => m.reportRoutes),
  //     canActivate: [AuthGuard]
  //   },

  // Ruta por defecto - redireccionar a transacciones activas
  {
    path: "",
    redirectTo: "transactions/active",
    pathMatch: "full",
  },
];
