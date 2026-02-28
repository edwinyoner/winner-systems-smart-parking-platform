import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

import { DocumentTypeService } from '../../../../core/services/parking/document-type.service';
import { DocumentType } from '../../../../core/models/parking/document-type.model';
import { ParkingPagedResponse } from '../../../../core/models/pagination.model';
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-document-type-list',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AlertMessageComponent],
  templateUrl: './document-type-list.component.html',
  styleUrls: ['./document-type-list.component.css']
})
export class DocumentTypeListComponent implements OnInit {

  filterForm!: FormGroup;
  documentTypes: DocumentType[] = [];
  isLoading = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  currentPage = 1;
  pageSize = 10;
  totalItems = 0;
  totalPages = 0;
  pageSizeOptions = [5, 10, 15, 20, 25, 50];

  showDeleteConfirm = false;
  documentTypeToDelete: DocumentType | null = null;

  Math = Math;

  constructor(
    private fb: FormBuilder,
    private documentTypeService: DocumentTypeService,
    private router: Router
  ) {
    const state = window.history.state;
    if (state?.['successMessage']) {
      this.successMessage = state['successMessage'];
      setTimeout(() => (this.successMessage = null), 5000);
    }
  }

  ngOnInit(): void {
    this.initFilterForm();
    this.loadDocumentTypes();
    this.setupFilterListeners();
  }

  private initFilterForm(): void {
    this.filterForm = this.fb.group({ search: [''], status: [''] });
  }

  private setupFilterListeners(): void {
    this.filterForm.get('search')?.valueChanges
      .pipe(debounceTime(400), distinctUntilChanged())
      .subscribe(() => { this.currentPage = 1; this.loadDocumentTypes(); });

    this.filterForm.get('status')?.valueChanges
      .subscribe(() => { this.currentPage = 1; this.loadDocumentTypes(); });
  }

  loadDocumentTypes(): void {
    this.isLoading = true;
    const search = this.filterForm.get('search')?.value ?? '';
    const status = this.filterForm.get('status')?.value ?? '';

    this.documentTypeService.getAll(this.currentPage - 1, this.pageSize, search, status).subscribe({
      next: (response: ParkingPagedResponse<DocumentType>) => {
        this.documentTypes = response.content;
        this.totalItems  = response.totalElements;
        this.totalPages  = response.totalPages;
        this.currentPage = response.pageNumber + 1;
        this.pageSize    = response.pageSize;
        this.isLoading   = false;
      },
      error: (error) => {
        this.isLoading    = false;
        this.errorMessage = 'Error al cargar los tipos de documento';
        console.error(error);
      }
    });
  }

  clearFilters(): void {
    this.filterForm.reset({ search: '', status: '' }, { emitEvent: false });
    this.currentPage = 1;
    this.loadDocumentTypes();
  }

  onPageSizeChange(event: any): void {
    this.pageSize = Number(event.target.value);
    this.currentPage = 1;
    this.loadDocumentTypes();
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.loadDocumentTypes();
    }
  }

  getPages(): number[] {
    const pages = [];
    const max = 5;
    let start = Math.max(1, this.currentPage - Math.floor(max / 2));
    let end   = Math.min(this.totalPages, start + max - 1);
    if (end - start + 1 < max) start = Math.max(1, end - max + 1);
    for (let i = start; i <= end; i++) pages.push(i);
    return pages;
  }

  createDocumentType(): void   { this.router.navigate(['/document-types/create']); }
  viewDocumentType(id: number): void { this.router.navigate(['/document-types', id]); }
  editDocumentType(id: number): void { this.router.navigate(['/document-types', id, 'edit']); }

  confirmDelete(dt: DocumentType): void {
    this.documentTypeToDelete = dt;
    this.showDeleteConfirm = true;
  }

  cancelDelete(): void {
    this.showDeleteConfirm = false;
    this.documentTypeToDelete = null;
  }

  deleteDocumentType(): void {
    if (!this.documentTypeToDelete?.id) return;
    this.isLoading = true;

    this.documentTypeService.delete(this.documentTypeToDelete.id).subscribe({
      next: () => {
        this.isLoading      = false;
        this.successMessage = `Tipo de documento "${this.documentTypeToDelete?.name}" eliminado exitosamente`;
        this.showDeleteConfirm   = false;
        this.documentTypeToDelete = null;
        this.loadDocumentTypes();
      },
      error: (error) => {
        this.isLoading    = false;
        this.errorMessage = error.error?.message || 'No se pudo eliminar el tipo de documento';
        this.showDeleteConfirm = false;
      }
    });
  }

  dismissSuccess(): void { this.successMessage = null; }
  dismissError(): void   { this.errorMessage = null; }
}