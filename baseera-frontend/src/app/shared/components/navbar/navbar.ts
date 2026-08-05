import { Component, OnInit, EventEmitter, Input, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive } from '@angular/router';
import { Language } from '../../services/language';
import { translations } from '../../i18n/translations';
import { Auth } from '../../services/auth';

@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class Navbar implements OnInit {
  @Input() showSidebarToggle = false;
  @Output() sidebarToggled = new EventEmitter<void>();
  t = translations.sidebar;
  menuOpen = signal(false);
  accountMenuOpen = signal(false);

  isAdmin = signal(false);

  constructor(public lang: Language, private authService: Auth) {}

  ngOnInit(): void {
    // Same check the Sidebar uses — the Admin link only appears based
    // on the real role stored at login, a parent never sees it regardless
    // of URL guessing, since adminGuard blocks the route itself too.
    this.isAdmin.set(localStorage.getItem('role') === 'ADMIN');
  }

  toggleMenu(): void {
    this.menuOpen.set(!this.menuOpen());
  }

  closeMenu(): void {
    this.menuOpen.set(false);
  }

  toggleAccountMenu(): void {
    this.accountMenuOpen.set(!this.accountMenuOpen());
  }

  closeAccountMenu(): void {
    this.accountMenuOpen.set(false);
  }

  logout(): void {
    this.authService.logout();
  }

  vaultLink(): string[] {
    const childId = localStorage.getItem('selectedChildId');
    return childId ? ['/children', childId, 'vault'] : ['/select-child'];
  }
}