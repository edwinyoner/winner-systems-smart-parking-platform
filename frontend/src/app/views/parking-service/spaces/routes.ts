// src/app/views/parking-service/spaces/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./space-list/space-list.component').then(m => m.SpaceListComponent),
    data: { title: 'Lista de Espacios' }
  },
  {
    path: 'create',
    loadComponent: () => import('./space-create/space-create.component').then(m => m.SpaceCreateComponent),
    data: { title: 'Crear Espacio' }
  },
  {
    path: ':id',
    loadComponent: () => import('./space-detail/space-detail.component').then(m => m.SpaceDetailComponent),
    data: { title: 'Detalle de Espacio' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./space-edit/space-edit.component').then(m => m.SpaceEditComponent),
    data: { title: 'Editar Espacio' }
  }
];