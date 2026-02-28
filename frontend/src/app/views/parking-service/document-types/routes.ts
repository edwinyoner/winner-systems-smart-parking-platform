// src/app/views/parking-service/document-types/routes.ts
import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./document-type-list/document-type-list.component').then(m => m.DocumentTypeListComponent),
    data: { title: 'Lista de Tipos de Documento' }
  },
  {
    path: 'create',
    loadComponent: () => import('./document-type-create/document-type-create.component').then(m => m.DocumentTypeCreateComponent),
    data: { title: 'Crear Tipo de Documento' }
  },
  {
    path: ':id',
    loadComponent: () => import('./document-type-detail/document-type-detail.component').then(m => m.DocumentTypeDetailComponent),
    data: { title: 'Detalle de Tipo de Documento' }
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./document-type-edit/document-type-edit.component').then(m => m.DocumentTypeEditComponent),
    data: { title: 'Editar Tipo de Documento' }
  }
];