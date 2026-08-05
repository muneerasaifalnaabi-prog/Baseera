import { Component, OnInit, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Assessment as AssessmentService, AssessmentItem } from '../../../shared/services/assessment';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';
import { CenterService } from '../../center/services/center.service';
import { Center } from '../../center/models/center.model';;
import { RouterModule } from '@angular/router';

type Stage = 'form' | 'thinking' | 'result';

@Component({
  selector: 'app-assessment',
  standalone: true,
  imports: [CommonModule, FormsModule,RouterModule],
  templateUrl: './assessment.html',
  styleUrl: './assessment.css'
})
export class AssessmentPage implements OnInit {
  t = translations.assessment;

  stage = signal<Stage>('form');
  description = '';
  errorMessage = signal('');

  history = signal<AssessmentItem[]>([]);
  latestResult = signal<AssessmentItem | null>(null);
  recommendedCenters = signal<Center[]>([]);

  childId: number | null = null;

  constructor(private assessmentService: AssessmentService, private centerService:CenterService,public lang: Language) {}

  ngOnInit(): void {
    const stored = localStorage.getItem('selectedChildId');
    this.childId = stored ? Number(stored) : null;

    if (this.childId) {
      this.loadHistory();
    }
  }

  loadHistory(): void {
    if (!this.childId) return;
    this.assessmentService.getHistory(this.childId).subscribe({
      next: (data) => this.history.set(data),
      error: () => { /* history is a nice-to-have, don't block the page on it */ }
    });
  }

  submit(): void {
    if (!this.childId) {
      this.errorMessage.set(this.t[this.lang.current()].noChildSelected);
      return;
    }
    if (this.description.trim().length < 10) return;

    this.errorMessage.set('');
    this.stage.set('thinking');

    // A brief, deliberate pause before showing the result — an instant
    // answer to something this sensitive would feel careless. See the
    // original design notes: this mirrors the "AI thinking" moment
    // built for the assessment flow from the very start of this project.
    this.assessmentService.submit(this.childId, this.description).subscribe({
      next: (result) => {

        setTimeout(() => {

          this.latestResult.set(result);

          this.stage.set('result');

          this.loadHistory();

          if (this.childId) {

            this.centerService
              .getRecommendedCenters(this.childId)
              .subscribe({

                next: centers => {

                  this.recommendedCenters.set(
                    centers.slice(0, 3)
                  );

                }

              });

          }

        }, 1800);

      },
      error: () => {
        this.stage.set('form');
        this.errorMessage.set(this.t[this.lang.current()].submitError);
      }
    });
  }

  startNew(): void {
    this.description = '';
    this.latestResult.set(null);
    this.stage.set('form');
  }

  riskLabel(risk: 'LOW' | 'MEDIUM' | 'HIGH'): string {
    const map = { LOW: 'riskLow', MEDIUM: 'riskMedium', HIGH: 'riskHigh' } as const;
    return this.t[this.lang.current()][map[risk]];
  }

  openHistory(item: AssessmentItem): void {

    this.latestResult.set(item);

    this.stage.set('result');

    if (!this.childId) return;

    this.centerService
      .getRecommendedCenters(this.childId)
      .subscribe({
        next: centers => {
          this.recommendedCenters.set(
            centers.slice(0, 3)
          );
        }
      });

  }

}
