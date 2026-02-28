// src/app/core/models/parking/document-type.model.ts

export interface DocumentType {
  id?: number;
  code: string;
  name: string;
  description?: string;
  status: boolean;
  createdAt?: string;
  updatedAt?: string;
}

export interface DocumentTypeRequest {
  code: string;
  name: string;
  description?: string;
  status?: boolean;
}