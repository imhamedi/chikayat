import { Component, OnInit } from '@angular/core';
import { ReclamationService } from '../../services/reclamation.service';

@Component({
  selector: 'app-ouverture-reclamation',
  templateUrl: './ouverture-reclamation.component.html',
  styleUrls: ['./ouverture-reclamation.component.css']
})
export class OuvertureReclamationComponent implements OnInit {
  numInscription: string = '';
  referenceBo: string = '';
  reclamations: any[] = [];
  message: string = '';

  constructor(private reclamationService: ReclamationService) { }

  ngOnInit(): void {
  }

  search(): void {
    this.reclamationService.searchOuverture(this.numInscription, this.referenceBo).subscribe(
      data => {
        this.reclamations = data;
      },
      error => {
        console.error('Erreur lors de la recherche', error);
      }
    );
  }

  // Appelé lorsque l'utilisateur clique sur "إعادة الفتح" pour réouvrir une réclamation
  ouvrirReclamation(reclamationId: number): void {
    this.reclamationService.ouvertureReclamation(reclamationId).subscribe(
      res => {
        this.message = 'Réclamation réouverte';
        this.search(); // rafraîchir la liste
      },
      error => {
        console.error(error);
        this.message = 'Erreur lors de la réouverture';
      }
    );
  }
}
