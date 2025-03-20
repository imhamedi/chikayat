import { Component, OnInit } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {
  users: any[] = [];
  filters: any = { nom: '', email: '', login: '', niveau: '', role: '' };
  message: string = "";
  selectedUser: any = {}; // ✅ Correction : initialiser avec un objet vide
  isViewingUser: boolean = false;
  isEditingUser: boolean = false;
  isCreatingUser: boolean = false;
  newUser: any = { nom: '', email: '', login: '', role: 'UTILISATEUR' };
  modalMessage: string = "";
  modalMessageType: string = "";

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.searchUsers();
  }

  searchUsers(): void {
    const filtersToSend: any = {};
    if (this.filters.nom) filtersToSend.nom = this.filters.nom;
    if (this.filters.email) filtersToSend.email = this.filters.email;
    if (this.filters.login) filtersToSend.login = this.filters.login;
    if (this.filters.niveau) filtersToSend.niveau = this.filters.niveau;
    if (this.filters.role) filtersToSend.role = this.filters.role;

    this.authService.getUsers(filtersToSend).subscribe({
      next: (data) => {
        console.log("✅ Utilisateurs récupérés :", data);
        this.users = data;
      },
      error: (err) => {
        console.error("❌ Erreur lors de la récupération des utilisateurs :", err);
      }
    });
  }

  deleteUser(id: number): void {
    if (confirm("Êtes-vous sûr de vouloir supprimer cet utilisateur ?")) {
      this.authService.deleteUser(id).subscribe({
        next: () => {
          this.message = "✅ Utilisateur supprimé avec succès.";
          this.searchUsers(); 
        },
        error: (err) => {
          console.error("❌ Erreur lors de la suppression de l'utilisateur :", err);
          this.message = "❌ Erreur lors de la suppression.";
        }
      });
    }
  }

  viewUser(user: any): void {
    this.selectedUser = { ...user };
    this.isViewingUser = true;
    this.isEditingUser = false;
  }

  editUser(user: any): void {
    this.selectedUser = { ...user }; // ✅ Remplit le formulaire avec les infos
    this.isEditingUser = true;
    this.isViewingUser = false;
    this.openEditModal(); // ✅ Ouvre la modale automatiquement
  }

  createUser(): void {
    this.authService.createUser(this.newUser).subscribe({
      next: (response: any) => {
        console.log("✅ Utilisateur créé :", response);
        
        // ✅ Affichage du message dans la modale
        this.modalMessage = response.message;
        this.modalMessageType = "alert-success";
  
        setTimeout(() => {
          this.modalMessage = "";
          this.closeCreateUserModal(); // ✅ Fermer la modale après succès
        }, 2000);
  
        this.searchUsers(); // ✅ Rafraîchir la liste des utilisateurs
      },
      error: (err) => {
        console.error("❌ Erreur lors de la création de l'utilisateur :", err);
        this.modalMessageType = "alert-danger";
  
        if (err.error) {
          try {
            const errorResponse = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
            this.modalMessage = `❌ ${errorResponse.error || "Erreur inconnue."}`;
          } catch (e) {
            this.modalMessage = "❌ Erreur lors de la création.";
          }
        } else {
          this.modalMessage = "❌ Erreur lors de la création.";
        }
      }
    });
  }
      
  saveUser(): void {
    this.authService.updateUser(this.selectedUser).subscribe({
      next: (response: any) => {
        console.log("✅ Réponse backend :", response);
  
        // ✅ Affichage du message de succès dans la modale
        this.modalMessage = "✅ " + response; // ✅ Afficher directement la réponse texte
        
        // ✅ Mettre à jour directement l'utilisateur dans la liste sans recharger la page
        this.users = this.users.map(user =>
          user.id === this.selectedUser.id ? { ...user, ...this.selectedUser } : user
        );
  
        // ✅ Fermer la modale après 2 secondes
        setTimeout(() => {
          this.modalMessage = "";
          this.closeModal('editUserModal'); // ✅ Fermer la modale proprement
          this.searchUsers(); // ✅ Rafraîchir la liste après mise à jour
        }, 2000);
      },
      error: (err: any) => {
        console.error("❌ Erreur lors de la mise à jour de l'utilisateur :", err);
  
        // ✅ Extraction propre du message d'erreur
        if (err.error) {
          try {
            this.modalMessage = `❌ ${typeof err.error === 'string' ? err.error : "Erreur lors de la mise à jour."}`;
          } catch (e) {
            this.modalMessage = "❌ Erreur lors de la mise à jour.";
          }
        } else {
          this.modalMessage = "❌ Erreur lors de la mise à jour.";
        }
  
        // ✅ Supprimer le message d'erreur après quelques secondes
        setTimeout(() => this.modalMessage = "", 3000);
      }
    });
  }
    
  /**
   * 🚀 **Ouvrir la modale d'édition**
   */
  openEditModal(): void {
    setTimeout(() => {
      const modalElement = document.getElementById('editUserModal');
      if (modalElement) {
        const modalInstance = new bootstrap.Modal(modalElement);
        modalInstance.show();
      }
    }, 200);
  }

  /**
   * 🚪 **Fermer les modales**
   */
  closeModal(modalId: string): void {
    const modalElement = document.getElementById(modalId);
    if (modalElement) {
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      modalInstance?.hide();
    }
  
    // ✅ Supprimer les backdrops restants
    document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
      backdrop.remove();
    });
  
    // ✅ Réinitialiser `selectedUser` après fermeture pour éviter les erreurs
    setTimeout(() => {
      this.selectedUser = null;
      this.isEditingUser = false;
    }, 500);
  }

  openCreateUserModal() {
    this.newUser = { nom: '', email: '', login: '', role: 'UTILISATEUR' }; // ✅ Réinitialisation du formulaire
    const modalElement = document.getElementById('createUserModal');
    if (modalElement) {
      const modal = new bootstrap.Modal(modalElement);
      modal.show();
    }
  }
  
  closeCreateUserModal() {
    const modalElement = document.getElementById('createUserModal');
    if (modalElement) {
      const modalInstance = bootstrap.Modal.getInstance(modalElement);
      modalInstance?.hide();
    }
  
    // ✅ Supprimer les backdrops qui restent affichés parfois
    document.querySelectorAll('.modal-backdrop').forEach(backdrop => {
      backdrop.remove();
    });
  
    // ✅ Réinitialiser les champs après fermeture
    this.newUser = { nom: '', email: '', login: '', role: 'UTILISATEUR' };
    this.modalMessage = "";
  }
  
    }
