// src/app/layout/default-layout/_nav.ts
import { INavData } from "@coreui/angular";

export interface INavDataWithPermissions extends INavData {
  requiredPermissions?: string[];
}

export const navItems: INavDataWithPermissions[] = [
  // ==================== NO TOCAR - INICIO ====================
  // PANEL PRINCIPAL
  {
    title: true,
    name: "PANEL PRINCIPAL",
    class: "nav-title",
  },
  {
    name: "Dashboard",
    url: "/dashboard",
    icon: "fa-solid fa-gauge-high",
    badge: { color: "info", text: "INICIO" },
  },

  // GESTIÓN DE ACCESOS
  {
    title: true,
    name: "GESTIÓN DE ACCESOS",
    class: "nav-title",
    requiredPermissions: ["users.read"],
  },
  {
    name: "Usuarios",
    url: "/users",
    icon: "fa-solid fa-user",
    requiredPermissions: ["users.read"],
  },
  {
    name: "Roles",
    url: "/roles",
    icon: "fa-solid fa-shield-halved",
    requiredPermissions: ["roles.read"],
  },
  {
    name: "Permisos",
    url: "/permissions",
    icon: "fa-solid fa-lock",
    requiredPermissions: ["permissions.read"],
  },

  // ==================== GESTIÓN DE PARKING ====================
  {
    title: true,
    name: "GESTIÓN DE PARKING",
    class: "nav-title",
  },
  {
    name: "Turnos",
    url: "/shifts",
    icon: "fa-solid fa-clock-rotate-left",
    // requiredPermissions: ["shifts.read"],
  },
  {
    name: "Tarifas",
    url: "/rates",
    icon: "fa-solid fa-coins",
    // requiredPermissions: ["rates.read"],
  },
  {
    name: "Parqueos",
    url: "/parkings",
    icon: "fa-solid fa-parking",
  },
    //   name: "Zonas",
  //   url: "/zones",
  //   icon: "fa-solid fa-map-location-dot",
  //   // requiredPermissions: ["zones.read"],
  // },

  //   name: "Espacios",
  //   url: "/spaces",
  //   icon: "fa-solid fa-table-cells",
  //   // requiredPermissions: ["spaces.read"],
  // },
  {
    name: "Tipos de Documento",
    url: "/document-types",
    icon: "fa-solid fa-id-card",
  },
  {
    name: "Tipos de Pago",
    url: "/payment-types",
    icon: "fa-solid fa-money-check",
  },

  // ==================== CLIENTES Y VEHÍCULOS ====================
  {
    title: true,
    name: "CLIENTES Y VEHÍCULOS",
    class: "nav-title",
  },
  {
    name: "Clientes",
    url: "/customers",
    icon: "fa-solid fa-users",
    badge: { color: "info", text: "NUEVO" },
  },
  {
    name: "Vehículos",
    url: "/vehicles",
    icon: "fa-solid fa-car",
    badge: { color: "info", text: "NUEVO" },
  },

  // ==================== OPERACIONES DIARIAS ====================
  {
    title: true,
    name: "OPERACIONES DIARIAS",
    class: "nav-title",
  },
  {
    name: "Transacciones Activas",
    url: "/transactions/active",
    icon: "fa-solid fa-clock",
    badge: { color: "warning", text: "15" },
  },
  {
    name: "Registrar Entrada",
    url: "/transactions/entry",
    icon: "fa-solid fa-right-to-bracket",
  },
  {
    name: "Registrar Salida",
    url: "/transactions/exit",
    icon: "fa-solid fa-right-from-bracket",
  },
  {
    name: "Procesar Pago",
    url: "/transactions/payment",
    icon: "fa-solid fa-money-bill-wave",
  },
  {
    name: "Historial Transacciones",
    url: "/transactions",
    icon: "fa-solid fa-list",
  },

  // ==================== INFRACCIONES ====================
  {
    title: true,
    name: "CONTROL DE INFRACCIONES",
    class: "nav-title",
  },
  {
    name: "Infracciones",
    url: "/infractions",
    icon: "fa-solid fa-triangle-exclamation",
    badge: { color: "danger", text: "3" },
  },

  // ==================== GESTIÓN DE PAGOS ====================
  {
    title: true,
    name: "GESTIÓN DE PAGOS",
    class: "nav-title",
  },
  {
    name: "Historial de Pagos",
    url: "/payments",
    icon: "fa-solid fa-list",
  },

  // ==================== REPORTES ====================
  {
    title: true,
    name: "REPORTES",
    class: "nav-title",
  },
  {
    name: "Reporte Diario",
    url: "/reports/daily",
    icon: "fa-solid fa-calendar-day",
  },
  {
    name: "Reporte de Ingresos",
    url: "/reports/revenue",
    icon: "fa-solid fa-sack-dollar",
  },
  {
    name: "Reporte de Ocupación",
    url: "/reports/occupancy",
    icon: "fa-solid fa-chart-pie",
  },

  // ==================== MONITOREO IOT (FUTURO) ====================
  {
    title: true,
    name: "IoT (Próximamente)",
    class: "nav-title",
  },
  {
    name: "Dashboard IoT",
    url: "#",
    icon: "fa-solid fa-microchip",
    badge: { color: "secondary", text: "PRONTO" },
    // attributes: { disabled: true },
  },

  // ==================== INTELIGENCIA ARTIFICIAL (FUTURO) ====================
  {
    title: true,
    name: "IA (Próximamente)",
    class: "nav-title",
  },
  {
    name: "Dashboard AI",
    url: "#",
    icon: "fa-solid fa-brain",
    badge: { color: "secondary", text: "PRONTO" },
    // attributes: { disabled: true },
  },

  // ==================== NO TOCAR - INICIO ====================
  // CONFIGURACIÓN
  {
    title: true,
    name: "CONFIGURACIÓN DEL SISTEMA",
    class: "nav-title",
    requiredPermissions: ["users.delete"],
  },
  {
    name: "Configuración General",
    url: "/settings/general",
    icon: "fa-solid fa-gear",
    requiredPermissions: ["users.delete"],
  },

  // SOPORTE
  {
    title: true,
    name: "SOPORTE",
    class: "nav-title",
  },
  {
    name: "Soporte Técnico",
    url: "/support",
    icon: "fa-solid fa-headset",
    badge: { color: "success", text: "EN LÍNEA" },
  },

  // DOCUMENTACIÓN
  {
    title: true,
    name: "DOCUMENTACIÓN",
    class: "nav-title",
  },
  {
    name: "Manual de Usuario",
    url: "/docs/user-manual",
    icon: "fa-solid fa-book",
  },
  {
    name: "Acerca del Sistema",
    url: "/about",
    icon: "fa-solid fa-circle-info",
  },
  // ==================== NO TOCAR - FIN ====================
];