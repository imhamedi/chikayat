import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  profilePicture = 'assets/default-profile.png';
  message: string = ''; // ✅ Ajout de la variable message pour éviter l'erreur
  passwordRules: any = {}; // ✅ Ajout pour éviter l'erreur de type
  passwordForm: FormGroup;

  constructor(private authService: AuthService, private fb: FormBuilder) {
    this.profileForm = this.fb.group({
      nom: ['', Validators.required],
      login: [{ value: '', disabled: true }],
      email: ['', [Validators.required, Validators.email]],
      niveau: ['', Validators.required],
      role: [{ value: '', disabled: true }]
    });
    this.passwordForm = this.fb.group({
      oldPassword: ['', Validators.required],
      newPassword: ['', Validators.required],
      confirmPassword: ['', Validators.required]
    });
  
  }
  niveaux: any[] = [];

  ngOnInit(): void {
    this.authService.getUserProfile().subscribe(user => {
      this.profileForm.patchValue(user);
      if (user.profilePicture) {
        this.profilePicture = `uploads/profile_pictures/${user.profilePicture}`;
      }
    });


    this.authService.getNiveaux().subscribe({
      next: (data) => {
        this.niveaux = data;
      },
      error: () => {
        this.message = "⚠️ Impossible de récupérer les niveaux.";
      }
    });
  
    // ✅ Récupération des règles du mot de passe
    this.authService.getPasswordRules().subscribe({
      next: (rules) => {
        this.passwordRules = rules;
      },
      error: () => {
        this.message = "⚠️ Impossible de récupérer les règles du mot de passe.";
      }
    });
  }
  updateProfile() {
    this.authService.updateUserProfile(this.profileForm.value).subscribe({
      next: (response) => {
        console.log("✅ Réponse du backend :", response);
        if (response) {
          this.message = "✅ Profil mis à jour avec succès.";
            setTimeout(() => {
              this.message = "";
            }, 3000);
        } else {
          alert("❌ Une erreur inattendue s'est produite.");
        }
      },
      error: (err) => {
        if (err.status === 200) {
          this.message = "✅ Profil mis à jour avec succès.";
            setTimeout(() => {
              this.message = "";
            }, 3000);

        } else {
          alert("❌ Erreur lors de la mise à jour du profil.");
        }
      }
    });
  }
  showPasswordRules() {
    alert("📜 Règles du mot de passe :\n\n" +
      "- Minimum " + (this.passwordRules.longueurMin || "N/A") + " caractères\n" +
      "- Majuscules requises : " + (this.passwordRules.nbrMajuscules ? this.passwordRules.nbrMajuscules + " majuscule(s)" : "❌ Aucune") + "\n" +
      "- Chiffres obligatoires : " + (this.passwordRules.chiffresObligatoires ? "✅ Oui" : "❌ Non") + "\n" +
      "- Lettres spéciales obligatoires : " + (this.passwordRules.lettresSpecObligatoires ? "✅ Oui" : "❌ Non")
    );
  }
    

  uploadPicture(event: any) {
    const file = event.target.files[0];
    if (file) {
      this.authService.uploadProfilePicture(file).subscribe({
        next: (response) => {
          console.log("✅ Réponse backend photo :", response);
          this.message = "✅ Photo de profil mise à jour.";
          setTimeout(() => {
            this.message = "";
          }, 3000);
                
          // ✅ Mettre à jour immédiatement l'image de profil sans recharger la page
          this.profilePicture = `uploads/profile_pictures/${response.fileName}?t=${new Date().getTime()}`;
      
          // ✅ Mettre à jour aussi dans le header
          const headerImage = document.querySelector('.profile-icon img') as HTMLImageElement;
          if (headerImage) {
            headerImage.src = this.profilePicture;
          }
        },
                error: (err) => {
          console.error("❌ Erreur lors du chargement de la photo :", err);
        
          if (err.error) {
            try {
              const errorResponse = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
              this.modalMessage = `❌ ${errorResponse.error || "Erreur inconnue."}`;
            } catch (e) {
              this.modalMessage = "❌ Erreur lors du chargement de la photo.";
            }
          } else {
            this.modalMessage = "❌ Erreur lors du chargement de la photo.";
          }
        
          this.modalMessageType = "alert-danger";
        }
        
      });
    }
  }
    
  // ✅ Ajout de la méthode pour ouvrir le sélecteur de fichiers
  triggerFileInput() {
    const fileInput = document.getElementById('fileInput') as HTMLInputElement;
    if (fileInput) {
      fileInput.click();
    }
  }
  confirmPasswordChange() {
    const { oldPassword, newPassword, confirmPassword } = this.passwordForm.value;
  
    if (newPassword !== confirmPassword) {
      this.message = "❌ Les mots de passe ne correspondent pas.";
      return;
    }
  
    if (!this.isPasswordValid(newPassword)) {
      this.message = "❌ Le mot de passe ne respecte pas les règles.";
      return;
    }
  
    this.authService.changePassword(oldPassword, newPassword).subscribe({
      next: () => {
        this.message = "✅ Mot de passe changé avec succès.";
        this.passwordForm.reset();
      },
      error: () => {
        this.message = "❌ Erreur lors du changement du mot de passe.";
      }
    });
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
  oldPassword: string = "";
newPassword: string = "";
confirmPassword: string = "";

openPasswordModal() {
  const modalElement = document.getElementById('passwordModal');
  if (modalElement) {
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
  }
}

closePasswordModal() {
  document.getElementById("passwordModal")!.style.display = "none";
}

modalMessage: string = "";
modalMessageType: string = "";

changePassword() {
  if (this.oldPassword && this.newPassword && this.confirmPassword) {
    if (this.newPassword !== this.confirmPassword) {
      this.modalMessage = "❌ Les nouveaux mots de passe ne correspondent pas.";
      this.modalMessageType = "alert-danger";
      return;
    }

    if (!this.isPasswordValid(this.newPassword)) {
      this.modalMessage = "❌ Le mot de passe ne respecte pas les règles.";
      this.modalMessageType = "alert-danger";
      return;
    }

    this.authService.changePassword(this.oldPassword, this.newPassword).subscribe({
      next: () => {
        this.modalMessage = "✅ Mot de passe changé avec succès.";
        this.modalMessageType = "alert-success";

        setTimeout(() => {
          // ✅ Fermer la modale
          const modalElement = document.getElementById('passwordModal');
          if (modalElement) {
            const modalInstance = bootstrap.Modal.getInstance(modalElement);
            modalInstance?.hide();
          }

          // ✅ Supprimer les backdrops
          document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
            backdrop.remove();
          });

          // ✅ Réinitialiser les champs après fermeture
          this.oldPassword = "";
          this.newPassword = "";
          this.confirmPassword = "";
          this.modalMessage = "";

        }, 2000);
      },
      error: (err) => {
        console.error("❌ Erreur lors du changement de mot de passe :", err);
        
        // ✅ Extraction propre du message d'erreur
        if (err.error) {
          try {
            const errorResponse = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
            this.modalMessage = `❌ ${errorResponse.error || "Erreur inconnue."}`;
          } catch (e) {
            this.modalMessage = "❌ Erreur lors du changement du mot de passe.";
          }
        } else {
          this.modalMessage = "❌ Erreur lors du changement du mot de passe.";
        }

        this.modalMessageType = "alert-danger";
      }
    });
  } else {
    this.modalMessage = "⚠️ Tous les champs sont obligatoires.";
    this.modalMessageType = "alert-warning";
  }
}


ngAfterViewInit() {
  const modalElement = document.getElementById('passwordModal');
  if (modalElement) {
    modalElement.addEventListener('hidden.bs.modal', () => {
      // Réinitialiser les champs et messages quand la modale se ferme
      this.oldPassword = "";
      this.newPassword = "";
      this.confirmPassword = "";
      this.modalMessage = "";
    });
  }
  const backdrop = document.querySelector('.modal-backdrop');
  if (backdrop) {
    backdrop.remove();
  }

}

}
