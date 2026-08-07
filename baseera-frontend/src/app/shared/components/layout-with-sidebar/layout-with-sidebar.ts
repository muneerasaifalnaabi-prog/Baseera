import { Component, HostListener, OnDestroy, OnInit, signal } from '@angular/core';
import { RouterOutlet, Router, NavigationStart } from '@angular/router';
import { Subscription, filter } from 'rxjs';
import { Navbar } from '../navbar/navbar';
import { Sidebar } from '../sidebar/sidebar';
import { Footer } from '../footer/footer';

/**
 * The sidebar is an off-canvas drawer, not a permanent column — it stays
 * closed on every page until the burger button in the navbar is pressed,
 * and closes again on backdrop click, Escape, or navigating anywhere from
 * inside it. Nothing here changes what any page renders in <router-outlet>.
 */
@Component({
  selector: 'app-layout-with-sidebar',
  standalone: true,
  imports: [RouterOutlet, Navbar, Sidebar, Footer],
  templateUrl: './layout-with-sidebar.html',
  styleUrl: './layout-with-sidebar.css'
})
export class LayoutWithSidebar implements OnInit, OnDestroy {
  sidebarOpen = signal(false);
  private routerSub?: Subscription;

  constructor(private router: Router) {}

  ngOnInit(): void {
    // Any navigation (including a route the sidebar itself didn't trigger,
    // e.g. a browser back/forward) closes the drawer, so it never lingers
    // open over a page the user didn't open it for.
    this.routerSub = this.router.events
      .pipe(filter((e): e is NavigationStart => e instanceof NavigationStart))
      .subscribe(() => this.sidebarOpen.set(false));
  }

  ngOnDestroy(): void {
    this.routerSub?.unsubscribe();
  }

  toggleSidebar(): void {
    this.sidebarOpen.set(!this.sidebarOpen());
  }

  closeSidebar(): void {
    this.sidebarOpen.set(false);
  }

  @HostListener('document:keydown.escape')
  onEscape(): void {
    if (this.sidebarOpen()) {
      this.closeSidebar();
    }
  }
}