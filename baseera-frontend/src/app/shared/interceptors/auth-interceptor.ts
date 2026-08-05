import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError, switchMap, throwError } from 'rxjs';

/**
 * Attaches the stored JWT to every outgoing request. On a 401 (expired
 * access token), automatically uses the refresh token to get a new one
 * and silently retries the original request — the user never sees this
 * happen. Only a genuinely expired/invalid refresh token results in a
 * real logout.
 *
 * Note: the backend's AuthResponseDTO names the access token field
 * "accessToken", not "token" — this interceptor reads that field
 * correctly and stores it under the frontend's own 'token' key in
 * localStorage, matching what the rest of the app already expects.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const http = inject(HttpClient);
  const token = localStorage.getItem('token');

  const authReq = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;

  return next(authReq).pipe(
    catchError((error: HttpErrorResponse) => {
      // Never attempt a refresh for the refresh endpoint itself, or an
      // already-expired refresh token would loop forever trying to refresh.
      if (error.status === 401 && !req.url.includes('/auth/refresh-token')) {
        const refreshToken = localStorage.getItem('refreshToken');
        if (!refreshToken) {
          return throwError(() => error);
        }

        return http.post<{ accessToken: string; refreshToken: string }>(
          'http://localhost:8080/api/auth/refresh-token',
          { refreshToken }
        ).pipe(
          switchMap((res) => {
            localStorage.setItem('token', res.accessToken);
            localStorage.setItem('refreshToken', res.refreshToken);

            const retriedReq = req.clone({
              setHeaders: { Authorization: `Bearer ${res.accessToken}` }
            });
            return next(retriedReq);
          }),
          catchError((refreshError) => {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('fullName');
            localStorage.removeItem('role');
            window.location.href = '/login';
            return throwError(() => refreshError);
          })
        );
      }

      return throwError(() => error);
    })
  );
};