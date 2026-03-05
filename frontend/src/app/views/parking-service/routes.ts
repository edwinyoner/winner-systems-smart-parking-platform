// src/app/views/parking-service/routes.ts
import { Routes } from "@angular/router";
import { AuthGuard } from "../../core/guards/auth.guard";

export const parkingRoutes: Routes = [
  // ==================== CONFIGURACIÓN BASE ====================
  // TURNOS
  {
    path: "shifts",
    loadChildren: () => import("./shifts/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // TARIFAS
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

  // ==================== CLIENTES Y VEHÍCULOS ====================
  // CLIENTES
  {
    path: "customers",
    loadChildren: () => import("./customers/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // VEHÍCULOS
  {
    path: "vehicles",
    loadChildren: () => import("./vehicles/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // ==================== OPERACIONES DIARIAS ====================
  // TRANSACCIONES
  {
    path: "transactions",
    loadChildren: () => import("./transactions/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // PAGOS
  {
    path: "payments",
    loadChildren: () => import("./payments/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // ==================== INFRACCIONES ====================
  // INFRACCIONES (pendiente de implementar)
  {
    path: "infractions",
    loadChildren: () => import("./infractions/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // ==================== REPORTES ====================
  // REPORTES (pendiente de implementar)
  {
    path: "reports",
    loadChildren: () => import("./reports/routes").then((m) => m.routes),
    canActivate: [AuthGuard],
  },

  // ==================== RUTA POR DEFECTO ====================
  // Ruta por defecto - redireccionar a transacciones activas
  {
    path: "",
    redirectTo: "transactions/active",
    pathMatch: "full",
  },
];