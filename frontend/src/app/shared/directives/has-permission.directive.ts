import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PermissionService } from '../../core/services/permission.service';
import { CurrentUserService } from '../../core/services/current-user.service';

/**
 * Directiva estructural para mostrar/ocultar elementos según permisos
 * 
 * Uso:
 * <button *hasPermission="'users.create'">Crear Usuario</button>
 * <div *hasPermission="['users.create', 'users.update']">...</div>
 * 
 * Por defecto usa lógica OR (al menos uno de los permisos)
 * Para lógica AND usa: *hasPermission="perms; requireAll: true"
 */
@Directive({
  selector: '[hasPermission]',
  standalone: true
})
export class HasPermissionDirective implements OnInit, OnDestroy {
  
  private permissions: string[] = [];
  private requireAll: boolean = false;
  private subscription?: Subscription;

  @Input() set hasPermission(permissions: string | string[]) {
    this.permissions = Array.isArray(permissions) ? permissions : [permissions];
    this.updateView();
  }

  @Input() set hasPermissionRequireAll(requireAll: boolean) {
    this.requireAll = requireAll;
    this.updateView();
  }

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private permissionService: PermissionService,
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
    const hasPermission = this.checkPermissions();

    if (hasPermission) {
      // Mostrar el elemento
      if (this.viewContainer.length === 0) {
        this.viewContainer.createEmbeddedView(this.templateRef);
      }
    } else {
      // Ocultar el elemento
      this.viewContainer.clear();
    }
  }

  private checkPermissions(): boolean {
    if (this.permissions.length === 0) {
      return true;
    }

    if (this.requireAll) {
      // Lógica AND: Debe tener TODOS los permisos
      return this.permissionService.hasAllPermissions(this.permissions);
    } else {
      // Lógica OR: Debe tener AL MENOS UNO de los permisos
      return this.permissionService.hasAnyPermission(this.permissions);
    }
  }
}