import { Routes } from '@angular/router';
import { Register } from './features/auth/register/register';
import { Home } from './features/home/home/home';
import { Login } from './features/auth/login/login';
import { authGuard } from './shared/guards/auth-guard';

export const routes: Routes = [
  {
    path: 'register',
    component: Register
  },
{
  path: 'home',
  component: Home,
  canActivate: [authGuard]
},
  {
  path: 'login',
  component: Login
},{
    path: 'children/:childId/vault',

    loadComponent: () =>
      import(
        './features/vault/components/child-vault/child-vault'
        ).then(
        m => m.ChildVaultComponent
      )
  }
];
