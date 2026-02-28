// src/app/views/parking-service/rates/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./rate-list/rate-list.component').then(m => m.RateListComponent),
    data: { title: 'Lista de Tarifas' }
  },
  {
    path: 'create',
    loadComponent: () => import('./rate-create/rate-create.component').then(m => m.RateCreateComponent),
    data: { title: 'Crear Tarifa' }
  },
  {
    path: ':id',
    loadComponent: () => import('./rate-detail/rate-detail.component').then(m => m.RateDetailComponent),
    data: { title: 'Detalle de Tarifa' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./rate-edit/rate-edit.component').then(m => m.RateEditComponent),
    data: { title: 'Editar Tarifa' }
  }
];