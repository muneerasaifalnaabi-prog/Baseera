import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { Language } from '../../services/language';
import { translations } from '../../i18n/translations';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './footer.html',
  styleUrl: './footer.css'
})
export class Footer {
  t = translations.footer;

  email = '';
  subscribed = signal(false);
  currentYear = new Date().getFullYear();

  constructor(public lang: Language) {}

  subscribe(): void {
    if (!this.email.includes('@')) return;
    // No real newsletter endpoint exists yet — this is honestly just a
    // visual confirmation for now, not wired to a backend.
    this.subscribed.set(true);
    this.email = '';
  }

  scrollToTop(): void {
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}