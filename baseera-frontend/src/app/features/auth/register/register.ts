import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { Auth } from '../../../shared/services/auth';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule,CommonModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class Register {
  fullName = '';
  email = '';
  password = '';
  errorMessage = '';
  loading = false;

  constructor(private authService: Auth, private router: Router) {}

  submit(): void {
    this.errorMessage = '';
    this.loading = true;

    this.authService.register({
      fullName: this.fullName,
      email: this.email,
      password: this.password
    }).subscribe({
     next: () => {
  this.loading = false;
  this.router.navigate(['/home']);
},
      error: (err: HttpErrorResponse) => {
        this.loading = false;
        this.errorMessage = err.status === 409
          ? 'An account with this email already exists.'
          : 'Something went wrong. Please try again.';
      }
    });
  }
}