import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class IconService {
  
  // Mapeo de iconos Font Awesome
  private iconMap: { [key: string]: string } = {
    // Dashboard
    'fa-solid fa-gauge-high': '\uf625',
    
    // Estacionamiento
    'fa-solid fa-table-cells': '\uf00a',
    'fa-solid fa-car': '\uf1b9',
    'fa-solid fa-calendar-check': '\uf274',
    'fa-solid fa-dollar-sign': '\uf155',
    'fa-solid fa-triangle-exclamation': '\uf071',
    
    // Seguridad
    'fa-solid fa-users': '\uf0c0',
    'fa-solid fa-shield-halved': '\uf3ed',
    'fa-solid fa-lock': '\uf023',
    'fa-solid fa-clock-rotate-left': '\uf1da',
    
    // Clientes y Pagos
    'fa-solid fa-address-book': '\uf2b9',
    'fa-solid fa-credit-card': '\uf09d',
    'fa-solid fa-file-invoice-dollar': '\uf571',
    'fa-solid fa-star': '\uf005',
    
    // IoT
    'fa-solid fa-wifi': '\uf1eb',
    'fa-solid fa-video': '\uf03d',
    'fa-solid fa-road-barrier': '\ue562',
    'fa-solid fa-microchip': '\uf2db',
    
    // Reportes
    'fa-solid fa-chart-line': '\uf201',
    'fa-solid fa-chart-pie': '\uf200',
    'fa-solid fa-chart-column': '\ue0e3',
    'fa-solid fa-building-columns': '\uf19c',
    'fa-solid fa-download': '\uf019',
    
    // Configuración
    'fa-solid fa-gear': '\uf013',
    'fa-solid fa-map-location-dot': '\uf5a0',
    'fa-solid fa-bell': '\uf0f3',
    'fa-solid fa-puzzle-piece': '\uf12e',
    'fa-solid fa-floppy-disk': '\uf0c7',
    
    // Documentación
    'fa-solid fa-book': '\uf02d',
    'fa-solid fa-code': '\uf121',
    'fa-solid fa-headset': '\uf590',
    'fa-solid fa-circle-info': '\uf05a',
    'fa-solid fa-award': '\uf559'
  };

  getIconUnicode(iconClass: string): string {
    return this.iconMap[iconClass] || '\uf128'; // default icon
  }
}