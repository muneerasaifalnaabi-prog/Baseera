import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  fullName: string;
  email: string;
  role: 'PARENT' | 'ADMIN';
}

export interface RegisterPayload {
  fullName: string;
  email: string;
  password: string;
}
export interface LoginPayload {
  email: string;
  password: string;
}

@Injectable({
  providedIn: 'root'
})
export class Auth {
  private readonly baseUrl = 'http://localhost:8080/api/auth';

  constructor(private http: HttpClient) {}

  register(payload: RegisterPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/register`, payload)
      .pipe(tap((res) => this.storeSession(res)));
  }

  login(payload: LoginPayload): Observable<AuthResponse> {
    return this.http
      .post<AuthResponse>(`${this.baseUrl}/login`, payload)
      .pipe(tap((res) => this.storeSession(res)));
  }

  logout(): void {
    const refreshToken = localStorage.getItem('refreshToken');
    this.http.post(`${this.baseUrl}/logout`, { refreshToken }).subscribe({
      next: () => this.clearSession(),
      error: () => this.clearSession() // clear locally even if the server call fails
    });
  }

  private storeSession(res: AuthResponse): void {
    localStorage.setItem('token', res.accessToken);
    localStorage.setItem('refreshToken', res.refreshToken);
    localStorage.setItem('fullName', res.fullName);
    localStorage.setItem('role', res.role);
  }

  private clearSession(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('fullName');
    localStorage.removeItem('role');
    localStorage.removeItem('selectedChildId');
    window.location.href = '/login';
  }
}