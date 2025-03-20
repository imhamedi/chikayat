import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../environments/environment';

export interface Page<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

export interface SourceReclamation {
  id: number;
  nomLangue2: string;  // Note: this matches what your backend sends
}
export interface TypeRequete {
  id: number;
  nomLangue2: string;  // Note: this matches what your backend sends
}
export interface TypeReclamation {
  id: number;
  nomLangue2: string;  // Note: this matches what your backend sends
}
export interface TypeDestinataire {
  id: number;
  nomLangue2: string;  // Note: this matches what your backend sends
}
export interface UserLogin {
  id: number;
  login: string;  // Note: this matches what your backend sends
}
export interface VoieReponse {
  id: number;
  nomLangue2: string;  // Note: this matches what your backend sends
}
export interface Reclamation {

  id?: number;
  dateDepot: string;
  dateInscription: string;
  numInscription: string;
  typeIdentifiant: string;
  identifiant: string;
  nomComplet: string;
  commune: string;
  adresse?: string;
  telephone?: string;
  referenceBo: string;
  typeRequete: { id: number };
  typeReclamation: { id: number };
  objetReclamation: string;
  instructionsGouverneur: string;
  annee: number;
  typeDestinataire: { id: number };
  voieReponse: { id: number };
  userLogin: { id: number };
  destinataire: string;
  observation1?: string;
  // autres champs éventuels...
  userSaisie: { id: number };
  voieEnvoiAutorite?: VoieReponse;
  voieRetourAutorite?: VoieReponse;
  voieEnvoiReclamant?: VoieReponse;



}

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private baseUrl = environment.apiUrl;
  private apiUrl = `${this.baseUrl}/reclamations`;

  constructor(private http: HttpClient) { }

  private getAuthHeaders(): HttpHeaders {
    const token = localStorage.getItem('token');
    return new HttpHeaders({ 'Authorization': `Bearer ${token}` });
  }

  createReclamation(reclamation: Reclamation): Observable<Reclamation> {
    const headers = this.getAuthHeaders().set('Content-Type', 'application/json');
    return this.http.post<Reclamation>(this.apiUrl, reclamation, { headers })
      .pipe(catchError(this.handleError));
  }

  uploadFile(reclamationId: number, file: File): Observable<any> {
    const url = `${this.apiUrl}/${reclamationId}/upload-file`;
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    const headers = this.getAuthHeaders();
    return this.http.post(url, formData, { headers })
      .pipe(catchError(this.handleError));
  }

  generateNumInscription(): Observable<string> {
    const url = `${this.apiUrl}/generate-num-inscription`;
    const headers = this.getAuthHeaders();
    return this.http.get(url, { headers, responseType: 'text' })
      .pipe(catchError(this.handleError));
  }

  getCommunes(): Observable<string[]> {
    const url = `${this.apiUrl}/territoires/nom_commune_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<string[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }
  getTypeRequete(): Observable<TypeRequete[]> {
    const url = `${this.apiUrl}/type-requete/nom_requete_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<TypeRequete[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }
  getTypeReclamation(): Observable<TypeReclamation[]> {
    const url = `${this.apiUrl}/type-reclamation/nom_reclamation_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<TypeReclamation[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }
  getTypeDestinataire(): Observable<TypeDestinataire[]> {
    const url = `${this.apiUrl}/type-destinataire/nom_destinataire_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<TypeDestinataire[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }  
  getUserLogin(): Observable<UserLogin[]> {
    const url = `${this.apiUrl}/utilisateurs/login`;
    const headers = this.getAuthHeaders();
    return this.http.get<UserLogin[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }  
    getVoieReponse(): Observable<VoieReponse[]> {
    const url = `${this.apiUrl}/voie-reponse/nom_voie_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<VoieReponse[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }  
  getSourceReclamation(): Observable<SourceReclamation[]> {
    const url = `${this.apiUrl}/source-reclamation/nom_source_langue_2`;
    const headers = this.getAuthHeaders();
    return this.http.get<SourceReclamation[]>(url, { headers })
      .pipe(catchError(this.handleError));
  }  


  getVoieEnvoiAutorite(): Observable<string[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<string[]>(`${this.apiUrl}/voie-envoi-autorite`, { headers });
  }

  getVoieRetourAutorite(): Observable<string[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<string[]>(`${this.apiUrl}/voie-retour-autorite`, { headers });
  }

  getVoieEnvoiReclamant(): Observable<string[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<string[]>(`${this.apiUrl}/voie-envoi-reclamant`, { headers });
  }

  getUtilisateurs(): Observable<string[]> {
    const headers = this.getAuthHeaders();

    return this.http.get<string[]>(`${this.apiUrl}/utilisateurs`, { headers });
  }
  private handleError(error: any) {
    let errorMessage = 'Une erreur est survenue.';
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erreur : ${error.error.message}`;
    } else {
      errorMessage = `Erreur Code : ${error.status}\nMessage: ${error.message}`;
    }
    return throwError(errorMessage);
  }
  releaseNumInscription(numInscription: string): Observable<any> {
    const url = `${this.apiUrl}/release-num-inscription`;
    const headers = this.getAuthHeaders();
    return this.http.post(url, { numInscription }, { headers });
  }

  searchReclamations(filters: any): Observable<any[]> {
    let params = new HttpParams();
    Object.keys(filters).forEach(key => {
      if (filters[key] !== null && filters[key] !== '') {
        params = params.set(key, filters[key]);
      }
    });
    const headers = this.getAuthHeaders();
    return this.http.get<any[]>(this.apiUrl, { params, headers });
  }

  updateReclamation(id: number, reclamation: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.put(`${this.apiUrl}/${id}`, reclamation, { headers });
  }

  deleteReclamation(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/${id}`, { 
	headers: this.getAuthHeaders(),
    responseType: 'text' 
	});
  }


  getReclamationById(id: number): Observable<any> {
    const headers = this.getAuthHeaders();

    return this.http.get<any>(`${this.apiUrl}/${id}`, { headers });
  }
  getStats(durationType?: string, durationValue?: number): Observable<any> {
    const headers = this.getAuthHeaders();

    let params = new HttpParams();
    if (durationType && durationValue) {
      params = params.set('durationType', durationType)
                     .set('durationValue', durationValue.toString());
    }
    return this.http.get<any>(`${this.apiUrl}/stats`, { headers, params });
  }
  getReclamationsByFlag(flag: string): Observable<Reclamation[]> {
    // Adaptation de l'URL selon votre endpoint backend qui filtre selon le flag
    const headers = this.getAuthHeaders();

    return this.http.get<Reclamation[]>(`${this.apiUrl}/filter?flag=${flag}`, { headers });
  }
  envoiAutorite(id: number, payload: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(`${this.apiUrl}/envoiAutorite/${id}`, payload, { headers })
      .pipe(catchError(this.handleError));
  }
  retourAutorite(id: number, payload: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(`${this.apiUrl}/retourAutorite/${id}`, payload, { headers })
      .pipe(catchError(this.handleError));
  }
  envoiReclamant(id: number, payload: any): Observable<any> {
    const headers = this.getAuthHeaders();
    return this.http.post<any>(`${this.apiUrl}/envoiReclamant/${id}`, payload, { headers })
      .pipe(catchError(this.handleError));
  }
  searchReclamationsPage(params: any): Observable<{ content: Reclamation[], totalPages: number, totalElements: number, page: number, size: number }> {
    const headers = this.getAuthHeaders();
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      if (params[key] !== null && params[key] !== undefined) {
        httpParams = httpParams.set(key, params[key]);
      }
    });
    return this.http.get<{ content: Reclamation[], totalPages: number, totalElements: number, page: number, size: number }>(this.apiUrl, { params: httpParams, headers });
  }

  // Méthode pour l'export Excel
  exportExcel(params: any): Observable<Blob> {
    const headers = this.getAuthHeaders();
    let httpParams = new HttpParams();
    Object.keys(params).forEach(key => {
      // On ajoute uniquement les clés dont la valeur n'est ni null ni undefined ni une chaîne vide
      if (params[key] !== null && params[key] !== undefined && params[key].toString().trim() !== '') {
        httpParams = httpParams.set(key, params[key]);
      }
    });
    return this.http.get(`${this.apiUrl}/export`, { params: httpParams, headers, responseType: 'blob' });
  }
    
}
