import * as L from 'leaflet';
import { Component, OnInit, AfterViewInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
 
import { CenterService } from './services/center.service';
import { Center } from './models/center.model';
import { Sidebar } from '../../shared/components/sidebar/sidebar';
// ==========================================
// Fix Leaflet default marker icons
// Leaflet cannot locate marker images automatically
// in Angular, so we set the correct paths manually.
// ==========================================
delete (L.Icon.Default.prototype as any)._getIconUrl;
 
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'assets/leaflet/marker-icon-2x.png',
  iconUrl: 'assets/leaflet/marker-icon.png',
  shadowUrl: 'assets/leaflet/marker-shadow.png'
});
// ==========================================
// Component
// ==========================================
 
@Component({
  selector: 'app-center',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    Sidebar
  ],
  templateUrl: './center.html',
  styleUrl: './center.css'
})
export class CenterComponent implements OnInit, AfterViewInit {
 
  // ==========================================
  // Properties
  // ==========================================
 
  // All centers from backend
  centers: Center[] = [];
 
  // Centers after filtering
  filteredCenters: Center[] = [];
 
  // Selected center
  selectedCenter?: Center;
  // Search & Filter
  searchText = '';
  selectedSpecialty = 'ALL';
 
  // Loading state
  loading = false;
 
  // Leaflet Map
  private map!: L.Map;
 
  // Store all markers
  private markers: L.Marker[] = [];
 
  // User Coordinates
  userLat!: number;
  userLng!: number;
 
  // ==========================================
  // Constructor
  // ==========================================
 
  constructor(
    private centerService: CenterService
  ) {}
 
  // ==========================================
  // Lifecycle Hooks
  // ==========================================
 
  ngOnInit(): void {
 
    // Load centers from backend
 
    // this.loadCenters();
 
  }
  ngAfterViewInit(): void {
 
  this.initMap();
 
  this.loadCenters();
 
  this.getUserLocation();
 
}
 
  // ==========================================
  // Map Functions
  // ==========================================
 
  private initMap(): void {
 
    this.map = L.map('map').setView([20.5,57.5],6);
 
    // L.tileLayer(
 
    //   'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
 
    //   {
 
    //     attribution:'© OpenStreetMap Contributors'
 
    //   }
 
    // ).addTo(this.map);
    L.tileLayer(
      'https://{s}.basemaps.cartocdn.com/light_all/{z}/{x}/{y}{r}.png',
    {
      attribution: '&copy; OpenStreetMap &copy; CARTO'
      }
      ).addTo(this.map);
 
  }
 
  //////////////////////////////////////////////////
 
  private getUserLocation(): void {
 
    if(!navigator.geolocation) return;
 
    navigator.geolocation.getCurrentPosition(position=>{
 
      this.userLat=position.coords.latitude;
 
      this.userLng=position.coords.longitude;
 
      // User Location
 
      L.circleMarker(
 
        [this.userLat,this.userLng],
 
        {
 
          radius:8,
 
          color:'#2563eb',
 
          fillColor:'#3b82f6',
 
          fillOpacity:1,
 
          weight:3
 
        }
 
      )
 
      .addTo(this.map)
 
      .bindPopup('📍 Your Location')
 
      .openPopup();
 
      // Accuracy Circle
 
      L.circle(
 
        [this.userLat,this.userLng],
 
        {
 
          radius:20,
 
          color:'#60a5fa',
 
          fillColor:'#60a5fa',
 
          fillOpacity:0.15,
 
          weight:1
 
        }
 
      ).addTo(this.map);
 
      this.map.setView(
 
        [this.userLat,this.userLng],
 
        8
 
      );
 
      if(this.centers.length>0){
 
        this.calculateDistances();
 
      }
 
    });
 
  }
 
  // ==========================================
  // Data Functions
  // ==========================================
 
  loadCenters(): void {
 
    this.loading = true;
 
    this.centerService.getCenters().subscribe({
 
      next: (data) => {
 
        this.centers = data;
 
        this.filterCenters();
 
        // Remove old markers
 
        this.markers.forEach(marker => {
 
          this.map.removeLayer(marker);
 
        });
 
        this.markers = [];
        this.centers.forEach(center => {
 
  const marker = L.marker([center.latitude, center.longitude])
    .addTo(this.map)
    .bindPopup(`
      <b>${center.name}</b><br>
      📍 ${center.city}<br>
      🧩 ${center.specialty}
    `);
 
  this.markers.push(marker);
 
});
 
setTimeout(() => {
  this.map.invalidateSize();
}, 100);
 
        if (this.userLat) {
 
          this.calculateDistances();
 
        }
 
        this.loading = false;
 
      },
 
      error: (err) => {
 
        console.error(err);
 
        this.loading = false;
 
      }
 
    });
 
  }
 
  //////////////////////////////////////////////////
 
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
 
// ==========================================
// GIS Functions
// ==========================================
 
// Calculate the distance between the user's
// location and every center using the
// Haversine Formula.
 
  private calculateDistances(): void {
 
    this.centers.forEach(center => {
 
      center.distance = this.getDistance(
 
        this.userLat,
 
        this.userLng,
 
        center.latitude,
 
        center.longitude
 
      );
 
    });
 
    // Sort by nearest center
 
    this.centers.sort(
 
      (a,b)=>(a.distance ?? 0)-(b.distance ?? 0)
     
 
    );
 
 
// Update filtered list after sorting
 
this.filterCenters();
 
 
  }
 
  //////////////////////////////////////////////////
 
  private getDistance(
 
    lat1:number,
 
    lon1:number,
 
    lat2:number,
 
    lon2:number
 
  ):number{
 
    const R = 6371;
 
    const dLat = this.toRadians(lat2-lat1);
 
    const dLon = this.toRadians(lon2-lon1);
 
    const a =
 
      Math.sin(dLat/2)*Math.sin(dLat/2)+
 
      Math.cos(this.toRadians(lat1))*
 
      Math.cos(this.toRadians(lat2))*
 
      Math.sin(dLon/2)*
 
      Math.sin(dLon/2);
 
    const c =
 
      2*Math.atan2(
 
        Math.sqrt(a),
 
        Math.sqrt(1-a)
 
      );
 
    return Number((R*c).toFixed(1));
 
  }
 
// Convert degrees to radians
  private toRadians(value:number):number{
 
    return value * Math.PI / 180;
 
  }
 
  // ==========================================
  // User Actions
  // ==========================================
 
  selectCenter(center: Center): void {
 
    this.selectedCenter = center;
 
    this.map.setView(
 
      [center.latitude, center.longitude],
 
      12
 
    );
 
    // Open popup automatically
 
    this.markers.forEach(marker => {
 
      const location = marker.getLatLng();
 
      if (
 
        location.lat === center.latitude &&
 
        location.lng === center.longitude
 
      ) {
 
        marker.openPopup();
 
      }
 
    });
 
  }
 
 
  openDirections(center: Center): void {
 
    window.open(
 
      `https://www.google.com/maps/dir/?api=1&destination=${center.latitude},${center.longitude}`,
 
      '_blank'
 
    );
 
  }
 
  // ==========================================
  // Helper Functions
  // ==========================================
 
  getImage(center: Center): string {
 
    switch(center.city.toLowerCase()){
 
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