import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';

import { DocumentTypeService } from '../../../../core/services/parking/document-type.service';
import { DocumentType } from '../../../../core/models/parking/document-type.model';
import { AlertMessageComponent } from '../../../../shared/components/alert-message/alert-message.component';

@Component({
  selector: 'app-document-type-detail',
  standalone: true,
  imports: [CommonModule, RouterLink, AlertMessageComponent],
  templateUrl: './document-type-detail.component.html',
  styleUrls: ['./document-type-detail.component.css']
})
export class DocumentTypeDetailComponent implements OnInit {

  documentTypeId!: number;
  documentType: DocumentType | null = null;
  isLoading = true;
  errorMessage: string | null = null;

  constructor(
    private documentTypeService: DocumentTypeService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.documentTypeId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadDocumentType();
  }

  private loadDocumentType(): void {
    this.isLoading = true;
    this.documentTypeService.getById(this.documentTypeId).subscribe({
      next: (dt: DocumentType) => { this.documentType = dt; this.isLoading = false; },
      error: (err) => {
        this.isLoading    = false;
        this.errorMessage = 'Error al cargar los detalles del tipo de documento';
        console.error(err);
      }
    });
  }

  goBack(): void { this.router.navigate(['/document-types']); }
  dismissError(): void { this.errorMessage = null; }
}