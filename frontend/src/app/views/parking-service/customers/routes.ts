// src/app/views/parking-service/customers/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./customer-list/customer-list.component').then(m => m.CustomerListComponent),
    data: { title: 'Lista de Clientes' }
  },
  {
    path: 'create',
    loadComponent: () => import('./customer-create/customer-create.component').then(m => m.CustomerCreateComponent),
    data: { title: 'Crear Cliente' }
  },
  {
    path: ':id',
    loadComponent: () => import('./customer-detail/customer-detail.component').then(m => m.CustomerDetailComponent),
    data: { title: 'Detalle de Cliente' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./customer-edit/customer-edit.component').then(m => m.CustomerEditComponent),
    data: { title: 'Editar Cliente' }
  }
];