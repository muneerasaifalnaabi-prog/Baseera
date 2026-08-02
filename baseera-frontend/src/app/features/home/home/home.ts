import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  fullName = '';

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName') ?? 'there';
  }
}