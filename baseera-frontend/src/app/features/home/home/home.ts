import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Child, ChildItem } from '../../../shared/services/child';
import { Assessment as AssessmentService, AssessmentItem } from '../../../shared/services/assessment';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';
import { Navbar } from '../../../shared/components/navbar/navbar';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink, Navbar],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  t = translations.dashboard;
  tSidebar = translations.sidebar;

  fullName = '';
  loading = signal(true);
  errorMessage = signal('');

  children = signal<ChildItem[]>([]);
  assessments = signal<AssessmentItem[]>([]);
  selectedChild = signal<ChildItem | null>(null);

  constructor(
    private childService: Child,
    private assessmentService: AssessmentService,
    public lang: Language
  ) {}

  ngOnInit(): void {
    this.fullName = localStorage.getItem('fullName') ?? '';
    this.loadDashboard();
  }

  loadDashboard(): void {
    this.loading.set(true);

    this.childService.getMyChildren().subscribe({
      next: (children) => {
        this.children.set(children);

        if (children.length === 0) {
          this.loading.set(false);
          return;
        }
        const storedId = localStorage.getItem('selectedChildId');
        const active = children.find(c => String(c.id) === storedId) ?? children[0];
        this.selectedChild.set(active);

        forkJoin({
          assessments: this.assessmentService.getHistory(active.id).pipe(catchError(() => of([])))
        }).subscribe({
          next: ({ assessments }) => {
            this.assessments.set(assessments);
            this.loading.set(false);
          },
          error: () => this.loading.set(false)
        });
      },
      error: () => {
        this.errorMessage.set(this.t[this.lang.current()].loadError);
        this.loading.set(false);
      }
    });
  }

  // First name only, so the hero greeting doesn't wrap awkwardly on mobile
  get firstName(): string {
    return this.fullName.trim().split(/\s+/)[0] ?? '';
  }

  hasChildren(): boolean {
    return this.children().length > 0;
  }

  latestAssessment(): AssessmentItem | null {
    const list = this.assessments();
    return list.length > 0 ? list[0] : null;
  }

  riskLabel(risk: 'LOW' | 'MEDIUM' | 'HIGH'): string {
    const map = { LOW: 'riskLow', MEDIUM: 'riskMedium', HIGH: 'riskHigh' } as const;
    return this.t[this.lang.current()][map[risk]];
  }

  riskClass(risk: 'LOW' | 'MEDIUM' | 'HIGH'): string {
    return 'risk-' + risk.toLowerCase();
  }

  vaultLink(): string[] {
    const child = this.selectedChild();
    return child ? ['/children', String(child.id), 'vault'] : ['/select-child'];
  }

  continueLink(): string[] {
    return this.hasChildren() ? this.vaultLink() : ['/select-child'];
  }

  // Real, continuous mouse-tracked tilt on the hero photo — the CSS
  // reads these two custom properties to rotate the image based on
  // exactly where the cursor is, not just a fixed hover angle.
  onHeroMouseMove(event: MouseEvent): void {
    const target = event.currentTarget as HTMLElement;
    const rect = target.getBoundingClientRect();
    const x = (event.clientX - rect.left) / rect.width - 0.5;
    const y = (event.clientY - rect.top) / rect.height - 0.5;
    target.style.setProperty('--tilt-x', `${y * -8}deg`);
    target.style.setProperty('--tilt-y', `${x * 8}deg`);
  }

  onHeroMouseLeave(event: MouseEvent): void {
    const target = event.currentTarget as HTMLElement;
    target.style.setProperty('--tilt-x', '0deg');
    target.style.setProperty('--tilt-y', '0deg');
  }
}