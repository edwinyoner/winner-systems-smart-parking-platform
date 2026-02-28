// src/app/views/parking-service/shifts/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./shift-list/shift-list.component').then(m => m.ShiftListComponent),
    data: { title: 'Lista de Turnos' }
  },
  {
    path: 'create',
    loadComponent: () => import('./shift-create/shift-create.component').then(m => m.ShiftCreateComponent),
    data: { title: 'Crear Turno' }
  },
  {
    path: ':id',
    loadComponent: () => import('./shift-detail/shift-detail.component').then(m => m.ShiftDetailComponent),
    data: { title: 'Detalle de Turno' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./shift-edit/shift-edit.component').then(m => m.ShiftEditComponent),
    data: { title: 'Editar Turno' }
  }
];