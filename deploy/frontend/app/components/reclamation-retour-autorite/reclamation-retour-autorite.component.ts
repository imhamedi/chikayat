import { ReclamationService,  VoieReponse } from '../../services/reclamation.service';
import { Component, OnInit } from '@angular/core';
declare var bootstrap: any; 

@Component({
  selector: 'app-reclamation-retour-autorite',
  templateUrl: './reclamation-retour-autorite.component.html',
  styleUrl: './reclamation-retour-autorite.component.css'
})
export class ReclamationRetourAutoriteComponent implements OnInit {
  reclamations: any[] = [];
  selectedReclamation: any;
  referenceRetour: string = '';
  dateRetourAutorite: string = new Date().toISOString().split('T')[0];
  message: string = '';
  selectedDetail: any; 
  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
    this.loadReclamations();

  }
  private appendSeconds(dateStr: string): string {
    if (dateStr.includes('T')) {
      const timePart = dateStr.split('T')[1];
      if (timePart.split(':').length === 2) {
        return dateStr + ':00';
      }
      return dateStr;
    }
    return dateStr + 'T00:00:00';
  }
  
  loadReclamations(): void {
    this.reclamationService.getReclamationsPourRetourAvecDetails()
    .subscribe(data => {
      this.reclamations = data;
    }, error => {
      console.error("Erreur lors du chargement des réclamations avec détails", error);
    });
  }

  // Ouvre la modale pour la réclamation sélectionnée
  openModal(rec: any): void {
    console.log('Réclamation sélectionnée :', rec);
    this.selectedReclamation = rec;
    this.referenceRetour = '';
    const modalEl = document.getElementById('retourAutoriteModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }

  }
  openModalForDetail(detail: any, rec: any): void {
    this.selectedDetail = detail;
    this.selectedReclamation = rec;
    this.referenceRetour = '';
    this.dateRetourAutorite = new Date().toISOString().split('T')[0];
    this.message = '';

    const modalEl = document.getElementById('retourAutoriteModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }
  }

  submitRetour(): void {
    if (!this.selectedDetail || !this.selectedReclamation) {
      this.message = "Erreur: aucun détail sélectionné.";
      return;
    }
  
    const formattedDate = this.appendSeconds(this.dateRetourAutorite);

    const payload = {
      referenceRetourAutorite: this.referenceRetour,
      dateRetourAutorite: formattedDate
    };
    this.reclamationService.retourAutorite(this.selectedReclamation.id,this.selectedDetail.id, payload)
      .subscribe(response => {
        this.message = 'Opération réussie';
        setTimeout(() => {
          this.message = '';
          // $('#retourAutoriteModal').modal('hide');
          this.loadReclamations();
          this.cancel();
        }, 3000);
      }, error => {
        this.message = 'Erreur : ' + error;
        setTimeout(() => this.message = '', 3000);
      });
  }

  cancel(): void {
    this.selectedReclamation = null;
    this.referenceRetour = '';
  
    const modalEl = document.getElementById('retourAutoriteModal');
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
    }

  }

}
