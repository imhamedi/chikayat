import { ReclamationService,  VoieReponse } from '../../services/reclamation.service';
import { Component, OnInit } from '@angular/core';
declare var bootstrap: any; 

@Component({
  selector: 'app-reclamation-envoi-reclamant',
  templateUrl: './reclamation-envoi-reclamant.component.html',
  styleUrls: ['./reclamation-envoi-reclamant.component.css']
})
export class ReclamationEnvoiReclamantComponent implements OnInit {
  reclamations: any[] = [];
  voieReponses: VoieReponse[] = [];
  selectedReclamation: any;
  selectedVoieId: number | null = null;
  referenceEnvoi: string = '';
  observation2: string = '';
  message: string = '';
  dateEnvoiReclamant: string = new Date().toISOString().split('T')[0];

  // Propriétés de pagination
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  pageSizeOptions: number[] = [5, 10, 20, 50];
  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
    this.loadReclamations();

  }
  private loadVoies(): void {
    this.reclamationService.getVoieReponse()
      .subscribe(data => {
        this.voieReponses = data;
      }, error => {
        console.error('Erreur lors de la récupération des voies', error);
      });
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
    this.reclamationService.getReclamationsByFlag('reponduAutorite')
      .subscribe(data => {
        this.reclamations = data;
      }, error => {
        console.error('Erreur lors de la récupération des réclamations', error);
      });
  }


  openModal(rec: any): void {
    console.log('Réclamation sélectionnée :', rec);
    this.loadVoies();
    this.selectedReclamation = rec;
    this.selectedVoieId = null;
    this.referenceEnvoi = '';    
    this.observation2 = '';

    const modalEl = document.getElementById('envoiReclamantModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }
  }

  submitEnvoi(): void {
    const formattedDate = this.appendSeconds(this.dateEnvoiReclamant);

    if (!this.selectedVoieId) {
      this.message = 'Veuillez sélectionner une voie.';
      return;
    }
    const payload = {
      voieEnvoiReclamantId: this.selectedVoieId,
      referenceEnvoiReclamant: this.referenceEnvoi,
      observation2: this.observation2,
      dateEnvoiReclamant: formattedDate
    };
    this.reclamationService.envoiReclamant(this.selectedReclamation.id, payload)
      .subscribe(response => {
        this.message = 'Opération réussie';
        setTimeout(() => {
          this.message = '';
          // $('#envoiReclamantModal').modal('hide');
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
    this.selectedVoieId = null;
    this.referenceEnvoi = '';
    this.observation2 = '';
  
    const modalEl = document.getElementById('envoiReclamantModal');
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
    }

  }

}
