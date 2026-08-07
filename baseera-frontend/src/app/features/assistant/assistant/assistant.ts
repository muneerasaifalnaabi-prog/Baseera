import { Component, signal, ViewChild, ElementRef, AfterViewChecked } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Assistant } from '../../../shared/services/assistant';
import { Language } from '../../../shared/services/language';
import { translations } from '../../../shared/i18n/translations';

interface ChatMessage {
  role: 'user' | 'ai';
  text: string;
}

@Component({
  selector: 'app-assistant',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assistant.html',
  styleUrl: './assistant.css'
})
export class AssistantChat implements AfterViewChecked {
  @ViewChild('scrollAnchor') private scrollAnchor?: ElementRef<HTMLDivElement>;

  t = translations.assistant;
  draft = '';
  sending = signal(false);
  errorMessage = signal('');
  messages = signal<ChatMessage[]>([]);

  private shouldScroll = false;

  constructor(private assistantService: Assistant, public lang: Language) {
    // The very first thing a parent sees when they open the chat —
    // Baseera introduces itself instead of opening on a blank screen.
    this.messages.set([{ role: 'ai', text: this.t[this.lang.current()].greeting }]);
  }

  ngAfterViewChecked(): void {
    if (this.shouldScroll) {
      this.scrollAnchor?.nativeElement.scrollIntoView({ behavior: 'smooth', block: 'end' });
      this.shouldScroll = false;
    }
  }

  send(): void {
    const text = this.draft.trim();
    if (!text || this.sending()) return;

    this.errorMessage.set('');
    this.messages.update(list => [...list, { role: 'user', text }]);
    this.draft = '';
    this.sending.set(true);
    this.shouldScroll = true;

    this.assistantService.sendMessage(text, this.selectedChildId(), this.lang.current()).subscribe({
      next: (res) => {
        this.messages.update(list => [...list, { role: 'ai', text: res.reply }]);
        this.sending.set(false);
        this.shouldScroll = true;
      },
      error: () => {
        this.errorMessage.set(this.t[this.lang.current()].errorMessage);
        this.sending.set(false);
      }
    });
  }

  // Enter sends, Shift+Enter still inserts a newline — the normal
  // convention for a chat composer.
  onEnter(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.send();
    }
  }

  private selectedChildId(): number | null {
    const id = localStorage.getItem('selectedChildId');
    return id ? Number(id) : null;
  }
}