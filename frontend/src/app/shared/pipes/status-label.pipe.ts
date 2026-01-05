import { Pipe, PipeTransform } from '@angular/core';

/**
 * Pipe para convertir estados booleanos en badges visuales
 * 
 * Uso:
 * <span [innerHTML]="user.status | statusLabel"></span>
 * <span [innerHTML]="user.emailVerified | statusLabel:'verificado':'no verificado'"></span>
 * 
 * Genera HTML con clases CSS para badges:
 * - true  → <span class="badge badge-success">Activo</span>
 * - false → <span class="badge badge-danger">Inactivo</span>
 */
@Pipe({
  name: 'statusLabel',
  standalone: true
})
export class StatusLabelPipe implements PipeTransform {

  transform(
    value: boolean | null | undefined, 
    trueLabel: string = 'Activo', 
    falseLabel: string = 'Inactivo',
    trueClass: string = 'success',
    falseClass: string = 'danger'
  ): string {
    
    if (value === null || value === undefined) {
      return `<span class="badge badge-secondary">N/A</span>`;
    }

    const label = value ? trueLabel : falseLabel;
    const cssClass = value ? trueClass : falseClass;

    return `<span class="badge badge-${cssClass}">${label}</span>`;
  }
}