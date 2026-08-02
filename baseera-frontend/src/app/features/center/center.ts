import * as L from 'leaflet';
import { AfterViewInit } from '@angular/core';
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { CenterService } from './services/center.service';
import { Center } from './models/center.model';

@Component({
  selector: 'app-center',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
  ],
  templateUrl: './center.html',
  styleUrl: './center.css'
})
export class CenterComponent implements OnInit, AfterViewInit {
  centers: Center[] = [];

  filteredCenters: Center[] = [];

  selectedCenter?: Center;

  searchText = '';

  selectedSpecialty = 'ALL';
  private map!: L.Map;
  ngAfterViewInit(): void {
  this.initMap();
}

private initMap(): void {
   console.log('Map initialization started');

  this.map = L.map('map').setView([20.5, 57.5], 6);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors'
  }).addTo(this.map);

}

  loading = false;


  constructor(private centerService: CenterService) {}

  ngOnInit(): void {
    this.loadCenters();
  }

  loadCenters(): void {

    this.loading = true;

    this.centerService.getCenters().subscribe({
      next: (data) => {

  this.centers = data;
  this.filteredCenters = data;

  data.forEach(center => {

    L.marker([center.latitude, center.longitude])
      .addTo(this.map)
      .bindPopup(`
        <b>${center.name}</b><br>
        ${center.city}<br>
        ${center.specialty}
      `);

  });

  this.loading = false;

},

    
      error: (err) => {

        console.error(err);
        this.loading = false;

      }

    });

  }

  filterCenters(): void {

    this.filteredCenters = this.centers.filter(center => {

      const matchesSearch =
        center.name.toLowerCase().includes(this.searchText.toLowerCase()) ||
        center.city.toLowerCase().includes(this.searchText.toLowerCase());

      const matchesSpecialty =
        this.selectedSpecialty === 'ALL' ||
        center.specialty === this.selectedSpecialty;

      return matchesSearch && matchesSpecialty;

    });

  }
  selectCenter(center: Center): void {

  this.selectedCenter = center;
   this.map.setView(
    [center.latitude, center.longitude],
    12
  );

}

  openDirections(center: Center): void {

    window.open(
      `https://www.google.com/maps/dir/?api=1&destination=${center.latitude},${center.longitude}`,
      '_blank'
    );
    

  }
getImage(center: Center): string {

  switch (center.city.toLowerCase()) {

    case 'muscat':
      return 'center/muscat.jpg';

    case 'sohar':
      return 'center/sohar.jpg';

    case 'salalah':
      return 'center/salalah.jpg';

    case 'sur':
      return 'center/sur.jpg';

    default:
      return 'center/muscat.jpg';
  }

}

}