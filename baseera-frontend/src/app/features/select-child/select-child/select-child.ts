import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { Child, ChildItem, ChildPayload } from '../../../shared/services/child';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';

@Component({
  selector: 'app-select-child',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './select-child.html',
  styleUrl: './select-child.css'
})
export class SelectChild implements OnInit {
  t = translations.selectChild;

  children = signal<ChildItem[]>([]);
  loading = signal(true);
  errorMessage = signal('');

  showAddForm = signal(false);
  saving = signal(false);

  form: ChildPayload = { fullName: '', dateOfBirth: '', gender: 'MALE' };
  today = new Date().toISOString().split('T')[0];

  constructor(private childService: Child, private router: Router, public lang: Language) {}

  ngOnInit(): void {
    this.loadChildren();
  }

  loadChildren(): void {
    this.loading.set(true);
    this.childService.getMyChildren().subscribe({
      next: (data) => {
        this.children.set(data);
        this.loading.set(false);
        // No children yet — skip straight to the add form, no reason to
        // show an empty list with nothing to pick.
        if (data.length === 0) {
          this.showAddForm.set(true);
        }
      },
      error: () => {
        this.errorMessage.set(this.t[this.lang.current()].loadError);
        this.loading.set(false);
      }
    });
  }

  selectChild(childId: number): void {
    // Remembered so every other page (assessment, vault, activities)
    // knows which child is active without re-asking.
    localStorage.setItem('selectedChildId', String(childId));
    this.router.navigate(['/home']);
  }

  saveNewChild(): void {
    if (!this.form.fullName || !this.form.dateOfBirth || !this.form.gender) return;

    this.saving.set(true);
    this.childService.createChild(this.form).subscribe({
      next: (created) => {
        this.saving.set(false);
        this.selectChild(created.id);
      },
      error: () => {
        this.saving.set(false);
        this.errorMessage.set(this.t[this.lang.current()].saveError);
      }
    });
  }

  cancelAdd(): void {
    if (this.children().length > 0) {
      this.showAddForm.set(false);
    }
  }
}
