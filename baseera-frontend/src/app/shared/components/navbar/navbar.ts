import { Component, EventEmitter, Input, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
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
  @Input() showSidebarToggle = false;
  @Output() sidebarToggled = new EventEmitter<void>();
  t = translations.sidebar;
  menuOpen = signal(false);

  constructor(public lang: Language) {}

  toggleMenu(): void {
    this.menuOpen.set(!this.menuOpen());
  }

  closeMenu(): void {
    this.menuOpen.set(false);
  }

  vaultLink(): string[] {
    const childId = localStorage.getItem('selectedChildId');
    return childId ? ['/children', childId, 'vault'] : ['/select-child'];
  }
}