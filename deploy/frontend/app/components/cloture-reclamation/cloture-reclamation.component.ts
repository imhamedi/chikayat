import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../services/reclamation.service';

@Component({
  selector: 'app-cloture-reclamation',
  templateUrl: './cloture-reclamation.component.html',
  styleUrls: ['./cloture-reclamation.component.css']
})
export class ClotureReclamationComponent implements OnInit {
  numInscription: string = '';
  referenceBo: string = '';
  reclamations: any[] = [];
  message: string = '';

  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
  }

  search(): void {
    this.reclamationService.searchCloture(this.numInscription, this.referenceBo).subscribe(
      data => {
        this.reclamations = data;
      },
      error => {
        console.error('Erreur lors de la recherche', error);
      }
    );
  }

  cloturerReclamation(reclamationId: number): void {
    this.reclamationService.clotureReclamation(reclamationId).subscribe(
      res => {
        this.message = 'Réclamation clôturée';
        this.search(); 
      },
      error => {
        console.error(error);
        this.message = 'Erreur lors de la clôture';
      }
    );
  }
}
