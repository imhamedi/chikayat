import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  login = '';
  pass = '';
  errorMessage = '';

  constructor(private authService: AuthService, private router: Router) {}

  async authenticate() {
    try {
        console.log("🔍 Tentative de connexion avec :", this.login, this.pass);

        const token = await this.authService.login(this.login, this.pass);

        if (token) {
            this.authService.saveToken(token);
            this.router.navigate(['/home']);
        } else {

            console.warn("⚠️ Aucun token reçu, identifiants incorrects.");
            this.errorMessage = 'Identifiants incorrects';
        }
    } catch (error: any) {
        console.error("❌ Erreur de connexion :", error);
        this.errorMessage = error.message || "Erreur de connexion.";
    }
}
  
}
