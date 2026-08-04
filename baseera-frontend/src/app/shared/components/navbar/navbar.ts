import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { Language } from '../../services/language';
import { translations } from '../../i18n/translations';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar {
  // Reuses the existing sidebar copy (dashboard/activities/centers/
  // logout are already there) plus a few additions — see
  // translations-additions.ts.
  t = translations.sidebar;

  menuOpen = false;

  constructor(public lang: Language, private router: Router) {}

  get fullName(): string {
    return localStorage.getItem('fullName') ?? '';
  }

  get initials(): string {
    const name = this.fullName.trim();
    if (!name) return '?';
    const parts = name.split(/\s+/);
    return parts.length > 1
      ? (parts[0][0] + parts[1][0]).toUpperCase()
      : name.slice(0, 2).toUpperCase();
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }

  closeMenu(): void {
    this.menuOpen = false;
  }

  // NOTE: adjust to match the real Language service API — assuming a
  // `set(lang)` method exists alongside the existing `current()`.
  switchLanguage(): void {
    const next = this.lang.current() === 'en' ? 'ar' : 'en';
    (this.lang as any).set ? (this.lang as any).set(next) : null;
  }

  // NOTE: wire to the real auth service when available. Placeholder
  // clears local session state and returns to login.
  logout(): void {
    localStorage.removeItem('fullName');
    localStorage.removeItem('selectedChildId');
    this.router.navigateByUrl('/login');
  }
}