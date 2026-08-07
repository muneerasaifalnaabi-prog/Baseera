import { Component, OnInit, ElementRef, ViewChild, signal, computed, DestroyRef, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { interval } from 'rxjs';
import { Chart, registerables } from 'chart.js';
import { Activity, ActivityItem } from '../../../shared/services/activity';
import { Admin as AdminService, CenterItem, AccountItem, AdminStats, ActivityPayload, CenterPayload } from '../../../shared/services/admin';

Chart.register(...registerables);

type Tab = 'overview' | 'activities' | 'centers' | 'accounts';

const COLOR_PRIMARY = '#3562E9';
const COLOR_PRIMARY_HOVER = '#4A76F5';
const COLOR_SUCCESS = '#16A34A';
const COLOR_DANGER = '#DC2626';
const COLOR_TEXT = '#16213E';

@Component({
  selector: 'app-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './admin.html',
  styleUrl: './admin.css'
})
export class AdminDashboard implements OnInit {
  @ViewChild('trendCanvas') trendCanvas!: ElementRef<HTMLCanvasElement>;
  @ViewChild('ratioCanvas') ratioCanvas!: ElementRef<HTMLCanvasElement>;

  private trendChart: Chart | null = null;
  private ratioChart: Chart | null = null;
  private destroyRef = inject(DestroyRef);

  activeTab = signal<Tab>('overview');

  activities = signal<ActivityItem[]>([]);
  centers = signal<CenterItem[]>([]);
  accounts = signal<AccountItem[]>([]);
  stats = signal<AdminStats | null>(null);
  statsLoading = signal(true);
  refreshing = signal(false);
  lastUpdated = signal<Date | null>(null);
  errorMessage = signal('');

  // Derived, not stored — recomputes itself the instant stats() changes,
  // so the "Total accounts" KPI can never drift out of sync with the
  // two numbers it's built from.
  totalAccounts = computed(() => {
    const s = this.stats();
    return s ? s.activeAccounts + s.deactivatedAccounts : 0;
  });

  // Which doughnut segment (if any) the cursor is currently over — the
  // center label swaps to show that segment's own value while hovered.
  donutHover = signal<{ label: string; value: number } | null>(null);
  legendVisible = signal<[boolean, boolean]>([true, true]);

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

    // Live dashboard: stats quietly re-fetch every 20s with no manual
    // "refresh" button and no loading skeleton flash — only while the
    // Overview tab is actually visible, and never mid-edit on another tab.
    interval(20000)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe(() => {
        if (this.activeTab() === 'overview') {
          this.loadStats(true);
        }
      });
  }

  switchTab(tab: Tab): void {
    this.activeTab.set(tab);
    this.showForm.set(false);
    this.editingId.set(null);

    // Angular's @if tears down the <canvas> elements when you leave
    // Overview and mounts brand-new ones when you come back — the old
    // Chart.js instances are left pointing at detached canvases, so the
    // charts need a fresh render against the new elements.
    if (tab === 'overview' && this.stats()) {
      setTimeout(() => this.renderCharts(this.stats()!), 0);
    }
  }

  loadStats(silent = false): void {
    if (silent) {
      this.refreshing.set(true);
    } else {
      this.statsLoading.set(true);
    }

    this.adminService.getStats().subscribe({
      next: (data) => {
        this.stats.set(data);
        this.statsLoading.set(false);
        this.refreshing.set(false);
        this.lastUpdated.set(new Date());
        setTimeout(() => this.renderCharts(data), 0);
      },
      error: () => {
        this.errorMessage.set('Could not load dashboard stats.');
        this.statsLoading.set(false);
        this.refreshing.set(false);
      }
    });
  }

  private renderCharts(data: AdminStats): void {
    this.trendChart?.destroy();
    this.ratioChart?.destroy();
    this.legendVisible.set([true, true]);
    this.donutHover.set(null);

    if (this.trendCanvas) {
      const days = this.lastSevenDays(data.registrationTrend);

      const ctx = this.trendCanvas.nativeElement.getContext('2d');
      const gradient = ctx?.createLinearGradient(0, 0, 0, 220);
      gradient?.addColorStop(0, COLOR_PRIMARY_HOVER);
      gradient?.addColorStop(1, COLOR_PRIMARY);

      this.trendChart = new Chart(this.trendCanvas.nativeElement, {
        type: 'bar',
        data: {
          labels: days.map(([date]) =>
            new Date(date).toLocaleDateString('en-US', { weekday: 'short' })
          ),
          datasets: [{
            label: 'New registrations',
            data: days.map(([, count]) => count),
            backgroundColor: gradient ?? COLOR_PRIMARY,
            hoverBackgroundColor: COLOR_PRIMARY_HOVER,
            borderRadius: { topLeft: 6, topRight: 6, bottomLeft: 0, bottomRight: 0 },
            borderSkipped: false,
            barPercentage: 0.55,
            categoryPercentage: 0.7
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          resizeDelay: 100,
          animation: { duration: 550, easing: 'easeOutQuart' },
          interaction: { mode: 'nearest', intersect: true },
          onHover: (evt, elements) => {
            const target = evt.native?.target as HTMLElement | undefined;
            if (target) target.style.cursor = elements.length ? 'pointer' : 'default';
          },
          plugins: {
            legend: { display: false },
            tooltip: {
              backgroundColor: COLOR_TEXT,
              titleFont: { family: 'Nunito Sans', weight: 700 },
              bodyFont: { family: 'Nunito Sans' },
              padding: 10,
              cornerRadius: 8,
              displayColors: false,
              callbacks: {
                title: (items) => new Date(days[items[0].dataIndex][0]).toLocaleDateString('en-US', { weekday: 'long', month: 'short', day: 'numeric' }),
                label: (ctx) => `${ctx.parsed.y} new sign-up${ctx.parsed.y === 1 ? '' : 's'}`
              }
            }
          },
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
            backgroundColor: [COLOR_SUCCESS, COLOR_DANGER],
            hoverOffset: 6,
            borderWidth: 0
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          resizeDelay: 100,
          cutout: '72%',
          animation: { duration: 550, easing: 'easeOutQuart' },
          plugins: {
            legend: { display: false },
            tooltip: {
              backgroundColor: COLOR_TEXT,
              padding: 10,
              cornerRadius: 8,
              displayColors: false
            }
          },
          onHover: (evt, elements) => {
            const target = evt.native?.target as HTMLElement | undefined;
            if (elements.length) {
              if (target) target.style.cursor = 'pointer';
              const idx = elements[0].index;
              const labels = ['Active', 'Deactivated'];
              const values = [data.activeAccounts, data.deactivatedAccounts];
              this.donutHover.set({ label: labels[idx].toLowerCase(), value: values[idx] });
            } else {
              if (target) target.style.cursor = 'default';
              this.donutHover.set(null);
            }
          }
        }
      });
    }
  }

  // Clicking a legend swatch toggles that slice on/off in the doughnut —
  // a real interaction, not just a static color key.
  // The backend only returns rows for days that actually had a
  // registration, which is why the chart could show "Sat, Sun, Wed"
  // with days silently missing. This builds a real, contiguous 7-day
  // window ending today and zero-fills anything the backend left out.
  private lastSevenDays(raw: [string, number][]): [string, number][] {
    const counts = new Map(raw.map(([date, count]) => [date.slice(0, 10), count]));
    const result: [string, number][] = [];

    for (let i = 6; i >= 0; i--) {
      const d = new Date();
      d.setDate(d.getDate() - i);
      const key = d.toISOString().slice(0, 10);
      result.push([key, counts.get(key) ?? 0]);
    }

    return result;
  }

  toggleSegment(index: 0 | 1): void {
    if (!this.ratioChart) return;
    this.ratioChart.toggleDataVisibility(index);
    this.ratioChart.update();

    const current = this.legendVisible();
    const next: [boolean, boolean] = [...current] as [boolean, boolean];
    next[index] = !next[index];
    this.legendVisible.set(next);
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
      next: () => { this.loadAccounts(); this.loadStats(true); },
      error: () => this.errorMessage.set('Could not update account status.')
    });
  }

  cancelForm(): void {
    this.showForm.set(false);
    this.editingId.set(null);
  }
}