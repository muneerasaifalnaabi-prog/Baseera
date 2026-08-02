import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import * as L from 'leaflet';

import { Center } from './models/center.model';
import { CenterService } from './services/center.service';

delete (L.Icon.Default.prototype as any)._getIconUrl;

L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
  iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
  shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png'
});
@Component({
  selector: 'app-center',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './center.html',
  styleUrl: './center.css'
})
export class CenterComponent implements OnInit, AfterViewInit {

  private centerService = inject(CenterService);

  centers: Center[] = [];

  private map!: L.Map;

  ngOnInit(): void {

    this.centerService.getCenters().subscribe({
      next: (data) => {
        this.centers = data;

        if (this.map) {
          this.addMarkers();
        }
      },
      error: (err) => {
        console.error(err);
      }
    });

  }

  ngAfterViewInit(): void {

    this.map = L.map('map').setView([23.5880, 58.3829], 7);

    L.tileLayer(
      'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      {
        attribution: '&copy; OpenStreetMap contributors'
      }
    ).addTo(this.map);

  
    setTimeout(() => {
      this.map.invalidateSize();
    }, 100);

    if (this.centers.length > 0) {
      this.addMarkers();
    }

  }

  addMarkers(): void {

    this.centers.forEach(center => {

      L.marker([center.latitude, center.longitude])
        .addTo(this.map)
        .bindPopup(`
          <b>${center.name}</b><br>
          ${center.city}<br>
          ${center.phone}
        `);

    });

  }

  openMap(center: Center): void {

    const url = `https://www.google.com/maps?q=${center.latitude},${center.longitude}`;

    window.open(url, '_blank');

  }

}