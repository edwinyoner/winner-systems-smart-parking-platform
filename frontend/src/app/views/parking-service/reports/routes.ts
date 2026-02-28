// src/app/views/parking-service/reports/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    redirectTo: 'daily',
    pathMatch: 'full'
  },
  {
    path: 'daily',
    loadComponent: () => import('./daily-report/daily-report.component').then(m => m.DailyReportComponent),
    data: { title: 'Reporte Diario' }
  },
  {
    path: 'revenue',
    loadComponent: () => import('./revenue-report/revenue-report.component').then(m => m.RevenueReportComponent),
    data: { title: 'Reporte de Ingresos' }
  },
  {
    path: 'occupancy',
    loadComponent: () => import('./occupancy-report/occupancy-report.component').then(m => m.OccupancyReportComponent),
    data: { title: 'Reporte de Ocupación' }
  }
];