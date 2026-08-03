import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Child, ChildItem } from '../../../shared/services/child';
import { Assessment as AssessmentService, AssessmentItem } from '../../../shared/services/assessment';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class Home implements OnInit {
  t = translations.dashboard;

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
}
