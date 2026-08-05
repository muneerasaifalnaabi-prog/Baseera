import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Center } from '../models/center.model';

@Injectable({
  providedIn: 'root'
})
export class CenterService {

  private http = inject(HttpClient);

  private apiUrl = 'http://localhost:8080/api/centers';

  getCenters(): Observable<Center[]> {
    return this.http.get<Center[]>(this.apiUrl);
  }

  getRecommendedCenters(
    childId: number
  ): Observable<Center[]> {

    return this.http.get<Center[]>(
      `http://localhost:8080/api/children/${childId}/centers/recommended`
    );

  }

}
