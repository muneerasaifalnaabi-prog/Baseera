import { Routes } from '@angular/router';
import { Register } from './features/auth/register/register';
import { Login } from './features/auth/login/login';
import { OauthCallback } from './features/auth/oauth-callback/oauth-callback';
import { Home } from './features/home/home/home';
import { Activities } from './features/activities/activities/activities';
import { CenterComponent } from './features/center/center';
import { SelectChild } from './features/select-child/select-child/select-child';
import { Layout } from './shared/components/layout/layout';
import { authGuard } from './shared/guards/auth-guard';

export const routes: Routes = [
  // Public — no sidebar, no login required
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'oauth-callback', component: OauthCallback },

  // Authenticated section — Layout wraps every child route with the
  // sidebar automatically. authGuard on the PARENT route protects the
  // whole section in one place, not repeated on every individual page.
  {
    path: '',
    component: Layout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: Home },
      { path: 'select-child', component: SelectChild },
      { path: 'activities', component: Activities },
      { path: 'centers', component: CenterComponent },
      {
        path: 'children/:childId/vault',
        loadComponent: () =>
          import('./features/vault/components/child-vault/child-vault')
            .then(m => m.ChildVaultComponent)
      }
    ]
  }
];
