import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

/**
 * Stricter than authGuard — requires a valid token AND the ADMIN role.
 * A logged-in parent hitting an admin route gets redirected to /home,
 * not /login (they ARE authenticated, just not authorized for this page).
 */
export const adminGuard: CanActivateFn = () => {
  const router = inject(Router);
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (token && role === 'ADMIN') {
    return true;
  }

  router.navigate(token ? ['/home'] : ['/login']);
  return false;
};