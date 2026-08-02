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

    this.centerService.getCenters().subscribe({
      next: (data) => {
        this.centers = data;
        console.log(data);
      },
      error: (err) => {
        console.error(err);
      }
    });

  }

}