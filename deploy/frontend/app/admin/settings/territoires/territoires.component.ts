import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

declare var bootstrap: any;

@Component({
  selector: 'app-territoires',
  templateUrl: './territoires.component.html',
  styleUrls: ['./territoires.component.css']
})
export class TerritoiresComponent implements OnInit {
  private baseUrl = environment.apiUrl;

  territoires: any[] = [];
  searchCriteria: any = {
    codeVille: '',
    nomVilleLangue2: '',
    codeCommune: '',
    nomCommuneLangue2: '',
    codeArrondissement: '',
    nomArrondissementLangue2: ''
  };
  newTerritoire: any = {
    codeVille: "",
    nomVilleLangue2: "",
    codeCommune: "",
    nomCommuneLangue2: "",
    codeArrondissement: "",
    nomArrondissementLangue2: ""
  };
  selectedTerritoire: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAll();
  }

  getAll(): void {
    `${this.baseUrl}/territoires`
    this.http.get<any>(`${this.baseUrl}/territoires`)
      .subscribe(res => {
        this.territoires = res;
      });
  }

  search(): void {
    const criteria = Object.values(this.searchCriteria)
    .filter(val => typeof val === 'string' && (val as string).trim() !== '')
    .join(' ');
      if (criteria) {
      this.http.get<any>(`${this.baseUrl}/territoires/search?keyword=${criteria}`)
        .subscribe(res => {
          this.territoires = res;
        });
    } else {
      this.getAll();
    }
  }

  openCreateModal(): void {
    this.newTerritoire = {
      codeVille: "",
      nomVilleLangue2: "",
      codeCommune: "",
      nomCommuneLangue2: "",
      codeArrondissement: "",
      nomArrondissementLangue2: ""
    };
    const modalElem = document.getElementById('createModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  create(): void {
    this.http.post<any>(`${this.baseUrl}/territoires`, this.newTerritoire)
      .subscribe(res => {
        this.getAll();
        const modalElem = document.getElementById('createModal');
        if (modalElem) {
          const modal = bootstrap.Modal.getInstance(modalElem);
          modal.hide();
        }
      });
  }

  openEditModal(territoire: any): void {
    this.selectedTerritoire = { ...territoire };
    const modalElem = document.getElementById('editModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  update(): void {
    this.http.put<any>(`${this.baseUrl}/territoires/${this.selectedTerritoire.id}`, this.selectedTerritoire)
      .subscribe(res => {
        this.getAll();
        const modalElem = document.getElementById('editModal');
        if (modalElem) {
          const modal = bootstrap.Modal.getInstance(modalElem);
          modal.hide();
        }
      });
  }

  delete(id: number): void {
    if (confirm("هل أنت متأكد من الحذف؟")) {
      this.http.delete<any>(`${this.baseUrl}/territoires/${id}`)
        .subscribe(() => {
          this.getAll();
        });
    }
  }
}
