// src/app/views/parking-service/transactions/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'active',
    pathMatch: 'full'
  },
  {
    path: 'active',
    loadComponent: () => import('./active-transactions/active-transactions.component').then(m => m.ActiveTransactionsComponent),
    data: { title: 'Transacciones Activas' }
  },
  {
    path: 'entry',
    loadComponent: () => import('./transaction-entry/transaction-entry.component').then(m => m.TransactionEntryComponent),
    data: { title: 'Registrar Entrada' }
  },
  {
    path: 'exit',
    loadComponent: () => import('./transaction-exit/transaction-exit.component').then(m => m.TransactionExitComponent),
    data: { title: 'Registrar Salida' }
  },
  {
    path: 'payment',
    loadComponent: () => import('./transaction-payment/transaction-payment.component').then(m => m.TransactionPaymentComponent),
    data: { title: 'Procesar Pago' }
  },
  {
    path: 'list',
    loadComponent: () => import('./transaction-list/transaction-list.component').then(m => m.TransactionListComponent),
    data: { title: 'Historial de Transacciones' }
  },
  {
    path: ':id',
    loadComponent: () => import('./transaction-detail/transaction-detail.component').then(m => m.TransactionDetailComponent),
    data: { title: 'Detalle de Transacción' }
  }
];