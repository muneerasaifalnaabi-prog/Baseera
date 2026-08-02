import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Activity, ActivityItem } from '../../../shared/services/activity';

@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './activities.html',
  styleUrl: './activities.css'
})
export class Activities implements OnInit {
  activities = signal<ActivityItem[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  nameFilter = '';
  conditionFilter = '';
  ageFilter: number | null = null;

  constructor(private activityService: Activity) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading.set(true);
    this.activityService.getAll().subscribe({
      next: (data) => {
        this.activities.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Could not load activities.');
        this.loading.set(false);
      }
    });
  }

  applyFilters(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.activityService
      .searchActivities(
        this.nameFilter || undefined,
        this.conditionFilter || undefined,
        this.ageFilter ?? undefined
      )
      .subscribe({
        next: (data) => {
          this.activities.set(data);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set('Could not load activities.');
          this.loading.set(false);
        }
      });
  }

  clearFilters(): void {
    this.nameFilter = '';
    this.conditionFilter = '';
    this.ageFilter = null;
    this.loadAll();
  }
}