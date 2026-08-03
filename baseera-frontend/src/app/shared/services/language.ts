import { Injectable, signal } from '@angular/core';

export type Lang = 'en' | 'ar';

/**
 * ONE shared language state for the whole app. Every page injects this
 * instead of keeping its own local `lang = signal('en')` — switching
 * language on one page now means it's switched everywhere, since they're
 * all reading the same signal, not five separate copies of the same idea.
 */
@Injectable({
  providedIn: 'root'
})
export class Language {
  current = signal<Lang>((localStorage.getItem('lang') as Lang) ?? 'en');

  toggle(): void {
    const next = this.current() === 'en' ? 'ar' : 'en';
    this.current.set(next);
    localStorage.setItem('lang', next);
  }
}
