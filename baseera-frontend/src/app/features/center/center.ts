import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';

import { Center } from './models/center.model';
import { CenterService } from './services/center.service';

@Component({
  selector: 'app-center',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './center.html',
  styleUrl: './center.css'
})
export class CenterComponent implements OnInit {

  private centerService = inject(CenterService);

  centers: Center[] = [];

  ngOnInit(): void {

    console.log("Center Component Loaded");

    this.centerService.getCenters().subscribe({
      next: (data) => {
        console.log("DATA:", data);
        this.centers = data;
        console.log("Centers array:", this.centers);
        console.log("Length:", this.centers.length);
      },
      error: (err) => {
        console.error("ERROR:", err);
      }
    });

  }

}