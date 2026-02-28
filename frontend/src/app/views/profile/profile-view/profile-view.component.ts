import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { 
  CardComponent, 
  CardBodyComponent, 
  CardHeaderComponent,
  ColComponent,
  RowComponent,
  ButtonDirective,
  BadgeComponent,
  AvatarComponent,
  SpinnerComponent
} from '@coreui/angular';
import { IconDirective } from '@coreui/icons-angular';
import { CurrentUserService } from '../../../core/services/current-user.service';
import { UserInfo } from '../../../core/models/auth-response.model';

/**
 * Componente para visualizar el perfil del usuario
 */
@Component({
  selector: 'app-profile-view',
  standalone: true,
  imports: [
    CommonModule,
    RouterLink,
    CardComponent,
    CardBodyComponent,
    CardHeaderComponent,
    ColComponent,
    RowComponent,
    ButtonDirective,
    BadgeComponent,
    AvatarComponent,
    SpinnerComponent,
    IconDirective
  ],
  templateUrl: './profile-view.component.html',
  styleUrls: ['./profile-view.component.scss']
})
export class ProfileViewComponent implements OnInit {

  currentUser: UserInfo | null = null;
  loading = false;

  constructor(
    private currentUserService: CurrentUserService
  ) {}

  ngOnInit(): void {
    this.loadProfile();
  }

  /**
   * Carga el perfil del usuario actual
   */
  loadProfile(): void {
    this.loading = true;

    this.currentUserService.currentUser$.subscribe({
      next: (user) => {
        this.currentUser = user;
        this.loading = false;
      },
      error: (error) => {
        console.error('Error al cargar perfil:', error);
        this.loading = false;
      }
    });
  }

  /**
   * Obtiene las iniciales del usuario para el avatar
   */
  getUserInitials(): string {
    return this.currentUserService.getInitials();
  }

  /**
   * Obtiene el nombre completo del usuario
   */
  getFullName(): string {
    return this.currentUserService.getFullName();
  }

  /**
   * Formatea la fecha de creación
   */
  getCreatedDate(): string {
    // Como UserInfo no tiene createdAt, retornamos un placeholder
    return 'N/A';
  }
}