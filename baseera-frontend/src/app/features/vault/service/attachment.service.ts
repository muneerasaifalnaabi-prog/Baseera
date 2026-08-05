import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import {
  Attachment,
  AttachmentAnalysis,
  DocumentType
} from '../Model/attachment.model';


@Injectable({
  providedIn: 'root'
})
export class AttachmentService {

  private readonly http = inject(HttpClient);

  private readonly API_URL =
    'http://localhost:8080/api';


  /**
   * Upload a new attachment for a child
   */
  uploadAttachment(
    childId: number,
    documentType: DocumentType,
    file: File
  ): Observable<Attachment> {

    const formData = new FormData();

    formData.append(
      'file',
      file
    );

    formData.append(
      'documentType',
      documentType
    );

    return this.http.post<Attachment>(
      `${this.API_URL}/children/${childId}/attachments`,
      formData
    );
  }


  /**
   * Get all attachments belonging to a child
   */
  getAttachments(
    childId: number
  ): Observable<Attachment[]> {

    return this.http.get<Attachment[]>(
      `${this.API_URL}/children/${childId}/attachments`
    );
  }


  /**
   * Delete an attachment
   */
  deleteAttachment(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.API_URL}/attachments/${id}`,
      {
        responseType: 'text'
      }
    );
  }


  /**
   * Ask AI to analyze an attachment
   */
  analyzeAttachment(
    id: number
  ): Observable<AttachmentAnalysis> {

    return this.http.post<AttachmentAnalysis>(
      `${this.API_URL}/attachments/${id}/analyze`,
      {}
    );
  }

  getAnalysis(
    id: number
  ): Observable<AttachmentAnalysis> {

    return this.http.get<AttachmentAnalysis>(
      `${this.API_URL}/attachments/${id}/analysis`
    );
  }


  /**
   * Get the child's latest/current plan
   */
  getCurrentPlan(
    childId: number
  ): Observable<AttachmentAnalysis> {

    return this.http.get<AttachmentAnalysis>(
      `${this.API_URL}/children/${childId}/current-plan`
    );
  }

  /**
   * Get recommended centers based on the child's latest assessment
   */
  getRecommendedCenters(
    childId: number
  ): Observable<any[]> {

    return this.http.get<any[]>(
      `${this.API_URL}/children/${childId}/centers/recommended`
    );

  }

}
