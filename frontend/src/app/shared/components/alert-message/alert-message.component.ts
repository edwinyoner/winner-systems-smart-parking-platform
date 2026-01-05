import { Component, Input, Output, EventEmitter } from '@angular/core';
import { CommonModule } from '@angular/common';

/**
 * Componente de mensajes de alerta
 * 
 * Tipos: success, error, warning, info
 * Puede ser dismissible (con botón X para cerrar)
 * 
 * Uso:
 * <app-alert-message 
 *   type="success" 
 *   message="Operación exitosa"
 *   [dismissible]="true"
 *   (onDismiss)="handleDismiss()">
 * </app-alert-message>
 */
@Component({
  selector: 'app-alert-message',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './alert-message.component.html',
  styleUrls: ['./alert-message.component.scss']
})
export class AlertMessageComponent {
  
  @Input() type: 'success' | 'error' | 'warning' | 'info' = 'info';
  @Input() message: string = '';
  @Input() dismissible: boolean = true;
  @Input() icon: string = ''; // Icono personalizado (opcional)
  
  @Output() onDismiss = new EventEmitter<void>();

  visible = true;

  /**
   * Obtiene el icono según el tipo de alerta
   */
  getIcon(): string {
    if (this.icon) {
      return this.icon;
    }

    switch (this.type) {
      case 'success':
        return 'fas fa-check-circle';
      case 'error':
        return 'fas fa-exclamation-circle';
      case 'warning':
        return 'fas fa-exclamation-triangle';
      case 'info':
        return 'fas fa-info-circle';
      default:
        return 'fas fa-info-circle';
    }
  }

  /**
   * Cierra la alerta
   */
  dismiss(): void {
    this.visible = false;
    this.onDismiss.emit();
  }
}