import { HttpInterceptorFn } from '@angular/common/http';

/**
 * Attaches the stored JWT to every outgoing request. Without this,
 * every authenticated endpoint (children, vault, activities-with-progress,
 * admin) fails with 401, since nothing else was sending it.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');

  if (token) {
    const cloned = req.clone({
      setHeaders: { Authorization: `Bearer ${token}` }
    });
    return next(cloned);
  }

  return next(req);
};
