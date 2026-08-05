import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

@Component({
  selector: 'app-oauth-callback',
  standalone: true,
  imports: [],
  templateUrl: './oauth-callback.html',
  styleUrl: './oauth-callback.css'
})
export class OauthCallback implements OnInit {
  constructor(
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const token = this.route.snapshot.queryParamMap.get('token');
    const refreshToken = this.route.snapshot.queryParamMap.get('refreshToken');

    if (token) {
      localStorage.setItem('token', token);
      if (refreshToken) {
        localStorage.setItem('refreshToken', refreshToken);
      }
      this.router.navigate(['/home']);
    } else {
      this.router.navigate(['/login']);
    }
  }
}