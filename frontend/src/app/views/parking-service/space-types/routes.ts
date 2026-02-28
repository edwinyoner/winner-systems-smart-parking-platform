// src/app/views/parking-service/space-types/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./space-type-list/space-type-list.component').then(m => m.SpaceTypeListComponent),
    data: { title: 'Lista de Tipos de Espacio' }
  },
  {
    path: 'create',
    loadComponent: () => import('./space-type-create/space-type-create.component').then(m => m.SpaceTypeCreateComponent),
    data: { title: 'Crear Tipo de Espacio' }
  },
  {
    path: ':id',
    loadComponent: () => import('./space-type-detail/space-type-detail.component').then(m => m.SpaceTypeDetailComponent),
    data: { title: 'Detalle de Tipo de Espacio' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./space-type-edit/space-type-edit.component').then(m => m.SpaceTypeEditComponent),
    data: { title: 'Editar Tipo de Espacio' }
  }
];