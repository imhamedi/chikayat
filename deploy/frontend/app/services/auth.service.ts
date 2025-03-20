import { Injectable } from '@angular/core';
import axios, { AxiosError } from 'axios';
import { BehaviorSubject, Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private API_URL = environment.apiUrl;
  private authStatusSubject = new BehaviorSubject<boolean>(this.isAuthenticated());
  authStatus = this.authStatusSubject.asObservable();

  constructor(private http: HttpClient) {}

  async login(login: string, pass: string) {
    try {
        console.log("📡 Envoi de la requête avec :", { login, pass });

        const response = await axios.post(`${this.API_URL}/utilisateurs/login`, { login, pass });


        const token = response.data; // ✅ Extraction correcte du token
        if (token) {
            this.saveToken(token);
            this.authStatusSubject.next(true);
            return token;
        } else {
            console.warn("⚠️ Aucun token reçu !");
            return null;
        }
            } catch (error: any) {
        console.error("❌ Erreur reçue du backend :", error.response?.data || error);
        throw error.response?.data?.message || 'Erreur de connexion';
    }
}
private getAuthHeaders(): HttpHeaders {
  const token = localStorage.getItem('token');
  return new HttpHeaders({
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  });
}

  saveToken(token: string) {
    localStorage.setItem('token', token);
  }

  isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    return token !== null && token.length > 10; // Vérification minimale
}

  logout() {
    localStorage.removeItem('token');
    this.authStatusSubject.next(false);
  }

  requestPasswordReset(email: string): Observable<any>  {
    return this.http.post(`${this.API_URL}/utilisateurs/forgot-password`, { email });

}


resetPassword(token: string, newPassword: string): Observable<any> {
  return this.http.post(`${this.API_URL}/utilisateurs/reset-password`, { token, newPassword });
}
getPasswordRules(): Observable<any> {
  return this.http.get<any>(`${this.API_URL}/utilisateurs/password-rules`);
}
getUserProfile(): Observable<any> {
  return this.http.get(`${this.API_URL}/utilisateurs/me`, { headers: this.getAuthHeaders() });
}

updateUserProfile(data: any): Observable<any> {
  return this.http.put(`${this.API_URL}/utilisateurs/update-profile`, data, { headers: this.getAuthHeaders() });

}

changePassword(oldPassword: string, newPassword: string): Observable<any> {
  return this.http.put(`${this.API_URL}/utilisateurs/change-password`, 
    { oldPassword, newPassword }, 
    { headers: this.getAuthHeaders(), responseType: 'text' as 'json' } //  Interpret response as text
  );
}

uploadProfilePicture(file: File): Observable<any> {
  const formData = new FormData();
  formData.append("file", file);
  return this.http.post(`${this.API_URL}/utilisateurs/upload-profile-picture`, formData, { headers: this.getAuthHeaders() });
}
getNiveaux(): Observable<any> {
  return this.http.get(`${this.API_URL}/niveaux`);
}
getUsers(filters: any): Observable<any> {
  return this.http.get(`${this.API_URL}/admin/users`, {
    headers: this.getAuthHeaders(),
    params: filters
  });
}


deleteUser(id: number): Observable<any> {
  return this.http.delete(`${this.API_URL}/admin/delete-user/${id}`, {
    headers: this.getAuthHeaders(),
    responseType: 'text' //  Évite l'erreur de parsing JSON
  });
}
createUser(user: any): Observable<any> {
  return this.http.post(`${this.API_URL}/admin/create-user`, user, {
    headers: this.getAuthHeaders(),
    responseType: 'text' //  Évite l'erreur de parsing JSON
  });
}
updateUser(user: any): Observable<any> {
  return this.http.put(`${this.API_URL}/admin/update-user/${user.id}`, user, {
    headers: this.getAuthHeaders(),
    responseType: 'text' //  Accepte une réponse en texte pour éviter l'erreur JSON
  });
}

}
