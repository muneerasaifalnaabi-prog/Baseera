import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Navbar } from '../navbar/navbar';
import { Footer } from '../footer/footer';

/** Navbar only — used exclusively for /home. */
@Component({
  selector: 'app-layout',
  standalone: true,
 imports: [RouterOutlet, Navbar, Footer],
  templateUrl: './layout.html',
  styleUrl: './layout.css'
})
export class Layout {}