import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { CurrentUserService } from '../../core/services/current-user.service';

/**
 * Directiva estructural para mostrar/ocultar elementos según roles
 * 
 * Uso:
 * <button *hasRole="'ADMIN'">Panel Admin</button>
 * <div *hasRole="['ADMIN', 'AUTORIDAD']">...</div>
 * 
 * Por defecto usa lógica OR (al menos uno de los roles)
 */
@Directive({
  selector: '[hasRole]',
  standalone: true
})
export class HasRoleDirective implements OnInit, OnDestroy {
  
  private roles: string[] = [];
  private subscription?: Subscription;

  @Input() set hasRole(roles: string | string[]) {
    this.roles = Array.isArray(roles) ? roles : [roles];
    this.updateView();
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private currentUserService: CurrentUserService
  ) {}

  ngOnInit(): void {
    // Suscribirse a cambios en el usuario actual
    this.subscription = this.currentUserService.currentUser$.subscribe(() => {
      this.updateView();
    });
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  private updateView(): void {
    const hasRole = this.checkRoles();

    if (hasRole) {
      // Mostrar el elemento
      if (this.viewContainer.length === 0) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    } else {
      // Ocultar el elemento
      this.viewContainer.clear();
    }
  }

  private checkRoles(): boolean {
    if (this.roles.length === 0) {
      return true;
    }

    // Lógica OR: Debe tener AL MENOS UNO de los roles
    return this.roles.some(role => this.currentUserService.hasRole(role));
  }
}