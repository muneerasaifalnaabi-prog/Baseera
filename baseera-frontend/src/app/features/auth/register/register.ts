import { Component, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../../shared/services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  fullName = '';
  email = '';
  password = '';
  showPassword = signal(false);
  errorMessage = signal('');
  loading = signal(false);

  lang = signal<'en' | 'ar'>('en');

  constructor(private authService: Auth, private router: Router) {}

  toggleLang(): void {
    this.lang.set(this.lang() === 'en' ? 'ar' : 'en');
  }

  submit(): void {
    this.errorMessage.set('');
    this.loading.set(true);

    this.authService.register({
      fullName: this.fullName,
      email: this.email,
      password: this.password
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.router.navigate(['/home']);
      },
      error: (err: HttpErrorResponse) => {
        this.loading.set(false);

        if (err.status === 409) {
          this.errorMessage.set(this.lang() === 'ar'
            ? 'يوجد حساب بهذا البريد الإلكتروني بالفعل.'
            : 'An account with this email already exists.');
        } else if (err.status === 400) {
          this.errorMessage.set(this.lang() === 'ar'
            ? 'يرجى التأكد من صحة جميع الحقول.'
            : 'Please check that all fields are valid.');
        } else {
          this.errorMessage.set(this.lang() === 'ar'
            ? 'حدث خطأ ما. يرجى المحاولة مرة أخرى.'
            : 'Something went wrong. Please try again.');
        }
      }
    });
  }

  t = {
    en: {
      title: 'Create your account', subtitle: 'Start your journey with us today.',
      tagline: 'You noticed something. We\'re here to help.',
      taglineSub: 'A calm, guided way to understand your child\'s development, every step of the way.',
      joined: 'Guided by care, supported by AI',
      fullName: 'Full name', fullNamePlaceholder: 'Salim Al Balushi',
      email: 'Email address', emailPlaceholder: 'name@example.com',
      password: 'Password', hint: 'Must be at least 8 characters long.',
      create: 'Create account', or: 'or join with', google: 'Continue with Google',
      haveAccount: 'Already have an account?', login: 'Log in', langLabel: 'العربية'
    },
    ar: {
      title: 'إنشاء حسابك', subtitle: 'ابدأ رحلتك معنا اليوم.',
      tagline: 'لاحظت شيئاً. نحن هنا لمساعدتك.',
      taglineSub: 'طريقة هادئة وموجّهة لفهم نمو طفلك، خطوة بخطوة.',
      joined: 'برعاية حقيقية، بدعم من الذكاء الاصطناعي',
      fullName: 'الاسم الكامل', fullNamePlaceholder: 'سالم البلوشي',
      email: 'البريد الإلكتروني', emailPlaceholder: 'name@example.com',
      password: 'كلمة المرور', hint: 'يجب ألا تقل عن 8 أحرف.',
      create: 'إنشاء حساب', or: 'أو أنشئ حسابك عبر', google: 'المتابعة باستخدام Google',
      haveAccount: 'لديك حساب بالفعل؟', login: 'تسجيل الدخول', langLabel: 'English'
    }
  };
}