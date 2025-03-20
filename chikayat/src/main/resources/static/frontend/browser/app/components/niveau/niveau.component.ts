import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

declare var bootstrap: any; 
@Component({
  selector: 'app-niveau',
  templateUrl: './niveau.component.html',
  styleUrls: ['./niveau.component.css']
})
export class NiveauComponent implements OnInit {
  private baseUrl = environment.apiUrl;

  niveaux: any[] = [];
  searchKeyword: string = "";
  newNiveau: any = { description: "" };
  selectedNiveau: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAllNiveaux();
  }

  getAllNiveaux(): void {
    this.http.get<any>(`${this.baseUrl}/niveaux`)
      .subscribe(res => {
        this.niveaux = res;
      });
  }

  searchNiveaux(): void {
    if (this.searchKeyword) {
      this.http.get<any>(`${this.baseUrl}/niveaux/search?keyword=${this.searchKeyword}`)
        .subscribe(res => {
          this.niveaux = res;
        });
    } else {
      this.getAllNiveaux();
    }
  }

  openCreateNiveauModal(): void {
    // Réinitialiser le niveau à créer
    this.newNiveau = { description: "" };
    const modalElem = document.getElementById('createNiveauModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  createNiveau(): void {
    this.http.post<any>(`${this.baseUrl}/niveaux`, this.newNiveau)
      .subscribe(res => {
        this.getAllNiveaux();
        const modalElem = document.getElementById('createNiveauModal');
        if (modalElem) {
          const modal = bootstrap.Modal.getInstance(modalElem);
          modal.hide();
        }
      });
  }

  openEditNiveauModal(niveau: any): void {
    this.selectedNiveau = { ...niveau };
    const modalElem = document.getElementById('editNiveauModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  updateNiveau(): void {
    this.http.put<any>(`${this.baseUrl}/niveaux/niveaux/${this.selectedNiveau.id}`, this.selectedNiveau)
      .subscribe(res => {
        this.getAllNiveaux();
        const modalElem = document.getElementById('editNiveauModal');
        if (modalElem) {
          const modal = bootstrap.Modal.getInstance(modalElem);
          modal.hide();
        }
      });
  }

  deleteNiveau(id: number): void {
    if (confirm("هل أنت متأكد من الحذف؟")) {
      this.http.delete<any>(`${this.baseUrl}/niveaux/niveaux/${id}`)
        .subscribe(() => {
          this.getAllNiveaux();
        });
    }
  }
}
