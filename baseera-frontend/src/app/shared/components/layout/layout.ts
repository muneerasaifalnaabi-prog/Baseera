import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Sidebar } from '../sidebar/sidebar';

/**
 * The shared shell — sidebar on one side, whatever page is currently
 * routed to on the other, via <router-outlet>. This is what makes the
 * nav bar appear identically on every authenticated page without
 * copy-pasting the sidebar into each one individually.
 */
@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [RouterOutlet, Sidebar],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {}
