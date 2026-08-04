import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';

/**
 * The public front door — the first thing anyone sees before logging in.
 * Deliberately calm and emotional rather than feature-listy: a parent
 * arriving here is usually worried, not shopping for software.
 */
@Component({
  selector: 'app-welcome',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './welcome.html',
  styleUrl: './welcome.css'
})
export class Welcome {
  t = translations.welcome;

  constructor(public lang: Language) {}
}
