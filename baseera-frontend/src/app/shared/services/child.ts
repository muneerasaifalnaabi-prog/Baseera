import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChildItem {
  id: number;
  fullName: string;
  dateOfBirth: string;
  age: number;
  gender: string;
}

export interface ChildPayload {
  fullName: string;
  dateOfBirth: string; // 'YYYY-MM-DD' — matches backend's LocalDate parsing
  gender: string;
}

@Injectable({
  providedIn: 'root'
})
export class Child {
  private readonly baseUrl = 'http://localhost:8080/api/children';

  constructor(private http: HttpClient) {}

  getMyChildren(): Observable<ChildItem[]> {
    return this.http.get<ChildItem[]>(this.baseUrl);
  }

  createChild(payload: ChildPayload): Observable<ChildItem> {
    return this.http.post<ChildItem>(this.baseUrl, payload);
  }

  deleteChild(childId: number): Observable<string> {
    return this.http.delete(`${this.baseUrl}/${childId}`, { responseType: 'text' });
  }
}
