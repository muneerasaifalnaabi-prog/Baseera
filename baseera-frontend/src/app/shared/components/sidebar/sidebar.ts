import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {
  fullName = '';
  isAdmin = signal(false);
  selectedChildId = signal<string | null>(null);

  constructor(private router: Router) {}

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName') ?? 'there';
    this.isAdmin.set(localStorage.getItem('role') === 'ADMIN');
    this.selectedChildId.set(localStorage.getItem('selectedChildId'));
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('fullName');
    localStorage.removeItem('role');
    localStorage.removeItem('selectedChildId');
    this.router.navigate(['/login']);
  }
}
