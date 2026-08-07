import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ChatReply {
  reply: string;
}


@Injectable({ providedIn: 'root' })
export class Assistant {
  private readonly baseUrl = 'http://localhost:8080/api/assistant';

  constructor(private http: HttpClient) {}

  sendMessage(message: string, childId?: number | null, lang?: 'en' | 'ar'): Observable<ChatReply> {
    return this.http.post<ChatReply>(`${this.baseUrl}/chat`, { message, childId, lang });
  }
}