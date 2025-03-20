import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

declare var bootstrap: any;

@Component({
  selector: 'app-voie-reponse',
  templateUrl: './voie-reponse.component.html',
  styleUrls: ['./voie-reponse.component.css']
})
export class VoieReponseComponent implements OnInit {
  private baseUrl = environment.apiUrl;
  sources: any[] = [];
  searchKeyword: string = "";
  newSource: any = { nomLangue2: "" };
  selectedSource: any = {};

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.getAll();
  }

  getAll(): void {
    
    this.http.get<any>(`${this.baseUrl}/voie-reponse`)
      .subscribe(res => {
        this.sources = res;
      });
  }

  search(): void {
    if (this.searchKeyword) {
      this.http.get<any>(`${this.baseUrl}/voie-reponse/search?keyword=${this.searchKeyword}`)
        .subscribe(res => {
          this.sources = res;
        });
    } else {
      this.getAll();
    }
  }

  openCreateModal(): void {
    this.newSource = { nomLangue2: "" };
    const modalElem = document.getElementById('createModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  create(): void {
    this.http.post<any>(`${this.baseUrl}/voie-reponse`, this.newSource)
      .subscribe(res => {
        this.getAll();
        const modalElem = document.getElementById('createModal');
        if (modalElem) {
          const modal = bootstrap.Modal.getInstance(modalElem);
          modal.hide();
        }
      });
  }

  openEditModal(source: any): void {
    this.selectedSource = { ...source };
    const modalElem = document.getElementById('editModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }

  update(): void {
    this.http.put<any>(`${this.baseUrl}/voie-reponse/${this.selectedSource.id}`, this.selectedSource)
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
      this.http.delete<any>(`${this.baseUrl}/voie-reponse/${id}`)
        .subscribe(() => {
          this.getAll();
        });
    }
  }
}
