import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Activity, ActivityItem } from '../../../shared/services/activity';
import { Admin as AdminService, CenterItem, AccountItem, AdminStats, ActivityPayload, CenterPayload } from '../../../shared/services/admin';

type Tab = 'overview' | 'activities' | 'centers' | 'accounts';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminDashboard implements OnInit {
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

  // The tallest bar in the chart, used to scale every other bar's
  // height proportionally — recalculates automatically if stats change.
  maxTrendCount = computed(() => {
    const trend = this.stats()?.registrationTrend ?? [];
    return Math.max(1, ...trend.map(([, count]) => count));
  });

  constructor(private activityService: Activity, private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadStats();
    this.loadActivities();
    this.loadCenters();
    this.loadAccounts();
  }

  switchTab(tab: Tab): void {
    this.activeTab.set(tab);
    this.showForm.set(false);
    this.editingId.set(null);
  }

  loadStats(): void {
    this.statsLoading.set(true);
    this.adminService.getStats().subscribe({
      next: (data) => { this.stats.set(data); this.statsLoading.set(false); },
      error: () => { this.errorMessage.set('Could not load dashboard stats.'); this.statsLoading.set(false); }
    });
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

  // Now correctly calls deactivate OR reactivate based on the account's
  // REAL current status, not always deactivate like before.
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

  // Formats "2026-08-04" into a short weekday label for the chart's x-axis
  formatChartDate(dateStr: string): string {
    const date = new Date(dateStr);
    return date.toLocaleDateString('en-US', { weekday: 'short' });
  }
}