import { Injectable } from '@angular/core';
import axios from 'axios';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ScanService {
  private API_URL = environment.apiUrl;

  constructor() { }

  async uploadScan(file: File, language: string, numInscription: string) {
    const formData = new FormData();
    formData.append('file', file);
    formData.append('language', language);
    formData.append('numInscription', numInscription);
    
    return axios.post(`${this.API_URL}/documents/extract-text`, formData);
  }
}
