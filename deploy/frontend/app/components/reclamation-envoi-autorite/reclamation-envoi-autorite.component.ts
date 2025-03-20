import { ReclamationService,  VoieReponse } from '../../services/reclamation.service';
import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';

declare var bootstrap: any; 

@Component({
  selector: 'app-reclamation-envoi-autorite',
  templateUrl: './reclamation-envoi-autorite.component.html',
  styleUrls: ['./reclamation-envoi-autorite.component.css']
})
export class ReclamationEnvoiAutoriteComponent implements OnInit {
  form: FormGroup | undefined;
  reclamations: any[] = [];
  selectedReclamation: any;
  selectedTypeDestinataire: number | null = null;
  typeDestinataires: any[] = [];
  referenceEnvoi: string = '';
  message: string = '';
  dateEnvoiAutorite: string = new Date().toISOString().split('T')[0];
  currentPage: number = 0;
  pageSize: number = 10;
  totalPages: number = 0;
  pageSizeOptions: number[] = [5, 10, 20, 50];


  constructor(private reclamationService: ReclamationService, private fb: FormBuilder) { }

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
  

  loadTypeDestinataires(): void {
    this.reclamationService.getTypeDestinataire().subscribe(
      (data: any[]) => {
        this.typeDestinataires = data;
      },
      (error) => {
        console.error('Erreur lors du chargement des types destinataires', error);
      }
    );
  }

  openModal(rec: any): void {
    console.log('Réclamation sélectionnée :', rec);
    this.form = this.fb.group({
      typeDestinataire: [null, Validators.required],
    });
    this.loadTypeDestinataires();
    this.selectedReclamation = rec;
    this.selectedTypeDestinataire = null;
    this.referenceEnvoi = '';
    const modalEl = document.getElementById('envoiAutoriteModal');
    if (modalEl) {
      const modalInstance = new bootstrap.Modal(modalEl);
      modalInstance.show();
    }
  }

  submitEnvoi(): void {
    const selectedType = this.form!.get('typeDestinataire')?.value;
    const formattedDate = this.appendSeconds(this.dateEnvoiAutorite);
    console.log('Type destinataire sélectionné:', this.selectedTypeDestinataire);

    if (!this.selectedTypeDestinataire) {
      this.message = "Veuillez sélectionner un type de destinataire.";
      return;
    }
    const payload = {
      idTypeDestinataire: this.selectedTypeDestinataire,
      referenceEnvoiAutorite: this.referenceEnvoi,
      dateEnvoiAutorite: formattedDate
    };
    this.reclamationService.envoiAutorite(this.selectedReclamation.id, payload)
      .subscribe(response => {
        this.message = 'Enregistrement réussi';
         setTimeout(() => {
          this.message = '';
        }, 3000); 
      }, error => {
        this.message = 'Erreur : ' + error;
        setTimeout(() => this.message = '', 3000);
      });
      this.loadTypeDestinataires();
      this.selectedTypeDestinataire = null;
      this.referenceEnvoi = '';
  
  }

  cancel(): void {
    this.selectedReclamation = null;
    this.selectedTypeDestinataire = null;
    this.referenceEnvoi = '';
  
    const modalEl = document.getElementById('envoiAutoriteModal');
    if (modalEl) {
      const modalInstance = bootstrap.Modal.getInstance(modalEl);
      modalInstance.hide();
    }

  }

  loadReclamations(): void {
    this.reclamationService.getReclamationsByFlag('enCours')
      .subscribe(data => {
        this.reclamations = data;
      }, error => {
        console.error('Erreur lors de la récupération des réclamations', error);
      });
  }

}
