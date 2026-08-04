import {
  Child,
  ChildItem
} from '../../../../shared/services/child';

import {
  Component,
  OnInit,
  inject
} from '@angular/core';

import {
  CommonModule
} from '@angular/common';

import {
  FormsModule
} from '@angular/forms';

import {
  ActivatedRoute
} from '@angular/router';

import {
  AttachmentService
} from '../../service/attachment.service';

import {
  Attachment,
  AttachmentAnalysis,
  DocumentType
} from '../../Model/attachment.model';


@Component({
  selector: 'app-child-vault',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule
  ],

  templateUrl: './child-vault.html',

  styleUrl: './child-vault.css'
})
export class ChildVaultComponent
  implements OnInit {


  private readonly route =
    inject(ActivatedRoute);

  private readonly attachmentService =
    inject(AttachmentService);

  private readonly childService =
    inject(Child);

// =========================
// CHILD
// =========================

  childId!: number;
  childName = '';
  childGender = '';


// =========================
// DOCUMENTS
// =========================

  attachments: Attachment[] = [];

  filteredAttachments: Attachment[] = [];


// =========================
// UI STATES
// =========================

  loading = false;

  uploading = false;

  deletingId: number | null = null;

  analyzingId: number | null = null;


// =========================
// UPLOAD
// =========================

  selectedFile: File | null = null;

  selectedDocumentType: DocumentType =
    'SPECIALIST_REPORT';


// =========================
// SEARCH / FILTER
// =========================

  searchTerm = '';

  selectedFilter:
    | 'ALL'
    | DocumentType = 'ALL';


// =========================
// MESSAGES
// =========================

  errorMessage = '';

  successMessage = '';


// =========================
// AI
// =========================

  selectedAnalysis:
    | AttachmentAnalysis
    | null = null;


// =========================
// DOCUMENT TYPES
// =========================

  documentTypes: {
    value: DocumentType;
    label: string;
  }[] = [

    {
      value: 'SPECIALIST_REPORT',
      label: 'Specialist Report'
    },

    {
      value: 'MEDICAL_RECORD',
      label: 'Medical Record'
    },

    {
      value: 'OTHER',
      label: 'Other'
    }

  ];


// =========================
// INIT
// =========================

  ngOnInit(): void {

    this.route.paramMap.subscribe(
      params => {

        const id =
          params.get('childId');

        if (!id) {
          return;
        }

        this.childId =
          Number(id);

        this.loadChildName();

        this.loadAttachments();

      }
    );
  }


// =========================
// LOAD
// =========================

  loadChildName(): void {

    this.childService
      .getMyChildren()
      .subscribe({

        next: (children: ChildItem[]) => {

          const child =
            children.find(
              child => child.id === this.childId
            );

          if (child) {

            this.childName =
              child.fullName;

            this.childGender =
              child.gender;

          }

        },

        error: error => {

          console.error(
            'Failed to load child:',
            error
          );

        }

      });
  }


  loadAttachments(): void {

    if (!this.childId) {
      return;
    }

    this.loading = true;

    this.errorMessage = '';


    this.attachmentService
      .getAttachments(this.childId)
      .subscribe({

        next: attachments => {

          this.attachments =
            attachments ?? [];

          this.applyFilters();

          this.loading = false;

        },

        error: error => {

          console.error(
            'Failed to load attachments:',
            error
          );

          this.loading = false;

          this.errorMessage =
            'Unable to load documents. Please try again.';

        }

      });
  }


// =========================
// FILE SELECTION
// =========================

  onFileSelected(
    event: Event
  ): void {

    const input =
      event.target as HTMLInputElement;


    if (
      !input.files ||
      input.files.length === 0
    ) {
      return;
    }


    this.setSelectedFile(
      input.files[0]
    );
  }


// =========================
// DRAG OVER
// =========================

  onDragOver(
    event: DragEvent
  ): void {

    event.preventDefault();

    event.stopPropagation();
  }


// =========================
// DROP
// =========================

  onDrop(
    event: DragEvent
  ): void {

    event.preventDefault();

    event.stopPropagation();


    if (
      !event.dataTransfer ||
      event.dataTransfer.files.length === 0
    ) {
      return;
    }


    this.setSelectedFile(
      event.dataTransfer.files[0]
    );
  }


// =========================
// SET FILE
// =========================

  private setSelectedFile(
    file: File
  ): void {

    this.errorMessage = '';

    this.successMessage = '';


// 10 MB
    const maxSize =
      10 * 1024 * 1024;


    if (file.size > maxSize) {

      this.errorMessage =
        'File size must be less than 10 MB.';

      return;
    }


    const allowedTypes = [

      'application/pdf',

      'image/png',

      'image/jpeg',

      'image/jpg',

      'application/msword',

      'application/vnd.openxmlformats-officedocument.wordprocessingml.document'

    ];


    if (
      !allowedTypes.includes(file.type)
    ) {

      this.errorMessage =
        'Please upload PDF, JPG, PNG or Word files.';

      return;
    }


    this.selectedFile = file;
  }


// =========================
// REMOVE SELECTED FILE
// =========================

  removeSelectedFile(): void {

    this.selectedFile = null;
  }


// =========================
// UPLOAD
// =========================

  uploadFile(): void {

    if (!this.selectedFile) {

      this.errorMessage =
        'Please select a file first.';

      return;
    }


    if (!this.childId) {

      this.errorMessage =
        'Child ID is missing.';

      return;
    }


    this.uploading = true;

    this.errorMessage = '';

    this.successMessage = '';


    this.attachmentService
      .uploadAttachment(
        this.childId,
        this.selectedDocumentType,
        this.selectedFile
      )
      .subscribe({

        next: attachment => {

          console.log(
            'Uploaded attachment:',
            attachment
          );


          this.uploading = false;

          this.selectedFile = null;


          this.successMessage =
            'Document uploaded successfully.';


          this.loadAttachments();

        },


        error: error => {

          console.error(
            'Upload failed:',
            error
          );


          this.uploading = false;


          this.errorMessage =
            error?.error?.message ??
            'Upload failed. Please try again.';
        }

      });
  }


// =========================
// SEARCH
// =========================

  onSearchChange(): void {

    this.applyFilters();
  }


// =========================
// FILTER
// =========================

  onFilterChange(
    filter: 'ALL' | DocumentType
  ): void {

    this.selectedFilter =
      filter;

    this.applyFilters();
  }


// =========================
// APPLY FILTERS
// =========================

  applyFilters(): void {

    let result =
      [...this.attachments];


// Document type
    if (
      this.selectedFilter !== 'ALL'
    ) {

      result =
        result.filter(
          attachment =>
            attachment.documentType ===
            this.selectedFilter
        );
    }


// Search
    const search =
      this.searchTerm
        .trim()
        .toLowerCase();


    if (search) {

      result =
        result.filter(
          attachment =>
            attachment.originalFileName
              .toLowerCase()
              .includes(search)
        );
    }


    this.filteredAttachments =
      result;
  }


// =========================
// DELETE
// =========================

  deleteAttachment(
    attachment: Attachment
  ): void {

    const confirmed =
      confirm(
        `Are you sure you want to delete "${attachment.originalFileName}"?`
      );


    if (!confirmed) {
      return;
    }


    this.deletingId =
      attachment.id;


    this.attachmentService
      .deleteAttachment(
        attachment.id
      )
      .subscribe({

        next: () => {

          this.deletingId = null;

          this.successMessage =
            'Document deleted successfully.';

          this.loadAttachments();

        },

        error: error => {

          console.error(
            error
          );

          this.deletingId = null;

          this.errorMessage =
            'Unable to delete document.';
        }

      });
  }


// =========================
// AI ANALYSIS
// =========================

  analyzeAttachment(
    attachment: Attachment
  ): void {

    this.analyzingId =
      attachment.id;

    this.errorMessage = '';


    this.attachmentService
      .analyzeAttachment(
        attachment.id
      )
      .subscribe({

        next: analysis => {

          this.analyzingId =
            null;

          this.selectedAnalysis =
            analysis;

        },

        error: error => {

          console.error(
            'AI analysis failed:',
            error
          );

          this.analyzingId =
            null;

          this.errorMessage =
            error?.error?.message ??
            'AI analysis failed. Please try again.';
        }

      });
  }


// =========================
// CLOSE AI MODAL
// =========================

  closeAnalysis(): void {

    this.selectedAnalysis = null;
  }


// =========================
// DOCUMENT TYPE LABEL
// =========================

  getDocumentTypeLabel(
    type: DocumentType
  ): string {

    switch (type) {

      case 'SPECIALIST_REPORT':
        return 'Specialist Report';

      case 'MEDICAL_RECORD':
        return 'Medical Record';

      case 'OTHER':
        return 'Other';

      default:
        return type;
    }
  }


// =========================
// FILE EXTENSION
// =========================

  getFileExtension(
    fileName: string
  ): string {

    const parts =
      fileName.split('.');


    if (parts.length < 2) {
      return 'FILE';
    }


    return parts
      .pop()!
      .toUpperCase();
  }


// =========================
// DATE
// =========================

  formatDate(
    date: string
  ): string {

    return new Intl.DateTimeFormat(
      'en-US',
      {
        month: 'short',
        day: 'numeric',
        year: 'numeric'
      }
    ).format(
      new Date(date)
    );
  }

}


