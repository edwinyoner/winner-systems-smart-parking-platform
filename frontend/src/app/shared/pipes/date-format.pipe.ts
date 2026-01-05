import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe para formatear fechas en español
 * 
 * Uso:
 * {{ date | dateFormat }}              // 15 de enero de 2025, 14:30
 * {{ date | dateFormat:'short' }}      // 15/01/2025
 * {{ date | dateFormat:'medium' }}     // 15 ene 2025, 14:30
 * {{ date | dateFormat:'long' }}       // 15 de enero de 2025, 14:30:45
 * {{ date | dateFormat:'time' }}       // 14:30
 * {{ date | dateFormat:'date' }}       // 15 de enero de 2025
 * {{ date | dateFormat:'relative' }}   // Hace 2 horas
 */
@Pipe({
  name: 'dateFormat',
  standalone: true
})
export class DateFormatPipe implements PipeTransform {

  private readonly months = [
    'enero', 'febrero', 'marzo', 'abril', 'mayo', 'junio',
    'julio', 'agosto', 'septiembre', 'octubre', 'noviembre', 'diciembre'
  ];

  private readonly monthsShort = [
    'ene', 'feb', 'mar', 'abr', 'may', 'jun',
    'jul', 'ago', 'sep', 'oct', 'nov', 'dic'
  ];

  transform(value: string | Date | null | undefined, format: string = 'default'): string {
    if (!value) {
      return '-';
    }

    const date = new Date(value);

    if (isNaN(date.getTime())) {
      return '-';
    }

    switch (format) {
      case 'short':
        return this.formatShort(date);
      case 'medium':
        return this.formatMedium(date);
      case 'long':
        return this.formatLong(date);
      case 'time':
        return this.formatTime(date);
      case 'date':
        return this.formatDate(date);
      case 'relative':
        return this.formatRelative(date);
      default:
        return this.formatDefault(date);
    }
  }

  /**
   * Formato por defecto: 15 de enero de 2025, 14:30
   */
  private formatDefault(date: Date): string {
    return `${this.formatDate(date)}, ${this.formatTime(date)}`;
  }

  /**
   * Formato corto: 15/01/2025
   */
  private formatShort(date: Date): string {
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear();
    return `${day}/${month}/${year}`;
  }

  /**
   * Formato medio: 15 ene 2025, 14:30
   */
  private formatMedium(date: Date): string {
    const day = date.getDate();
    const month = this.monthsShort[date.getMonth()];
    const year = date.getFullYear();
    const time = this.formatTime(date);
    return `${day} ${month} ${year}, ${time}`;
  }

  /**
   * Formato largo: 15 de enero de 2025, 14:30:45
   */
  private formatLong(date: Date): string {
    const day = date.getDate();
    const month = this.months[date.getMonth()];
    const year = date.getFullYear();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    return `${day} de ${month} de ${year}, ${hours}:${minutes}:${seconds}`;
  }

  /**
   * Solo fecha: 15 de enero de 2025
   */
  private formatDate(date: Date): string {
    const day = date.getDate();
    const month = this.months[date.getMonth()];
    const year = date.getFullYear();
    return `${day} de ${month} de ${year}`;
  }

  /**
   * Solo hora: 14:30
   */
  private formatTime(date: Date): string {
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    return `${hours}:${minutes}`;
  }

  /**
   * Formato relativo: Hace 2 horas, Hace 3 días, etc.
   */
  private formatRelative(date: Date): string {
    const now = new Date();
    const diffMs = now.getTime() - date.getTime();
    const diffSeconds = Math.floor(diffMs / 1000);
    const diffMinutes = Math.floor(diffSeconds / 60);
    const diffHours = Math.floor(diffMinutes / 60);
    const diffDays = Math.floor(diffHours / 24);
    const diffMonths = Math.floor(diffDays / 30);
    const diffYears = Math.floor(diffDays / 365);

    if (diffSeconds < 60) {
      return 'Hace un momento';
    } else if (diffMinutes < 60) {
      return `Hace ${diffMinutes} ${diffMinutes === 1 ? 'minuto' : 'minutos'}`;
    } else if (diffHours < 24) {
      return `Hace ${diffHours} ${diffHours === 1 ? 'hora' : 'horas'}`;
    } else if (diffDays < 30) {
      return `Hace ${diffDays} ${diffDays === 1 ? 'día' : 'días'}`;
    } else if (diffMonths < 12) {
      return `Hace ${diffMonths} ${diffMonths === 1 ? 'mes' : 'meses'}`;
    } else {
      return `Hace ${diffYears} ${diffYears === 1 ? 'año' : 'años'}`;
    }
  }
}