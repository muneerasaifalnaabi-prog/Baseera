import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export type RiskLevel = 'LOW' | 'MEDIUM' | 'HIGH';
export type ConditionType = 'ASD' | 'ADHD' | 'BOTH';

export interface AssessmentItem {
  id: number;
  childId: number;
  description: string;
  riskLevel: RiskLevel;
  suggestedCondition: ConditionType;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class Assessment {
  private readonly baseUrl = 'http://localhost:8080/api/children';

  constructor(private http: HttpClient) {}

  submit(childId: number, description: string): Observable<AssessmentItem> {
    return this.http.post<AssessmentItem>(`${this.baseUrl}/${childId}/assessments`, { description });
  }

  getHistory(childId: number): Observable<AssessmentItem[]> {
    return this.http.get<AssessmentItem[]>(`${this.baseUrl}/${childId}/assessments`);
  }
}