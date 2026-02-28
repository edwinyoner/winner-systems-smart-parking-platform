// src/app/views/parking-service/payment-types/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./payment-type-list/payment-type-list.component').then(m => m.PaymentTypeListComponent),
    data: { title: 'Lista de Tipos de Pago' }
  },
  {
    path: 'create',
    loadComponent: () => import('./payment-type-create/payment-type-create.component').then(m => m.PaymentTypeCreateComponent),
    data: { title: 'Crear Tipo de Pago' }
  },
  {
    path: ':id',
    loadComponent: () => import('./payment-type-detail/payment-type-detail.component').then(m => m.PaymentTypeDetailComponent),
    data: { title: 'Detalle de Tipo de Pago' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./payment-type-edit/payment-type-edit.component').then(m => m.PaymentTypeEditComponent),
    data: { title: 'Editar Tipo de Pago' }
  }
];
