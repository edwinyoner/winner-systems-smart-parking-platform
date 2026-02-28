import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { DocumentTypeService } from '../../../../core/services/parking/document-type.service';
import { DocumentType } from '../../../../core/models/parking/document-type.model';
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-document-type-edit',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, AlertMessageComponent],
  templateUrl: './document-type-edit.component.html',
  styleUrls: ['./document-type-edit.component.css']
})
export class DocumentTypeEditComponent implements OnInit {

  documentTypeId!: number;
  form!: FormGroup;
  isLoading = true;
  isSubmitting = false;
  errorMessage: string | null = null;
  currentDocumentType: DocumentType | null = null;

  constructor(
    private fb: FormBuilder,
    private documentTypeService: DocumentTypeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.documentTypeId = Number(this.route.snapshot.paramMap.get('id'));
    this.initForm();
    this.loadDocumentType();
  }

  private initForm(): void {
    this.form = this.fb.group({
      name:        ['', [Validators.required, Validators.maxLength(100)]],
      description: ['', Validators.maxLength(255)],
      status:      [true]
    });
  }

  private loadDocumentType(): void {
    this.isLoading = true;
    this.documentTypeService.getById(this.documentTypeId).subscribe({
      next: (dt: DocumentType) => {
        this.currentDocumentType = dt;
        this.form.patchValue({ name: dt.name, description: dt.description, status: dt.status });
        this.isLoading = false;
      },
      error: (err) => {
        this.isLoading    = false;
        this.errorMessage = 'Error al cargar el tipo de documento';
        console.error(err);
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) { this.markTouched(this.form); return; }
    this.isSubmitting = true;
    this.errorMessage = null;

    this.documentTypeService.update(this.documentTypeId, this.form.value).subscribe({
      next: (res) => {
        this.isSubmitting = false;
        this.router.navigate(['/document-types'], {
          state: { successMessage: `Tipo de documento "${res.name}" actualizado exitosamente` }
        });
      },
      error: (err) => {
        this.isSubmitting = false;
        this.errorMessage = err.error?.message || 'Error al actualizar el tipo de documento';
        window.scrollTo({ top: 0, behavior: 'smooth' });
      }
    });
  }

  cancel(): void { this.router.navigate(['/document-types']); }

  isFieldInvalid(f: string): boolean {
    const c = this.form.get(f);
    return !!(c && c.invalid && (c.dirty || c.touched));
  }

  getFieldError(f: string): string {
    const c = this.form.get(f);
    if (c?.errors?.['required'])   return 'Este campo es obligatorio';
    if (c?.errors?.['maxlength'])  return `Máximo ${c.errors['maxlength'].requiredLength} caracteres`;
    return '';
  }

  private markTouched(fg: FormGroup): void {
    Object.keys(fg.controls).forEach(k => fg.get(k)?.markAsTouched());
  }

  dismissError(): void { this.errorMessage = null; }
}