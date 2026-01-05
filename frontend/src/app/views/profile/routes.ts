import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./profile-view/profile-view.component').then(m => m.ProfileViewComponent),
    title: 'Mi Perfil'
  },
  {
    path: 'edit',
    loadComponent: () => import('./profile-edit/profile-edit.component').then(m => m.ProfileEditComponent),
    title: 'Editar Perfil'
  },
  {
    path: 'change-password',
    loadComponent: () => import('./change-password/change-password.component').then(m => m.ChangePasswordComponent),
    title: 'Cambiar Contraseña'
  }
];