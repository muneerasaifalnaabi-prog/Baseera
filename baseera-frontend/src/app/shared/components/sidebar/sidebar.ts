import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Language } from '../../services/language';
import { translations } from '../../i18n/translations';

/**
 * Off-canvas navigation drawer. Hidden by default on every page — it only
 * appears when the burger button in the navbar is pressed, and closes again
 * on: backdrop click, Escape, or picking a destination from inside it.
 */
@Component({
  selector: 'app-sidebar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css'
})
export class Sidebar implements OnInit {
  @Input() open = false;
  @Output() closeRequested = new EventEmitter<void>();

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

  onNavClick(): void {
    // Picking a destination closes the drawer — it should never stay
    // parked open over the page you just navigated to.
    this.closeRequested.emit();
  }

  onBackdropClick(): void {
    this.closeRequested.emit();
  }
}