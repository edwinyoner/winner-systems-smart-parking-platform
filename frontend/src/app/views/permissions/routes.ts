import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./permission-list/permission-list.component').then(m => m.PermissionListComponent)
  }
];