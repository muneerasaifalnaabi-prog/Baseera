import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ActivityItem {
  id: number;
  name: string;
  description: string;
  minAge: number;
  maxAge: number;
  targetCondition: 'ASD' | 'ADHD' | 'BOTH';
}

@Injectable({
  providedIn: 'root'
})
export class Activity {
  private readonly baseUrl = 'http://localhost:8080/api/activities';

  constructor(private http: HttpClient) {}

  getAll(): Observable<ActivityItem[]> {
    return this.http.get<ActivityItem[]>(this.baseUrl);
  }

  searchActivities(name?: string, targetCondition?: string, age?: number): Observable<ActivityItem[]> {
    let params = new HttpParams();
    if (name) params = params.set('name', name);
    if (targetCondition) params = params.set('targetCondition', targetCondition);
    if (age !== undefined && age !== null) params = params.set('age', age.toString());

    return this.http.get<ActivityItem[]>(`${this.baseUrl}/search`, { params });
  }
}