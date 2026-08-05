import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ActivityItem } from './activity';

// ===== Centers =====
export interface CenterItem {
  id: number;
  name: string;
  city: string;
  specialty: 'ASD' | 'ADHD' | 'BOTH';
  phone: string;
  latitude: number;
  longitude: number;
}

// ===== Payloads sent when creating/editing (no id — the backend assigns that) =====
export interface ActivityPayload {
  name: string;
  description: string;
  minAge: number;
  maxAge: number;
  targetCondition: 'ASD' | 'ADHD' | 'BOTH';
}

export interface CenterPayload {
  name: string;
  city: string;
  specialty: 'ASD' | 'ADHD' | 'BOTH';
  phone: string;
  latitude: number;
  longitude: number;
}

// ===== Accounts (admin view) =====
export interface AccountItem {
  id: number;
  email: string;
  fullName: string;
  role: 'PARENT' | 'ADMIN';
  isActive: boolean;
}

// ===== Dashboard stats =====
export interface AdminStats {
  activeAccounts: number;
  deactivatedAccounts: number;
  registrationTrend: [string, number][];
}

@Injectable({
  providedIn: 'root'
})
export class Admin {
  private readonly activityUrl = 'http://localhost:8080/api/admin/activities';
  private readonly centerUrl = 'http://localhost:8080/api/admin/centers';
  private readonly publicCenterUrl = 'http://localhost:8080/api/centers';
  private readonly accountUrl = 'http://localhost:8080/api/admin/accounts';
  private readonly statsUrl = 'http://localhost:8080/api/admin/stats';

  // The interceptor attaches the admin's JWT to every one of these
  // requests automatically — nothing here needs to handle auth headers itself.
  constructor(private http: HttpClient) {}

  // ===== Activities — full CRUD =====
  createActivity(payload: ActivityPayload): Observable<ActivityItem> {
    return this.http.post<ActivityItem>(this.activityUrl, payload);
  }

  updateActivity(id: number, payload: ActivityPayload): Observable<ActivityItem> {
    return this.http.put<ActivityItem>(`${this.activityUrl}/${id}`, payload);
  }

  deleteActivity(id: number): Observable<string> {
    return this.http.delete(`${this.activityUrl}/${id}`, { responseType: 'text' });
  }

  // ===== Centers — full CRUD =====
  getAllCenters(): Observable<CenterItem[]> {
    return this.http.get<CenterItem[]>(this.publicCenterUrl);
  }

  createCenter(payload: CenterPayload): Observable<CenterItem> {
    return this.http.post<CenterItem>(this.centerUrl, payload);
  }

  updateCenter(id: number, payload: CenterPayload): Observable<CenterItem> {
    return this.http.put<CenterItem>(`${this.centerUrl}/${id}`, payload);
  }

  deleteCenter(id: number): Observable<string> {
    return this.http.delete(`${this.centerUrl}/${id}`, { responseType: 'text' });
  }

  // ===== Accounts — view + deactivate/reactivate. Deliberately no
  // email/password editing here, that stays the parent's own to manage. =====
  getAllAccounts(): Observable<AccountItem[]> {
    return this.http.get<AccountItem[]>(this.accountUrl);
  }

  // Backend returns a plain string ("DEACTIVATED"/"REACTIVATED"), not
  // JSON — { responseType: 'text' } tells Angular not to parse it as JSON.
  deactivateAccount(id: number): Observable<string> {
    return this.http.put(`${this.accountUrl}/${id}/deactivate`, {}, { responseType: 'text' });
  }

  reactivateAccount(id: number): Observable<string> {
    return this.http.put(`${this.accountUrl}/${id}/reactivate`, {}, { responseType: 'text' });
  }

  // ===== Dashboard stats — real counts + real registration trend =====
  getStats(): Observable<AdminStats> {
    return this.http.get<AdminStats>(this.statsUrl);
  }
}