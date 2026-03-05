// src/app/views/parking-service/payments/routes.ts

import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    data: { title: 'Pagos' },
    children: [
      {
        path: '',
        redirectTo: 'list',
        pathMatch: 'full'
      },
      {
        path: 'list',
        loadComponent: () =>
          import('./payment-list/payment-list.component').then(
            (m) => m.PaymentListComponent
          ),
        data: { title: 'Historial de Pagos' }
      },
      {
        path: ':id',
        loadComponent: () =>
          import('./payment-detail/payment-detail.component').then(
            (m) => m.PaymentDetailComponent
          ),
        data: { title: 'Detalle de Pago' }
      }
    ]
  }
];