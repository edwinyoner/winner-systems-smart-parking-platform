import { Routes } from '@angular/router';
import { RoleGuard } from '../../core/guards/role.guard';

export const routes: Routes = [
  // Lista de usuarios (ruta principal)
  {
    path: '',
    loadComponent: () => import('./user-list/user-list.component').then(m => m.UserListComponent),
    data: { 
      title: 'Usuarios',
      requiredPermissions: ['users.read']
    }
  },
  
  // Crear nuevo usuario
  {
    path: 'create',
    loadComponent: () => import('./user-create/user-create.component').then(m => m.UserCreateComponent),
    canActivate: [RoleGuard],
    data: { 
      title: 'Crear Usuario',
      requiredPermissions: ['users.create']
    }
  },
  
  // Ver detalle de usuario
  {
    path: ':id',
    loadComponent: () => import('./user-detail/user-detail.component').then(m => m.UserDetailComponent),
    data: { 
      title: 'Detalle de Usuario',
      requiredPermissions: ['users.read']
    }
  },
  
  // Editar usuario
  {
    path: ':id/edit',
    loadComponent: () => import('./user-edit/user-edit.component').then(m => m.UserEditComponent),
    canActivate: [RoleGuard],
    data: { 
      title: 'Editar Usuario',
      requiredPermissions: ['users.update']
    }
  }
];