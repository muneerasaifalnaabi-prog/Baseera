import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type ProgressStatus = 'NOT_STARTED' | 'IN_PROGRESS' | 'COMPLETED';

export interface ProgressItem {
  id: number;
  activityId: number;
  activityName: string;
  activityDescription: string;
  status: ProgressStatus;
  notes: string | null;
  addedAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class Progress {
  private readonly baseUrl = 'http://localhost:8080/api/children';

  constructor(private http: HttpClient) {}

  addActivityToChild(childId: number, activityId: number): Observable<ProgressItem> {
    return this.http.post<ProgressItem>(`${this.baseUrl}/${childId}/activities/${activityId}`, {});
  }

  getActivitiesForChild(childId: number): Observable<ProgressItem[]> {
    return this.http.get<ProgressItem[]>(`${this.baseUrl}/${childId}/activities`);
  }

  updateStatus(childId: number, activityId: number, status: ProgressStatus, notes?: string): Observable<ProgressItem> {
    return this.http.put<ProgressItem>(`${this.baseUrl}/${childId}/activities/${activityId}`, { status, notes });
  }

  removeActivity(childId: number, activityId: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${childId}/activities/${activityId}`, { responseType: 'text' });
  }
}