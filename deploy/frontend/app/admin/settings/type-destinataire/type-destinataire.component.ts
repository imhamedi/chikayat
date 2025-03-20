import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../environments/environment';

declare var bootstrap: any;

@Component({
  selector: 'app-type-destinataire',
  templateUrl: './type-destinataire.component.html',
  styleUrls: ['./type-destinataire.component.css']
})
export class TypeDestinataireComponent implements OnInit {
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
    
    this.http.get<any>(`${this.baseUrl}/type-destinataire`)
      .subscribe(res => {
        this.sources = res;
      });
  }

  search(): void {
    if (this.searchKeyword) {
      this.http.get<any>(`${this.baseUrl}/type-destinataire/type-destinataire/search?keyword=${this.searchKeyword}`)
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
    this.http.post<any>(`${this.baseUrl}/type-destinataire`, this.newSource)
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
    this.http.put<any>(`${this.baseUrl}/type-destinataire/${this.selectedSource.id}`, this.selectedSource)
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
      this.http.delete<any>(`${this.baseUrl}/type-destinataire/${id}`)
        .subscribe(() => {
          this.getAll();
        });
    }
  }
}
