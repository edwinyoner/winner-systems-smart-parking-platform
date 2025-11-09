import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  // PANEL PRINCIPAL - DASHBOARD
  {
    title: true,
    name: 'PANEL PRINCIPAL',
    class: 'nav-title'
  },
  {
    name: 'Dashboard',
    url: '/dashboardm',
    class: 'nav-item-dashboard',
    icon: 'fa-solid fa-gauge-high', // Cambiar de iconComponent a icon
    badge: {
      color: 'info',
      text: 'INICIO'
    }
  },

  // GESTIÓN DE ACCESOS Y SEGURIDAD
  {
    title: true,
    name: 'GESTIÓN DE ACCESOS',
    class: 'nav-title'
  },
  {
    name: 'Usuarios',
    url: '/dashboard',
    icon: 'fa-solid fa-user',
    class: 'nav-item-users'
  },
  {
    name: 'Roles',
    url: '/access/roles',
    icon: 'fa-solid fa-shield-halved',
    class: 'nav-item-roles'
  },
  {
    name: 'Permisos',
    url: '/access/permissions',
    icon: 'fa-solid fa-lock',
    class: 'nav-item-permissions'
  },
  
  // GESTIÓN DE ESTACIONAMIENTO
  {
    title: true,
    name: 'GESTIÓN DE ESTACIONAMIENTO',
    class: 'nav-title'
  },
  {
    name: 'Espacios',
    url: '/parking/spaces',
    icon: 'fa-solid fa-table-cells',
    class: 'nav-item-spaces',
    badge: {
      color: 'success',
      text: '250'
    }
  },
  {
    name: 'Vehículos',
    url: '/parking/vehicles',
    icon: 'fa-solid fa-car',
    class: 'nav-item-vehicles'
  },
  {
    name: 'Tarifas',
    url: '/parking/rates',
    icon: 'fa-solid fa-dollar-sign',
    class: 'nav-item-rates'
  },
  {
    name: 'Infracciones',
    url: '/parking/violations',
    icon: 'fa-solid fa-triangle-exclamation',
    class: 'nav-item-violations',
    badge: {
      color: 'danger',
      text: '3'
    }
  },

  // CLIENTES Y PAGOS
  {
    title: true,
    name: 'CLIENTES Y PAGOS',
    class: 'nav-title'
  },
  {
    name: 'Clientes',
    url: '/clients',
    icon: 'fa-solid fa-address-book',
    class: 'nav-item-clients'
  },
  {
    name: 'Pagos',
    url: '/payments',
    icon: 'fa-solid fa-credit-card',
    class: 'nav-item-payments'
  },
  {
    name: 'Facturación',
    url: '/billing',
    icon: 'fa-solid fa-file-invoice-dollar',
    class: 'nav-item-billing'
  },

  // MONITOREO Y CONTROL IOT
  {
    title: true,
    name: 'MONITOREO Y CONTROL IOT',
    class: 'nav-title'
  },
  {
    name: 'Sensores',
    url: '/iot/sensors',
    icon: 'fa-solid fa-wifi',
    class: 'nav-item-sensors',
    badge: {
      color: 'info',
      text: 'ACTIVO'
    }
  },
  {
    name: 'Cámaras',
    url: '/iot/cameras',
    icon: 'fa-solid fa-video',
    class: 'nav-item-cameras'
  },
  {
    name: 'Barreras',
    url: '/iot/barriers',
    icon: 'fa-solid fa-road-barrier',
    class: 'nav-item-barriers'
  },
  {
    name: 'Estado de Dispositivos',
    url: '/iot/status',
    icon: 'fa-solid fa-microchip',
    class: 'nav-item-device-status'
  },

  // REPORTES Y ANALÍTICAS
  {
    title: true,
    name: 'REPORTES Y ANALÍTICAS',
    class: 'nav-title'
  },
  {
    name: 'Dashboard Ejecutivo',
    url: '/reports/executive',
    icon: 'fa-solid fa-chart-line',
    class: 'nav-item-executive'
  },
  {
    name: 'Reportes Generales',
    url: '/reports/general',
    icon: 'fa-solid fa-chart-pie',
    class: 'nav-item-general-reports'
  },
  {
    name: 'Análisis de Ocupación',
    url: '/reports/occupancy',
    icon: 'fa-solid fa-chart-column',
    class: 'nav-item-occupancy'
  },
  {
    name: 'Reportes Financieros',
    url: '/reports/financial',
    icon: 'fa-solid fa-building-columns',
    class: 'nav-item-financial'
  },
  {
    name: 'Exportar Datos',
    url: '/reports/export',
    icon: 'fa-solid fa-download',
    class: 'nav-item-export'
  },

  // CONFIGURACIÓN DEL SISTEMA
  {
    title: true,
    name: 'CONFIGURACIÓN DEL SISTEMA',
    class: 'nav-title'
  },
  {
    name: 'Configuración General',
    url: '/settings/general',
    icon: 'fa-solid fa-gear',
    class: 'nav-item-general-settings'
  },
  {
    name: 'Zonas de Estacionamiento',
    url: '/settings/zones',
    icon: 'fa-solid fa-map-location-dot',
    class: 'nav-item-zones'
  },
  {
    name: 'Notificaciones',
    url: '/settings/notifications',
    icon: 'fa-solid fa-bell',
    class: 'nav-item-notifications'
  },
  {
    name: 'Integraciones',
    url: '/settings/integrations',
    icon: 'fa-solid fa-puzzle-piece',
    class: 'nav-item-integrations'
  },
  {
    name: 'Backup y Restauración',
    url: '/settings/backup',
    icon: 'fa-solid fa-floppy-disk',
    class: 'nav-item-backup'
  },

  // DOCUMENTACIÓN Y SOPORTE
  {
    title: true,
    name: 'DOCUMENTACIÓN Y SOPORTE',
    class: 'nav-title'
  },
  {
    name: 'Manual de Usuario',
    url: '/docs/user-manual',
    icon: 'fa-solid fa-book',
    class: 'nav-item-manual'
  },
  {
    name: 'Soporte Técnico',
    url: '/support',
    icon: 'fa-solid fa-headset',
    class: 'nav-item-support',
    badge: {
      color: 'success',
      text: 'EN LÍNEA'
    }
  },
  {
    name: 'Acerca del Sistema',
    url: '/about',
    icon: 'fa-solid fa-circle-info',
    class: 'nav-item-about'
  }
];
