import { Component, OnInit, AfterViewInit, ElementRef, ViewChild, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Chart, registerables } from 'chart.js';
import { Activity, ActivityItem } from '../../../shared/services/activity';
import { Admin as AdminService, CenterItem, AccountItem, AdminStats, ActivityPayload, CenterPayload } from '../../../shared/services/admin';

Chart.register(...registerables);

type Tab = 'overview' | 'activities' | 'centers' | 'accounts';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminDashboard implements OnInit, AfterViewInit {
  @ViewChild('trendCanvas') trendCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('ratioCanvas') ratioCanvas!: ElementRef<HTMLCanvasElement>;

  private trendChart: Chart | null = null;
  private ratioChart: Chart | null = null;

  activeTab = signal<Tab>('overview');

  activities = signal<ActivityItem[]>([]);
  centers = signal<CenterItem[]>([]);
  accounts = signal<AccountItem[]>([]);
  stats = signal<AdminStats | null>(null);
  statsLoading = signal(true);
  errorMessage = signal('');

  showForm = signal(false);
  editingId = signal<number | null>(null);

  activityForm: ActivityPayload = { name: '', description: '', minAge: 0, maxAge: 18, targetCondition: 'ASD' };
  centerForm: CenterPayload = { name: '', city: '', specialty: 'ASD', phone: '', latitude: 0, longitude: 0 };

  constructor(private activityService: Activity, private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadActivities();
    this.loadCenters();
    this.loadAccounts();
  }

  ngAfterViewInit(): void {
    // Charts render once stats actually arrive — see loadStats()
  }

  switchTab(tab: Tab): void {
    this.activeTab.set(tab);
    this.showForm.set(false);
    this.editingId.set(null);
  }

  loadStats(): void {
    this.statsLoading.set(true);
    this.adminService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.statsLoading.set(false);
        // One tick so the canvas elements exist in the DOM (they're
        // behind @if (statsLoading()) until this point) before drawing.
        setTimeout(() => this.renderCharts(data), 0);
      },
      error: () => { this.errorMessage.set('Could not load dashboard stats.'); this.statsLoading.set(false); }
    });
  }

  private renderCharts(data: AdminStats): void {
    this.trendChart?.destroy();
    this.ratioChart?.destroy();

    if (this.trendCanvas) {
      this.trendChart = new Chart(this.trendCanvas.nativeElement, {
        type: 'bar',
        data: {
          labels: data.registrationTrend.map(([date]) =>
            new Date(date).toLocaleDateString('en-US', { weekday: 'short' })
          ),
          datasets: [{
            label: 'New registrations',
            data: data.registrationTrend.map(([, count]) => count),
            backgroundColor: '#3562E9',
            borderRadius: 8,
            maxBarThickness: 36
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: { legend: { display: false } },
          scales: {
            y: { beginAtZero: true, ticks: { precision: 0 }, grid: { color: 'rgba(22,33,62,0.06)' } },
            x: { grid: { display: false } }
          }
        }
      });
    }

    if (this.ratioCanvas) {
      this.ratioChart = new Chart(this.ratioCanvas.nativeElement, {
        type: 'doughnut',
        data: {
          labels: ['Active', 'Deactivated'],
          datasets: [{
            data: [data.activeAccounts, data.deactivatedAccounts],
            backgroundColor: ['#2FAE7C', '#E2604F'],
            borderWidth: 0
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          cutout: '72%',
          plugins: { legend: { display: false } }
        }
      });
    }
  }

  loadActivities(): void {
    this.activityService.getAll().subscribe({
      next: (data) => this.activities.set(data),
      error: () => this.errorMessage.set('Could not load activities.')
    });
  }

  loadCenters(): void {
    this.adminService.getAllCenters().subscribe({
      next: (data) => this.centers.set(data),
      error: () => this.errorMessage.set('Could not load centers.')
    });
  }

  loadAccounts(): void {
    this.adminService.getAllAccounts().subscribe({
      next: (data) => this.accounts.set(data),
      error: () => this.errorMessage.set('Could not load accounts.')
    });
  }

  startCreate(): void {
    this.editingId.set(null);
    this.activityForm = { name: '', description: '', minAge: 0, maxAge: 18, targetCondition: 'ASD' };
    this.centerForm = { name: '', city: '', specialty: 'ASD', phone: '', latitude: 0, longitude: 0 };
    this.showForm.set(true);
  }

  startEditActivity(a: ActivityItem): void {
    this.editingId.set(a.id);
    this.activityForm = { name: a.name, description: a.description, minAge: a.minAge, maxAge: a.maxAge, targetCondition: a.targetCondition };
    this.showForm.set(true);
  }

  startEditCenter(c: CenterItem): void {
    this.editingId.set(c.id);
    this.centerForm = { name: c.name, city: c.city, specialty: c.specialty, phone: c.phone, latitude: c.latitude, longitude: c.longitude };
    this.showForm.set(true);
  }

  saveActivity(): void {
    const id = this.editingId();
    const req = id ? this.adminService.updateActivity(id, this.activityForm) : this.adminService.createActivity(this.activityForm);
    req.subscribe({
      next: () => { this.showForm.set(false); this.loadActivities(); },
      error: () => this.errorMessage.set('Could not save activity.')
    });
  }

  saveCenter(): void {
    const id = this.editingId();
    const req = id ? this.adminService.updateCenter(id, this.centerForm) : this.adminService.createCenter(this.centerForm);
    req.subscribe({
      next: () => { this.showForm.set(false); this.loadCenters(); },
      error: () => this.errorMessage.set('Could not save center.')
    });
  }

  deleteActivity(id: number): void {
    if (!confirm('Deactivate this activity?')) return;
    this.adminService.deleteActivity(id).subscribe({ next: () => this.loadActivities() });
  }

  deleteCenter(id: number): void {
    if (!confirm('Deactivate this center?')) return;
    this.adminService.deleteCenter(id).subscribe({ next: () => this.loadCenters() });
  }

  toggleAccount(account: AccountItem): void {
    const request$ = account.isActive
      ? this.adminService.deactivateAccount(account.id)
      : this.adminService.reactivateAccount(account.id);

    request$.subscribe({
      next: () => { this.loadAccounts(); this.loadStats(); },
      error: () => this.errorMessage.set('Could not update account status.')
    });
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.editingId.set(null);
  }
}