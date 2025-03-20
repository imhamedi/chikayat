import { Component, HostListener, OnInit } from '@angular/core';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';
import { ReclamationService } from './services/reclamation.service';


@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  isAuthenticated = false;
  isResetPasswordPage: boolean = false;

  constructor(public authService: AuthService,private router: Router) {
    this.router.events.subscribe(() => {
      this.isResetPasswordPage = this.router.url.includes('/reset-password');
    });

  }

  ngOnInit() {
    this.isAuthenticated = this.authService.isAuthenticated();
    this.authService.authStatus.subscribe((status: boolean) => {
      this.isAuthenticated = status;
    });

    // Vérification après un petit délai pour forcer la mise à jour
    setTimeout(() => {
      this.isAuthenticated = this.authService.isAuthenticated();
    }, 1000);
  }

}
