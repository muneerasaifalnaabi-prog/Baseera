import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Language } from '../../services/language';
import { translations } from '../../i18n/translations';

@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {
  t = translations.sidebar;
  isAdmin = signal(false);
  selectedChildId = signal<string | null>(null);

  constructor(public lang: Language) {}

  ngOnInit(): void {
    // The Admin Dashboard link only appears here, based on the actual
    // role stored at login — a parent account never sees it, regardless
    // of URL guessing, since adminGuard blocks the route itself too.
    this.isAdmin.set(localStorage.getItem('role') === 'ADMIN');
    this.selectedChildId.set(localStorage.getItem('selectedChildId'));
  }

  vaultLink(): string[] {
    const id = this.selectedChildId();
    return id ? ['/children', id, 'vault'] : ['/select-child'];
  }
}