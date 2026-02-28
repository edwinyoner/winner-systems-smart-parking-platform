import { Component, OnInit } from '@angular/core';
import { RouterLink, RouterOutlet } from '@angular/router';
import { NgScrollbar } from 'ngx-scrollbar';
import { CommonModule } from '@angular/common';

import {
  ContainerComponent,
  ShadowOnScrollDirective,
  SidebarBrandComponent,
  SidebarComponent,
  SidebarFooterComponent,
  SidebarHeaderComponent,
  SidebarNavComponent,
  SidebarToggleDirective,
  SidebarTogglerDirective
} from '@coreui/angular';

import { DefaultFooterComponent, DefaultHeaderComponent } from './';
import { navItems, INavDataWithPermissions } from './_nav';
import { AuthContextService } from '../../core/services/auth-context.service';
import { PermissionService } from '../../core/services/permission.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './default-layout.component.html',
  styleUrls: ['./default-layout.component.scss'],
  imports: [
    CommonModule,
    SidebarComponent,
    SidebarHeaderComponent,
    SidebarBrandComponent,
    SidebarNavComponent,
    SidebarFooterComponent,
    SidebarToggleDirective,
    SidebarTogglerDirective,
    ContainerComponent,
    DefaultFooterComponent,
    DefaultHeaderComponent,
    NgScrollbar,
    RouterOutlet,
    RouterLink,
    ShadowOnScrollDirective
  ]
})
export class DefaultLayoutComponent implements OnInit {
  
  public navItems: INavDataWithPermissions[] = [];
  public fullName: string = '';
  public userInitials: string = '';
  public profilePicture: string | null = null;
  public activeRole: string = '';
  public roleBadgeClass: string = '';

  constructor(
    private authContext: AuthContextService,
    private permissionService: PermissionService  
  ) {}

  ngOnInit(): void {
    this.loadUserInfo();
    this.filterNavItems();
  }

  private loadUserInfo(): void {
    this.fullName = this.authContext.getFullName();
    this.userInitials = this.authContext.getUserInitials();
    this.profilePicture = this.authContext.getProfilePicture();
    this.activeRole = this.authContext.getActiveRole() || '';
    this.roleBadgeClass = this.authContext.getRoleBadgeClass();
  }

  /**
   * Filtra el menú según los PERMISOS del usuario
   */
  private filterNavItems(): void {
    this.navItems = navItems.filter(item => {
      // Si no tiene permisos requeridos, mostrar a todos
      if (!item.requiredPermissions || item.requiredPermissions.length === 0) {
        return true;
      }

      // Verificar si tiene AL MENOS UNO de los permisos
      return this.permissionService.hasAnyPermission(item.requiredPermissions);
    });
  }
}