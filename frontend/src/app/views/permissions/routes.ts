import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./permission-list/permission-list.component').then(m => m.PermissionListComponent)
  },
  {
    path: 'create',
    loadComponent: () => import('./permission-create/permission-create.component').then(m => m.PermissionCreateComponent)
  },
  {
    path: ':id',
    loadComponent: () => import('./permission-detail/permission-detail.component').then(m => m.PermissionDetailComponent)
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./permission-edit/permission-edit.component').then(m => m.PermissionEditComponent)
  }
];