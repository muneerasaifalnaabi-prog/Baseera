import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Activity, ActivityItem } from '../../../shared/services/activity';

type ConditionFilter = '' | 'ASD' | 'ADHD' | 'BOTH';

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
  conditionFilter = signal<ConditionFilter>('');
  ageFilter: number | null = null;

  lang = signal<'en' | 'ar'>('en');

  constructor(private activityService: Activity) {}

  ngOnInit(): void {
    this.loadAll();
  }

  toggleLang(): void {
    this.lang.set(this.lang() === 'en' ? 'ar' : 'en');
  }

  loadAll(): void {
    this.loading.set(true);
    this.errorMessage.set('');
    this.activityService.getAll().subscribe({
      next: (data) => {
        this.activities.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set(this.t[this.lang()].loadError);
        this.loading.set(false);
      }
    });
  }

  selectCondition(condition: ConditionFilter): void {
    this.conditionFilter.set(condition);
    this.applyFilters();
  }

  applyFilters(): void {
    this.loading.set(true);
    this.errorMessage.set('');

    this.activityService
      .searchActivities(
        this.nameFilter || undefined,
        this.conditionFilter() || undefined,
        this.ageFilter ?? undefined
      )
      .subscribe({
        next: (data) => {
          this.activities.set(data);
          this.loading.set(false);
        },
        error: () => {
          this.errorMessage.set(this.t[this.lang()].loadError);
          this.loading.set(false);
        }
      });
  }

  clearFilters(): void {
    this.nameFilter = '';
    this.conditionFilter.set('');
    this.ageFilter = null;
    this.loadAll();
  }

  conditionLabel(condition: 'ASD' | 'ADHD' | 'BOTH'): string {
    return this.t[this.lang()].conditionLabels[condition];
  }

  t = {
    en: {
      title: 'Activities',
      subtitle: 'Matched to your child\'s age and needs.',
      searchPlaceholder: 'Search by name',
      agePlaceholder: 'Age',
      all: 'All',
      clear: 'Clear',
      loadError: 'Could not load activities. Please try again.',
      emptyTitle: 'No activities match your search',
      emptyText: 'Try adjusting the filters above.',
      agesLabel: 'Ages',
      langLabel: 'العربية',
      conditionLabels: { ASD: 'ASD', ADHD: 'ADHD', BOTH: 'Both' }
    },
    ar: {
      title: 'الأنشطة',
      subtitle: 'مطابقة لعمر طفلك واحتياجاته.',
      searchPlaceholder: 'ابحث بالاسم',
      agePlaceholder: 'العمر',
      all: 'الكل',
      clear: 'مسح',
      loadError: 'تعذّر تحميل الأنشطة. يرجى المحاولة مرة أخرى.',
      emptyTitle: 'لا توجد أنشطة تطابق بحثك',
      emptyText: 'حاول تعديل عوامل التصفية أعلاه.',
      agesLabel: 'الأعمار',
      langLabel: 'English',
      conditionLabels: { ASD: 'توحد', ADHD: 'فرط حركة', BOTH: 'كلاهما' }
    }
  };
  getActivityImage(activity: ActivityItem): string {
  const name = activity.name.toLowerCase();

  const keywordMap: { keywords: string[]; url: string }[] = [
    { keywords: ['shape sorting', 'shape', 'sorting puzzle'], url: 'https://images.unsplash.com/photo-1587654780291-39c9404d746b?w=400&q=80' },
    { keywords: ['simon says', 'following directions'], url: 'https://images.unsplash.com/photo-1587616211892-b6bd15bb1b90?w=400&q=80' },
    { keywords: ['sensory bin', 'sensory exploration'], url: 'https://images.unsplash.com/photo-1560785496-3c9d27877182?w=400&q=80' },
    { keywords: ['visual schedule', 'schedule building'], url: 'https://images.unsplash.com/photo-1503676260728-1c00da094a0b?w=400&q=80' },
   { keywords: ['obstacle course', 'timer challenge'], url: 'https://images.unsplash.com/photo-1587616211892-b6bd15bb1b90?w=400&q=80' },
  ];

  const match = keywordMap.find((entry) =>
    entry.keywords.some((k) => name.includes(k))
  );
  if (match) return match.url;

  // Fallback by condition, only used if a future activity's name doesn't match any keyword above
  const conditionFallback: Record<string, string> = {
    ASD: 'https://images.unsplash.com/photo-1543269865-cbf427effbad?w=400&q=80',
    ADHD: 'https://images.unsplash.com/photo-1503454537195-1dcabb73ffb9?w=400&q=80',
    BOTH: 'https://images.unsplash.com/photo-1503919545889-aef636e10ad4?w=400&q=80',
  };
  return conditionFallback[activity.targetCondition];
}
}