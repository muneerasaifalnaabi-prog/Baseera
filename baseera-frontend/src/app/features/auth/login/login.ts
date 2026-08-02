import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../../shared/services/auth';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {
  email = '';
  password = '';
  showPassword = false;
  keepSignedIn = false;
  errorMessage = '';
  loading = false;

  lang: 'en' | 'ar' = 'en';

  constructor(private authService: Auth, private router: Router) {}

  toggleLang(): void {
    this.lang = this.lang === 'en' ? 'ar' : 'en';
  }

  submit(): void {
    this.errorMessage = '';
    this.loading = true;

    this.authService.login({
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.loading = false;
        this.router.navigate(['/home']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage = this.t[this.lang].invalidCreds;
      }
    });
  }

  t = {
    en: {
      title: 'Welcome back',
      subtitle: 'Log in to continue your child\'s journey.',
      tagline: 'Welcome back.',
      taglineSub: 'Pick up right where you left off, your child\'s progress is waiting for you.',
      joined: 'Your journey, continued',
      email: 'Email address',
      emailPlaceholder: 'name@example.com',
      password: 'Password',
      keepSignedIn: 'Keep me signed in',
      forgot: 'Forgot password?',
      signIn: 'Sign in',
      or: 'or continue with',
      google: 'Continue with Google',
      noAccount: 'Don\'t have an account?',
      signup: 'Sign up',
      langLabel: 'العربية',
      invalidCreds: 'Invalid email or password.'
    },
    ar: {
      title: 'مرحباً بعودتك',
      subtitle: 'سجّل الدخول لمتابعة رحلة طفلك.',
      tagline: 'مرحباً بعودتك.',
      taglineSub: 'تابع من حيث توقفت، تقدّم طفلك بانتظارك.',
      joined: 'رحلتك مستمرة',
      email: 'البريد الإلكتروني',
      emailPlaceholder: 'name@example.com',
      password: 'كلمة المرور',
      keepSignedIn: 'إبقني مسجلاً للدخول',
      forgot: 'نسيت كلمة المرور؟',
      signIn: 'تسجيل الدخول',
      or: 'أو تابع باستخدام',
      google: 'المتابعة باستخدام Google',
      noAccount: 'ليس لديك حساب؟',
      signup: 'إنشاء حساب',
      langLabel: 'English',
      invalidCreds: 'البريد الإلكتروني أو كلمة المرور غير صحيحة.'
    }
  };
}