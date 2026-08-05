import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Sidebar } from '../sidebar/sidebar';

@Component({
  selector: 'app-layout-with-sidebar',
  standalone: true,
  imports: [RouterOutlet, Navbar, Sidebar],
  templateUrl: './layout-with-sidebar.html',
  styleUrl: './layout-with-sidebar.css'
})
export class LayoutWithSidebar {
  sidebarOpen = signal(true);

  toggleSidebar(): void {
    this.sidebarOpen.set(!this.sidebarOpen());
  }
}