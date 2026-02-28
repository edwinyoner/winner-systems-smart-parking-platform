import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./parking-list/parking-list.component').then(m => m.ParkingListComponent),
  },
  {
    path: 'create',
    loadComponent: () =>
      import('./parking-create/parking-create.component').then(m => m.ParkingCreateComponent),
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./parking-detail/parking-detail.component').then(m => m.ParkingDetailComponent),
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./parking-edit/parking-edit.component').then(m => m.ParkingEditComponent),
  },
];