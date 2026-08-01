import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
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

  private storeSession(res: AuthResponse): void {
    localStorage.setItem('token', res.token);
    localStorage.setItem('fullName', res.fullName);
    localStorage.setItem('role', res.role);
  }
}