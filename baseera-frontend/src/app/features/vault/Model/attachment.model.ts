export type DocumentType =
  | 'SPECIALIST_REPORT'
  | 'MEDICAL_RECORD'
  | 'OTHER';


export interface Attachment {

  id: number;

  childId: number;

  originalFileName: string;

  documentType: DocumentType;

  createdAt: string;
}


export interface AttachmentAnalysis {

  id: number;

  attachmentId: number;

  improvementSigns: string;

  progressSummary: string;

  suggestedGoalTags: string[];

  createdAt: string;
}
