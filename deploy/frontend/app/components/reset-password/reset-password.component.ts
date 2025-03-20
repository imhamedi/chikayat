import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {
  resetPasswordForm: FormGroup;
  emailForm: FormGroup;
  token: string | null = null;
  isTokenValid = false;
  message = '';
  passwordRules: any = {};

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private fb: FormBuilder
  ) {
    this.emailForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.resetPasswordForm = this.fb.group({
      newPassword: ['', [Validators.required]],
      confirmPassword: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.token = params['token'] || null;
      this.isTokenValid = !!this.token;
    });

    this.authService.getPasswordRules().subscribe({
      next: (rules) => {
        if (!rules || Object.keys(rules).length === 0) {
          this.message = "⚠️ Impossible de récupérer les règles du mot de passe.";
          console.warn("⚠️ Réponse vide de l'API !");
        } else {
          this.passwordRules = rules;
          console.log("📜 Règles du mot de passe récupérées :", this.passwordRules);

          this.resetPasswordForm.controls['newPassword'].setValidators([
            Validators.required,
            Validators.minLength(parseInt(rules.longueurMin, 10) || 8)
          ]);
          this.resetPasswordForm.controls['newPassword'].updateValueAndValidity();
        }
      },
      error: (err) => {
        console.error("❌ Erreur API :", err);
        this.message = "⚠️ Impossible de récupérer les règles du mot de passe.";
      }
    });
  }

  requestPasswordReset() {
    if (this.emailForm.valid) {
      const email = this.emailForm.value.email;
      this.authService.requestPasswordReset(email).subscribe({
        next: (response) => {
          console.log("✅ Réponse du backend :", response);
          if (response && response.status === 200) {
            this.message = "📧 Un email de réinitialisation a été envoyé avec succès.";
          } else {
            this.message = "❌ Une erreur inattendue s'est produite.";
          }
        },
        error: (err) => {
          if (err.status === 200) {
            this.message = "📧 Un email de réinitialisation a été envoyé avec succès.";
          } else {
            this.message = "❌ Erreur lors de la demande de réinitialisation.";
          }
        }
      });
    }
  }
  
  resetPassword() {
    if (this.resetPasswordForm.valid && this.token) {
      const { newPassword, confirmPassword } = this.resetPasswordForm.value;
  
      if (newPassword !== confirmPassword) {
        this.message = "❌ Les mots de passe ne correspondent pas.";
        return;
      }
  
      if (!this.isPasswordValid(newPassword)) {
        this.message = "❌ Le mot de passe ne respecte pas les règles.";
        return;
      }
  
      this.authService.resetPassword(this.token, newPassword).subscribe({
        next: (response) => {
          console.log("✅ Réponse du backend :", response);
          if (response) {
            this.message = "✅ Mot de passe modifié avec succès !";
            console.log("🔄 Redirection vers /login dans 3 secondes...");

            setTimeout(() => {
              this.router.navigate(['/login']).then(() => {
                console.log("✅ Redirection effectuée vers /login !");
              }).catch(err => {
                console.error("❌ Erreur de redirection :", err);
              });
            }, 3000);
          } else {
            this.message = "❌ Erreur lors de la réinitialisation du mot de passe.";
          }
        },
        error: (err) => {
          console.error("❌ Erreur API lors de la réinitialisation :", err);
          if (err.status === 200) {
            this.message = "✅ Mot de passe modifié avec succès.";
            setTimeout(() => {
              this.router.navigate(['/login']).then(() => {
                console.log("✅ Redirection effectuée vers /login !");
              }).catch(err => {
                console.error("❌ Erreur de redirection :", err);
              });
            }, 3000);
          } else {
            this.message = "❌ Erreur lors de la réinitialisation du mot de passe.";
          }
        }
      });
    } else {
      console.warn("⚠️ Le formulaire est invalide.");
      this.message = "⚠️ Les mots de passe doivent correspondre et être valides.";
    }
  }
  
  isPasswordValid(password: string): boolean {
    if (!this.passwordRules) return false;
    
    const minLength = parseInt(this.passwordRules.longueurMin || "8", 10);
    const uppercaseRequired = parseInt(this.passwordRules.nbrMajuscules || "0", 10);
    const numbersRequired = this.passwordRules.chiffresObligatoires === true;
    const specialCharsRequired = this.passwordRules.lettresSpecObligatoires === true;

    if (password.length < minLength) return false;
    if ((password.match(/[A-Z]/g) || []).length < uppercaseRequired) return false;
    if (numbersRequired && !/\d/.test(password)) return false;
    if (specialCharsRequired && !/[!@#$%^&*(),.?":{}|<>]/.test(password)) return false;

    return true;
  }

  showPasswordRules() {
    if (!this.passwordRules || Object.keys(this.passwordRules).length === 0) {
      alert("⚠️ Impossible de récupérer les règles du mot de passe.");
      return;
    }

    alert("📜 Règles du mot de passe :\n\n" +
      "- Minimum " + (this.passwordRules.longueurMin || "N/A") + " caractères\n" +
      "- Majuscules requises : " + (this.passwordRules.nbrMajuscules ? this.passwordRules.nbrMajuscules + " majuscule(s)" : "❌ Aucune") + "\n" +
      "- Chiffres obligatoires : " + (this.passwordRules.chiffresObligatoires ? "✅ Oui" : "❌ Non") + "\n" +
      "- Lettres spéciales obligatoires : " + (this.passwordRules.lettresSpecObligatoires ? "✅ Oui" : "❌ Non")
      );
  }
}
