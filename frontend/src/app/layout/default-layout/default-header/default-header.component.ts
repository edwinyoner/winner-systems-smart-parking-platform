import { NgTemplateOutlet } from '@angular/common';
import { Component, computed, inject, input, OnInit, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';

import {
  AvatarComponent,
  BadgeComponent,
  BreadcrumbRouterComponent,
  ColorModeService,
  ContainerComponent,
  DropdownComponent,
  DropdownDividerDirective,
  DropdownHeaderDirective,
  DropdownItemDirective,
  DropdownMenuDirective,
  DropdownToggleDirective,
  HeaderComponent,
  HeaderNavComponent,
  HeaderTogglerDirective,
  NavLinkDirective,
  SidebarToggleDirective
} from '@coreui/angular';

import { IconDirective } from '@coreui/icons-angular';
import { AuthService } from '../../../core/services/auth.service';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { UserInfo } from '../../../core/models/auth-response.model';

@Component({
  selector: 'app-default-header',
  templateUrl: './default-header.component.html',
  imports: [
    ContainerComponent,
    HeaderTogglerDirective,
    SidebarToggleDirective,
    IconDirective,
    HeaderNavComponent,
    NavLinkDirective,
    RouterLink,
    NgTemplateOutlet,
    BreadcrumbRouterComponent,
    DropdownComponent,
    DropdownToggleDirective,
    AvatarComponent,
    DropdownMenuDirective,
    DropdownHeaderDirective,
    DropdownItemDirective,
    BadgeComponent,
    DropdownDividerDirective
  ]
})
export class DefaultHeaderComponent extends HeaderComponent implements OnInit {

  readonly #colorModeService = inject(ColorModeService);
  readonly colorMode = this.#colorModeService.colorMode;
  
  private authService = inject(AuthService);
  private router = inject(Router);
  private currentUserService = inject(CurrentUserService);

  // ✅ Signal para el usuario actual
  currentUser = signal<UserInfo | null>(null);

  readonly colorModes = [
    { name: 'light', text: 'Light', icon: 'cilSun' },
    { name: 'dark', text: 'Dark', icon: 'cilMoon' },
    { name: 'auto', text: 'Auto', icon: 'cilContrast' }
  ];

  readonly icons = computed(() => {
    const currentMode = this.colorMode();
    return this.colorModes.find(mode => mode.name === currentMode)?.icon ?? 'cilSun';
  });

  constructor() {
    super();
  }

  ngOnInit(): void {
    // Suscribirse al usuario actual
    this.currentUserService.currentUser$.subscribe(user => {
      this.currentUser.set(user);
    });
  }

  sidebarId = input('sidebar1');

  /**
   * Cierra sesión del usuario
   */
  logout(): void {
    this.authService.logout();
  }

  /**
   * Navega al perfil del usuario
   */
  goToProfile(): void {
    this.router.navigate(['/profile']);
  }

  /**
   * Navega a editar perfil
   */
  goToEditProfile(): void {
    this.router.navigate(['/profile/edit']);
  }

  /**
   * Navega a cambiar contraseña
   */
  goToChangePassword(): void {
    this.router.navigate(['/profile/change-password']);
  }
}