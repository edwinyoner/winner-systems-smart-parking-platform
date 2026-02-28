// src/app/views/parking-service/vehicles/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./vehicle-list/vehicle-list.component').then(m => m.VehicleListComponent),
    data: { title: 'Lista de Vehículos' }
  },
  {
    path: 'create',
    loadComponent: () => import('./vehicle-create/vehicle-create.component').then(m => m.VehicleCreateComponent),
    data: { title: 'Crear Vehículo' }
  },
  {
    path: ':id',
    loadComponent: () => import('./vehicle-detail/vehicle-detail.component').then(m => m.VehicleDetailComponent),
    data: { title: 'Detalle de Vehículo' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./vehicle-edit/vehicle-edit.component').then(m => m.VehicleEditComponent),
    data: { title: 'Editar Vehículo' }
  }
];