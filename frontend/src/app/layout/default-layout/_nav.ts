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
  // ==================== NO TOCAR - FIN ====================

  // ==================== GESTIÓN DE ESTACIONAMIENTO - ACTUALIZADO ====================
  {
    title: true,
    name: "GESTIÓN DE PARKING",
    class: "nav-title",
    // requiredPermissions: ['parking.view']
  },
  {
    name: "Turnos",
    url: "/shifts",
    icon: "fa-solid fa-clock-rotate-left",
    requiredPermissions: ["shifts.read"],
  },
  {
    name: "Tarifas",
    url: "/rates",
    icon: "fa-solid fa-coins",
    requiredPermissions: ["rates.read"],
  },
  {
    name: "Parqueo",
    url: "/parkings",
    icon: "fa-solid fa-parking",
    // requiredPermissions: ["parking.read"],
  },
  // {
  //   name: "Zonas",
  //   url: "/zones",
  //   icon: "fa-solid fa-map-location-dot",
  //   // requiredPermissions: ["zones.read"],
  // },

  // CLIENTES Y VEHÍCULOS
  {
    name: "Tipos de Documento",
    url: "/document-types",
    icon: "fa-solid fa-id-card",
    // requiredPermissions: ["document-types.read"],
  },
  {
    name: "Tipos de Pago",
    url: "/payment-types",
    icon: "fa-solid fa-money-check",
    // requiredPermissions: ["payment-types.read"],
  },
  {
    name: "Clientes",
    url: "/customers",
    icon: "fa-solid fa-users",
    // requiredPermissions: ['customers.view']
  },
  {
    name: "Vehículos",
    url: "/vehicles",
    icon: "fa-solid fa-car",
    // requiredPermissions: ['vehicles.view']
  },
  {
    name: "Infracciones",
    url: "/infractions",
    icon: "fa-solid fa-triangle-exclamation",
    badge: { color: "danger", text: "3" },
    // requiredPermissions: ['infractions.view']
  },

  // OPERACIONES DIARIAS
  {
    name: "Transacciones Activas",
    url: "/transactions/active",
    icon: "fa-solid fa-clock",
    badge: { color: "warning", text: "15" },
    // requiredPermissions: ['transaction.view']
  },
  {
    name: "Registrar Entrada",
    url: "/transactions/entry",
    icon: "fa-solid fa-right-to-bracket",
    // requiredPermissions: ['entry']
  },
  {
    name: "Registrar Salida",
    url: "/transactions/exit",
    icon: "fa-solid fa-right-from-bracket",
    // requiredPermissions: ['parking.exit']
  },
  {
    name: "Procesar Pago",
    url: "/transactions/payment",
    icon: "fa-solid fa-money-bill-wave",
    // requiredPermissions: ['payment']
  },
  {
    name: "Historial Transacciones",
    url: "/transactions",
    icon: "fa-solid fa-list",
    // requiredPermissions: ['transaction.view']
  },

  // INFRAESTRUCTURA
  {
    name: "Gestión de Espacios",
    url: "/spaces",
    icon: "fa-solid fa-table-cells",
    badge: { color: "success", text: "340" },
    // requiredPermissions: ['spaces.view'],
  },

  // PAGOS
  {
    name: "Gestión de Pagos",
    icon: "fa-solid fa-credit-card",
    // requiredPermissions: ['payments.view'],
    children: [
      {
        name: "Historial de Pagos",
        url: "/payments",
        icon: "fa-solid fa-list",
        // requiredPermissions: ['payments.view']
      },
      {
        name: "Tipos de Pago",
        url: "/payment-types",
        icon: "fa-solid fa-money-check",
        // requiredPermissions: ['payment-types.view']
      },
    ],
  },

  // REPORTES DE PARKING
  {
    name: "Reportes Parking",
    icon: "fa-solid fa-chart-simple",
    // requiredPermissions: ['reports.view'],
    children: [
      {
        name: "Reporte Diario",
        url: "/reports/daily",
        icon: "fa-solid fa-calendar-day",
        // requiredPermissions: ['reports.view']
      },
      {
        name: "Reporte de Ingresos",
        url: "/reports/revenue",
        icon: "fa-solid fa-sack-dollar",
        // requiredPermissions: ['reports.view']
      },
      {
        name: "Reporte de Ocupación",
        url: "/reports/occupancy",
        icon: "fa-solid fa-chart-pie",
        // requiredPermissions: ['reports.view']
      },
    ],
  },

  // ==================== MONITOREO IOT ====================
  {
    title: true,
    name: "MONITOREO Y CONTROL IoT",
    class: "nav-title",
    // requiredPermissions: ['iot.view']
  },
  {
    name: "Dashboard IoT",
    url: "/iot/dashboard",
    icon: "fa-solid fa-microchip",
    badge: { color: "info", text: "ACTIVO" },
    // requiredPermissions: ['iot.view']
  },
  {
    name: "Sensores",
    icon: "fa-solid fa-wifi",
    // requiredPermissions: ['iot.sensor.view'],
    children: [
      {
        name: "Sensores de Ocupación",
        url: "/iot/sensors/occupancy",
        icon: "fa-solid fa-signal",
        // requiredPermissions: ['iot.sensor.view']
      },
      {
        name: "Estado de Sensores",
        url: "/iot/sensors/status",
        icon: "fa-solid fa-heartbeat",
        badge: { color: "success", text: "98%" },
        // requiredPermissions: ['iot.sensor.view']
      },
    ],
  },
  {
    name: "Cámaras",
    icon: "fa-solid fa-video",
    // requiredPermissions: ['iot.camera.view'],
    children: [
      {
        name: "Cámaras Activas",
        url: "/iot/cameras",
        icon: "fa-solid fa-camera",
        // requiredPermissions: ['iot.camera.view']
      },
      {
        name: "Reconocimiento de Placas",
        url: "/iot/cameras/license-plate",
        icon: "fa-solid fa-rectangle-ad",
        badge: { color: "info", text: "AI" },
        // requiredPermissions: ['ai.view']
      },
    ],
  },
  {
    name: "Barreras",
    url: "/iot/barriers",
    icon: "fa-solid fa-road-barrier",
    // requiredPermissions: ['iot.barrier.view']
  },
  {
    name: "Configuración IoT",
    url: "/iot/settings",
    icon: "fa-solid fa-sliders",
    // requiredPermissions: ['iot.admin']
  },

  // ==================== INTELIGENCIA ARTIFICIAL ====================
  {
    title: true,
    name: "INTELIGENCIA ARTIFICIAL",
    class: "nav-title",
    // requiredPermissions: ['ai.view']
  },
  {
    name: "Dashboard AI",
    url: "/ai/dashboard",
    icon: "fa-solid fa-brain",
    badge: { color: "info", text: "AI" },
    // requiredPermissions: ['ai.view']
  },
  {
    name: "Detección de Placas",
    url: "/ai/plate-detection",
    icon: "fa-solid fa-camera-retro",
    // requiredPermissions: ['ai.view']
  },
  {
    name: "Análisis de Comportamiento",
    url: "/ai/behavior-analysis",
    icon: "fa-solid fa-user-secret",
    // requiredPermissions: ['ai.view']
  },
  {
    name: "Predicción de Ocupación",
    url: "/ai/occupancy-prediction",
    icon: "fa-solid fa-chart-area",
    // requiredPermissions: ['ai.view']
  },
  {
    name: "Modelos AI",
    url: "/ai/models",
    icon: "fa-solid fa-network-wired",
    // requiredPermissions: ['ai.admin']
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
