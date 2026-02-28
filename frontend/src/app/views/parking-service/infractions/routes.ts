// src/app/views/parking-service/infractions/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./infraction-list/infraction-list.component').then(m => m.InfractionListComponent),
    data: { title: 'Lista de Infracciones' }
  },
  {
    path: 'create',
    loadComponent: () => import('./infraction-create/infraction-create.component').then(m => m.InfractionCreateComponent),
    data: { title: 'Crear Infracción' }
  },
  {
    path: ':id',
    loadComponent: () => import('./infraction-detail/infraction-detail.component').then(m => m.InfractionDetailComponent),
    data: { title: 'Detalle de Infracción' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./infraction-edit/infraction-edit.component').then(m => m.InfractionEditComponent),
    data: { title: 'Editar Infracción' }
  }
];