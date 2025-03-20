import { ReclamationService,  VoieReponse } from '../../services/reclamation.service';
import { Component, OnInit } from '@angular/core';
declare var bootstrap: any; // Pour utiliser Bootstrap si vous l’incluez via script

@Component({
  selector: 'app-reclamation-retour-autorite',
  templateUrl: './reclamation-retour-autorite.component.html',
  styleUrl: './reclamation-retour-autorite.component.css'
})
export class ReclamationRetourAutoriteComponent implements OnInit {
  reclamations: any[] = [];
  voieReponses: VoieReponse[] = [];
  selectedReclamation: any;
  selectedVoieId: number | null = null;
  referenceRetour: string = '';
  dateRetourAutorite: string = new Date().toISOString().split('T')[0];
  message: string = '';

  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
    this.loadReclamations();

  }
  private appendSeconds(dateStr: string): string {
    // Si le string contient déjà un "T", vérifie s'il manque les secondes
    if (dateStr.includes('T')) {
      const timePart = dateStr.split('T')[1];
      if (timePart.split(':').length === 2) {
        return dateStr + ':00';
      }
      return dateStr;
    }
    // Sinon, ajoute "T00:00:00"
    return dateStr + 'T00:00:00';
  }
  
  loadReclamations(): void {
    this.reclamationService.getReclamationsByFlag('envoyeesAutorite')
      .subscribe(data => {
        this.reclamations = data;
      }, error => {
        console.error('Erreur lors de la récupération des réclamations', error);
      });
  }

  private loadVoies(): void {
    this.reclamationService.getVoieReponse()
      .subscribe(data => {
        this.voieReponses = data;
      }, error => {
        console.error('Erreur lors de la récupération des voies', error);
      });
  }

  // Ouvre la modale pour la réclamation sélectionnée
  openModal(rec: any): void {
    console.log('Réclamation sélectionnée :', rec);
    this.loadVoies();
    this.selectedReclamation = rec;
    this.selectedVoieId = null;
    this.referenceRetour = '';
    const modalEl = document.getElementById('retourAutoriteModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }

    // Vous pouvez utiliser Bootstrap (ou votre méthode habituelle) pour afficher la modale.
    // Par exemple, avec jQuery : $('#retourAutoriteModal').modal('show');
  }

  submitRetour(): void {
    const formattedDate = this.appendSeconds(this.dateRetourAutorite);

    if (!this.selectedVoieId) {
      this.message = 'Veuillez sélectionner une voie.';
      return;
    }
    const payload = {
      voieRetourAutoriteId: this.selectedVoieId,
      referenceRetourAutorite: this.referenceRetour,
      dateRetourAutorite: formattedDate
    };
    this.reclamationService.retourAutorite(this.selectedReclamation.id, payload)
      .subscribe(response => {
        this.message = 'Opération réussie';
        // Après 3 secondes, fermer la modale et rafraîchir la liste
        setTimeout(() => {
          this.message = '';
          // Fermer la modale (ex. avec Bootstrap)
          // $('#retourAutoriteModal').modal('hide');
          this.loadReclamations();
          this.cancel();
        }, 3000);
      }, error => {
        this.message = 'Erreur : ' + error;
        setTimeout(() => this.message = '', 3000);
      });
  }

  // Annule et ferme la modale
  cancel(): void {
    this.selectedReclamation = null;
    this.selectedVoieId = null;
    this.referenceRetour = '';
  
    const modalEl = document.getElementById('retourAutoriteModal');
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
    }

  }

}
