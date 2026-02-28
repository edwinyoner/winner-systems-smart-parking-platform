import { Directive, Input, TemplateRef, ViewContainerRef, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { PermissionService } from '../../core/services/permission.service';

/**
 * Directiva estructural para mostrar/ocultar elementos según permisos
 * 
 * Uso:
 * <button *hasPermission="'users.create'">Crear Usuario</button>
 * <div *hasPermission="['users.create', 'users.update']">...</div>
 * 
 * Por defecto usa lógica OR (al menos uno de los permisos)
 * Para lógica AND usa: *hasPermission="perms; requireAll: true"
 * 
 * <button *appHasPermission="'users.create'">Crear</button>
 * <button *appHasPermission="['users.update', 'users.delete']" [requireAll]="false">Editar o Eliminar</button>
 * 
 * Directiva estructural para mostrar/ocultar elementos según roles
 * 
 * Uso:
 * <div *appHasRole="'ADMIN'">Solo para admin</div>
 * <div *appHasRole="['ADMIN', 'AUTORIDAD']">Para admin o autoridad</div>
 */
@Directive({
  selector: '[appHasPermission]',
  standalone: true
})
export class HasPermissionDirective implements OnInit {
  
  private permissions: string[] = [];
  private requireAll = false;

  constructor(
    private templateRef: TemplateRef<any>,
    private viewContainer: ViewContainerRef,
    private permissionService: PermissionService
  ) {}

  @Input()
  set appHasPermission(permissions: string | string[]) {
    this.permissions = Array.isArray(permissions) ? permissions : [permissions];
    this.updateView();
  }

  @Input()
  set appHasPermissionRequireAll(value: boolean) {
    this.requireAll = value;
    this.updateView();
  }

  ngOnInit(): void {
    this.updateView();
  }

  private updateView(): void {
    const hasPermission = this.requireAll
      ? this.permissionService.hasAllPermissions(this.permissions)
      : this.permissionService.hasAnyPermission(this.permissions);

    if (hasPermission) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}