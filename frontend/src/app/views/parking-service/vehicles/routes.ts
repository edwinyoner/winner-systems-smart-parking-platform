// src/app/views/parking-service/vehicles/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./vehicle-list/vehicle-list.component').then(
        (m) => m.VehicleListComponent
      ),
    data: { title: 'Vehículos' },
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./vehicle-detail/vehicle-detail.component').then(
        (m) => m.VehicleDetailComponent
      ),
    data: { title: 'Detalle del Vehículo' },
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./vehicle-edit/vehicle-edit.component').then(
        (m) => m.VehicleEditComponent
      ),
    data: { title: 'Editar Vehículo' },
  },
];