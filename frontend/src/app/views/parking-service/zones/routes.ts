// src/app/views/parking-service/zones/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./zone-list/zone-list.component').then(m => m.ZoneListComponent),
    data: { title: 'Lista de Zonas' }
  },
  {
    path: 'create',
    loadComponent: () => import('./zone-create/zone-create.component').then(m => m.ZoneCreateComponent),
    data: { title: 'Crear Zona' }
  },
  {
    path: ':id',
    loadComponent: () => import('./zone-detail/zone-detail.component').then(m => m.ZoneDetailComponent),
    data: { title: 'Detalle de Zona' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./zone-edit/zone-edit.component').then(m => m.ZoneEditComponent),
    data: { title: 'Editar Zona' }
  }
];