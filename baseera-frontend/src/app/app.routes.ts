import { Routes } from '@angular/router';
import { Register } from './features/auth/register/register';
import { Login } from './features/auth/login/login';
import { OauthCallback } from './features/auth/oauth-callback/oauth-callback';
import { Home } from './features/home/home/home';
import { Activities } from './features/activities/activities/activities';
import { CenterComponent } from './features/center/center';
import { SelectChild } from './features/select-child/select-child/select-child';
import { AssessmentPage } from './features/assessment/assessment/assessment';
import { AdminDashboard } from './features/admin/admin/admin';
import { Layout } from './shared/components/layout/layout';
import { LayoutWithSidebar } from './shared/components/layout-with-sidebar/layout-with-sidebar';
import { authGuard } from './shared/guards/auth-guard';
import { adminGuard } from './shared/guards/admin-guard';

export const routes: Routes = [
  { path: 'register', component: Register },
  { path: 'login', component: Login },
  { path: 'oauth-callback', component: OauthCallback },

  // Navbar only
  {
    path: '',
    component: Layout,
    canActivate: [authGuard],
    children: [
      { path: '', redirectTo: 'home', pathMatch: 'full' },
      { path: 'home', component: Home }
    ]
  },

  // Navbar + sidebar — everything except Home
  {
    path: '',
    component: LayoutWithSidebar,
    canActivate: [authGuard],
    children: [
      { path: 'select-child', component: SelectChild },
      { path: 'assessment', component: AssessmentPage },
      { path: 'activities', component: Activities },
      { path: 'centers', component: CenterComponent },
      {
        path: 'children/:childId/vault',
        loadComponent: () =>
          import('./features/vault/components/child-vault/child-vault')
            .then(m => m.ChildVaultComponent)
      },
      { path: 'admin', component: AdminDashboard, canActivate: [adminGuard] }
    ]
  }
];