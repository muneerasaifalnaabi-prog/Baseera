import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Activity, ActivityItem } from '../../../shared/services/activity';
import { Child, ChildItem } from '../../../shared/services/child';
import { Progress, ProgressItem } from '../../../shared/services/progress';


type ConditionFilter = '' | 'ASD' | 'ADHD' | 'BOTH';


@Component({
  selector: 'app-activities',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './activities.html',
  styleUrl: './activities.css'
})
export class Activities implements OnInit {


  activities = signal<ActivityItem[]>([]);

  loading = signal(true);

  errorMessage = signal('');



  // Filters

  nameFilter = '';

  conditionFilter = signal<ConditionFilter>('');

  ageFilter: number | null = null;



  // Language

  lang = signal<'en' | 'ar'>('en');



  // Authentication

  isLoggedIn = signal(false);



  // Child

  children = signal<ChildItem[]>([]);

  selectedChildId = signal<number | null>(null);



  // Progress

  addedActivityIds = signal<Set<number>>(new Set());

  addingId = signal<number | null>(null);



  constructor(
    private activityService: Activity,
    private childService: Child,
    private progressService: Progress
  ) {}



  ngOnInit(): void {

    this.loadAll();


    this.isLoggedIn.set(
      !!localStorage.getItem('token')
    );


    if (this.isLoggedIn()) {

      this.loadChildren();

    }

  }



  toggleLang(): void {

    this.lang.set(
      this.lang() === 'en'
        ? 'ar'
        : 'en'
    );

  }
    // ============================
  // Load Activities
  // ============================

  loadAll(): void {

    this.loading.set(true);

    this.errorMessage.set('');


    this.activityService.getAll()
      .subscribe({

        next: (data) => {

          this.activities.set(data);

          this.loading.set(false);

        },


        error: () => {

          this.errorMessage.set(
            this.t[this.lang()].loadError
          );

          this.loading.set(false);

        }

      });

  }



  // ============================
  // Load Children
  // ============================

  loadChildren(): void {

    this.childService.getMyChildren()
      .subscribe({

        next: (data) => {

          this.children.set(data);


          if (data.length > 0) {

            this.selectChild(data[0].id);

          }

        },


        error: () => {

          // keep activities available

        }

      });

  }



  // ============================
  // Select Child
  // ============================

  selectChild(childId: number): void {

    this.selectedChildId.set(childId);


    localStorage.setItem(
      'selectedChildId',
      String(childId)
    );



    this.progressService
      .getActivitiesForChild(childId)
      .subscribe({

        next: (list: ProgressItem[]) => {

          this.addedActivityIds.set(
            new Set(
              list.map(
                item => item.activityId
              )
            )
          );

        },


        error: () => {

          this.addedActivityIds.set(
            new Set()
          );

        }

      });

  }



  // ============================
  // Child Dropdown
  // ============================

  onChildDropdownChange(value: string): void {

    const id = Number(value);


    if (id) {

      this.selectChild(id);

    }

  }



  // ============================
  // Check Added
  // ============================

  isAdded(activityId: number): boolean {

    return this.addedActivityIds()
      .has(activityId);

  }



  // ============================
  // Add Activity
  // ============================

  addToChild(activityId: number): void {

    const childId =
      this.selectedChildId();



    if (!childId) {

      return;

    }



    this.addingId.set(activityId);



    this.progressService
      .addActivityToChild(
        childId,
        activityId
      )
      .subscribe({

        next: () => {

          this.addingId.set(null);



          const updated =
            new Set(
              this.addedActivityIds()
            );


          updated.add(activityId);


          this.addedActivityIds.set(
            updated
          );

        },


        error: () => {


          this.addingId.set(null);



          const updated =
            new Set(
              this.addedActivityIds()
            );


          updated.add(activityId);


          this.addedActivityIds.set(
            updated
          );

        }

      });

  }



  // ============================
  // Filters
  // ============================

  selectCondition(
    condition: ConditionFilter
  ): void {

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

          this.errorMessage.set(
            this.t[this.lang()].loadError
          );


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



  conditionLabel(
    condition: 'ASD' | 'ADHD' | 'BOTH'
  ): string {

    return this.t[this.lang()]
      .conditionLabels[condition];

  }
    // ============================
  // Activity Images
  // ============================
getActivityImage(activity: ActivityItem): string {

  const name = activity.name.toLowerCase();


  const keywordMap = [

    {
      keywords: [
        'shape sorting',
        'shape',
        'sorting puzzle',
        'فرز الأشكال'
      ],
      url: '/assets/images/activities/shape-sorting.webp'
    },


    {
      keywords: [
        'simon says',
        'following directions',
        'اتبع'
      ],
      url: '/assets/images/activities/simon-says.webp'
    },


    {
      keywords: [
        'sensory bin',
        'sensory exploration',
        'حسي'
      ],
      url: '/assets/images/activities/sensory-bin.webp'
    },


    {
      keywords: [
        'visual schedule',
        'schedule building',
        'جدول بصري'
      ],
      url: '/assets/images/activities/visual-schedule.webp'
    },


    {
      keywords: [
        'obstacle course',
        'timer challenge',
        'مسار'
      ],
      url: '/assets/images/activities/obstacle-course.webp'
    }

  ];


  const match = keywordMap.find(item =>
    item.keywords.some(keyword =>
      name.includes(keyword)
    )
  );


  return match
    ? match.url
    : '/assets/images/activities/sensory-bin.webp';
}




  // ============================
  // Translation
  // ============================

  t = {

    en: {

      title:
        'Activities',

      subtitle:
        'Matched to your child\'s age and needs.',


      searchPlaceholder:
        'Search by name',


      agePlaceholder:
        'Age',


      all:
        'All',


      clear:
        'Clear',


      agesLabel:
        'Ages',


      langLabel:
        'العربية',


      emptyTitle:
        'No activities match your search',


      emptyText:
        'Try adjusting the filters above.',


      loadError:
        'Could not load activities. Please try again.',


      addFor:
        '+ Add for child',


      added:
        'Added',


      adding:
        '...',


      forLabel:
        'Activities for',



      conditionLabels: {

        ASD:
          'ASD',

        ADHD:
          'ADHD',

        BOTH:
          'Both'

      }

    },



    ar: {


      title:
        'الأنشطة',


      subtitle:
        'مطابقة لعمر طفلك واحتياجاته.',


      searchPlaceholder:
        'ابحث بالاسم',


      agePlaceholder:
        'العمر',


      all:
        'الكل',


      clear:
        'مسح',


      agesLabel:
        'الأعمار',


      langLabel:
        'English',


      emptyTitle:
        'لا توجد أنشطة تطابق بحثك',


      emptyText:
        'حاول تعديل عوامل التصفية أعلاه.',


      loadError:
        'تعذّر تحميل الأنشطة. يرجى المحاولة مرة أخرى.',


      addFor:
        '+ إضافة للطفل',


      added:
        'تمت الإضافة',


      adding:
        '...',


      forLabel:
        'أنشطة',



      conditionLabels: {

        ASD:
          'توحد',


        ADHD:
          'فرط حركة',


        BOTH:
          'كلاهما'

      }

    }

  };

}