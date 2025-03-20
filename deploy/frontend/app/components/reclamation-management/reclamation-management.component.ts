import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Page, Reclamation, ReclamationService, SourceReclamation, TypeDestinataire, TypeReclamation, TypeRequete, UserLogin, VoieReponse } from '../../services/reclamation.service';

@Component({
  selector: 'app-reclamation-management',
  templateUrl: './reclamation-management.component.html',
  styleUrls: ['./reclamation-management.component.css']
})
export class ReclamationManagementComponent implements OnInit {
  searchForm: FormGroup;
  reclamations: any[] = [];
  selectedReclamation: any = null;
  isConsultation: boolean = false;
  isEditing: boolean = false;

  communes: string[] = [];
  typeRequetes: TypeRequete[] = [];
  typeReclamations: TypeReclamation[] = [];
  sourceReclamations: SourceReclamation[] = [];
  typeDestinataires: TypeDestinataire[] = [];
  userLogins: UserLogin[] = [];
  voieReponses: VoieReponse[] = [];   
public currentPage: number = 0;
public pageSize: number = 10;
public totalPages: number = 0;
public pageSizeOptions: number[] = [5, 10, 20, 50];

  constructor(private fb: FormBuilder, private reclamationService: ReclamationService) {
    this.searchForm = this.fb.group({
      dateDepotStart: [''],
      dateDepotEnd: [''],
      dateInscriptionStart: [''],
      dateInscriptionEnd: [''],
      numInscription: [''],
      referenceBo: [''],
      identifiant: [''],
      nomComplet: [''],
      commune: [''],
      typeReclamation: [''],
      objetReclamation: [''],
      annee: [''],
      typeRequete: [''],
      sourceReclamation: [''],
      telephone: [''],
      instructionsGouverneur: [''],
      observation1: [''],
      observation2: [''],
      userSaisie: [''],
      flagCloture: [''],
      flagEnvoiAutorite: [null],
      dateEnvoiAutoriteStart: [''],
      dateEnvoiAutoriteEnd: [''],
      referenceEnvoiAutorite: [''],
      flagRetourAutorite: [null],
      dateRetourAutoriteStart: [''],
      dateRetourAutoriteEnd: [''],
      referenceRetourAutorite: [''],
      flagEnvoiReclamant: [null],
      dateEnvoiReclamantStart: [''],
      dateEnvoiReclamantEnd: [''],
      voieEnvoiReclamant: [''],
      referenceEnvoiReclamant: [''],
      userEnvoiAutorite: [''],
      userRetourAutorite: [''],
      userEnvoiReclamant: ['']


    });
  }
  loadStaticData(): void {
    this.reclamationService.getCommunes().subscribe(data => this.communes = data);
    this.reclamationService.getSourceReclamation().subscribe(data => this.sourceReclamations = data);
    this.reclamationService.getTypeReclamation().subscribe(data => this.typeReclamations = data);
    this.reclamationService.getTypeRequete().subscribe(data => this.typeRequetes = data);
    this.reclamationService.getUserLogin().subscribe(data => this.userLogins = data);
    this.reclamationService.getVoieReponse().subscribe(data => this.voieReponses = data);
  }

  ngOnInit(): void {
    this.searchReclamationsPage();
    this.loadStaticData();

  }
  modalMessage: string = "";
  modalMessageType: string = "";
  message: string = "";

  
  searchReclamations(): void {
    const rawFilters = { ...this.searchForm.value };

    Object.keys(rawFilters).forEach(key => {
      if (typeof rawFilters[key] === 'string' && rawFilters[key].trim() === '') {
        rawFilters[key] = null;
      }
    });
  
    if (rawFilters.identifiant) {
      rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    }
  
  rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : 0;
  rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : 0;
  rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : 0;
  rawFilters.flagCloture = rawFilters.flagCloture ? 1 : null;

  this.reclamationService.searchReclamations(rawFilters).subscribe(
    (response: any) => { // Modifier le type de 'data' en 'response'
      this.reclamations = response.content; // Extraire le tableau 'content'
      this.totalPages = response.totalPages; 
    }, error => {
      console.error('خطأ أثناء البحث', error);
    });
  }
  private formatDateForInput(dateInput: any): string {
    if (!dateInput) {
      return '';
    }
    const d = new Date(dateInput);
    const year = d.getFullYear();
    const month = ('0' + (d.getMonth() + 1)).slice(-2);
    const day = ('0' + d.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }
  
  consultReclamation(rec: any): void {

  
    this.reclamationService.getReclamation(rec.id).subscribe(
      (data: any) => {
        if(data.dateDepot) {
          data.dateDepot = new Date(data.dateDepot).toISOString().split('T')[0];
        }        
        this.selectedReclamation = data;
        this.isConsultation = true;
      },
      (error: any) => {
        console.error("Erreur lors du chargement de la réclamation", error);
      }
    );
    }

  editReclamation(rec: any): void {
    this.reclamationService.getReclamation(rec.id).subscribe(
      (data: any) => {
        if(data.dateDepot) {
          data.dateDepot = new Date(data.dateDepot).toISOString().split('T')[0];
        }        
        this.selectedReclamation = data;
        this.isConsultation = false;
      },
      (error: any) => {
        console.error("Erreur lors du chargement de la réclamation", error);
      }
    );
    }
    
/**
 * Transforme une date au format "YYYY-MM-DD" en "YYYY-MM-DDT00:00:00" 
 * si l'heure n'est pas déjà présente.
 */
private completeDate(dateStr: string, isStart: boolean = true): string {
  if (!dateStr) {
    return dateStr;
  }
  // Si la chaîne contient déjà "T", on la retourne telle quelle
  if (dateStr.indexOf('T') !== -1) {
    return dateStr;
  }
  return dateStr + (isStart ? 'T00:00:00' : 'T23:59:59');
}

saveReclamation(): void {
  if (this.selectedReclamation.dateDepot && this.selectedReclamation.dateDepot.length === 10) {
    this.selectedReclamation.dateDepot = this.selectedReclamation.dateDepot + "T00:00:00";
  }

  this.reclamationService.updateReclamation(this.selectedReclamation.id, this.selectedReclamation).subscribe(
    (updatedRec) => {
      this.selectedReclamation = updatedRec;
      this.closeModal();
      this.searchReclamationsPage();
    },
    (error) => {
      console.error('Erreur lors de la mise à jour', error);
    }
  );

}

  deleteReclamation(id: number): void {
    if (confirm("هل أنت متأكد من حذف هذه الشكاية؟")) {
      this.reclamationService.deleteReclamation(id).subscribe({
        next: () => {
          this.message = "✅ تم حذف الشكاية.";
          this.searchReclamations(); 
        },
        error: (err) => {
          console.error("❌ خطأ أثناء الحذف :", err);
          this.message = "❌ خطأ أثناء الحذف";
        }
      });
    }
  }

  closeModal(): void {
    this.selectedReclamation = null;
    this.isConsultation = false;
    this.isEditing = false;
  }
  private loadListes(): void {

    this.reclamationService.getCommunes().subscribe(
      data => this.communes = data,
      err => console.error('Erreur lors de la récupération des communes', err)
    );
    this.reclamationService.getTypeRequete().subscribe(
      data => this.typeRequetes = data,
      err => console.error('Erreur lors de la récupération des types de requêtes', err)
    );
        

    this.reclamationService.getTypeReclamation().subscribe(
      data => this.typeReclamations = data,
      err => console.error('Erreur lors de la récupération des types de réclamations', err)
    );
    
    this.reclamationService.getSourceReclamation().subscribe(
      data => this.sourceReclamations = data,
      err => console.error('Erreur lors de la récupération des sources de réclamations', err)
    );
    this.reclamationService.getTypeDestinataire().subscribe(
      data => this.typeDestinataires = data,
      err => console.error('Erreur lors de la récupération des types de déstinataires', err)
    );	

    this.reclamationService.getUserLogin().subscribe(
      data => this.userLogins = data,
      err => console.error('Erreur lors de la récupération des Logins', err)
    );
    this.reclamationService.getVoieReponse().subscribe(
      data => this.voieReponses = data,
      err => console.error('Erreur lors de la récupération des voies de réponses', err)
    );				 

  }
  formatIdentifiant() {
    if (this.selectedReclamation.identifiant) {
      this.selectedReclamation.identifiant = this.selectedReclamation.identifiant
        .replace(/[^a-zA-Z0-9]/g, '')
        .toUpperCase();
    }
  }
  
// Définir la regex pour un numéro de téléphone à 10 chiffres
telephoneRegex: RegExp = /^[0-9]{10}$/;

validateTelephone() {
  const tel = this.selectedReclamation.telephone;
  const isValid = this.telephoneRegex.test(tel);
  if (!isValid) {
    alert("Le format du téléphone est incorrect. Veuillez entrer 10 chiffres.");
  }
}
private normalizeDate(dateStr: string, isStart: boolean): string {
  if (!dateStr) { 
    return dateStr;
  }
  const dateOnly = dateStr.split('T')[0];
  return dateOnly + (isStart ? 'T00:00:00' : 'T23:59:59');
}

private normalizeDateFilters(filters: any): any {
  if (filters.dateDepotStart) {
    filters.dateDepotStart = this.normalizeDate(filters.dateDepotStart, true);
  }
  if (filters.dateDepotEnd) {
    filters.dateDepotEnd = this.normalizeDate(filters.dateDepotEnd, false);
  }
  if (filters.dateInscriptionStart) {
    filters.dateInscriptionStart = this.normalizeDate(filters.dateInscriptionStart, true);
  }
  if (filters.dateInscriptionEnd) {
    filters.dateInscriptionEnd = this.normalizeDate(filters.dateInscriptionEnd, false);
  }
  if (filters.dateEnvoiAutoriteStart) {
    filters.dateEnvoiAutoriteStart = this.normalizeDate(filters.dateEnvoiAutoriteStart, true);
  }
  if (filters.dateEnvoiAutoriteEnd) {
    filters.dateEnvoiAutoriteEnd = this.normalizeDate(filters.dateEnvoiAutoriteEnd, false);
  }
  if (filters.dateRetourAutoriteStart) {
  filters.dateRetourAutoriteStart = this.normalizeDate(filters.dateRetourAutoriteStart, true);
  }
  if (filters.dateRetourAutoriteEnd) {
  filters.dateRetourAutoriteEnd = this.normalizeDate(filters.dateRetourAutoriteEnd, false);
}
if (filters.dateEnvoiReclamantStart) {
  filters.dateEnvoiReclamantStart = this.normalizeDate(filters.dateEnvoiReclamantStart, true);
}
if (filters.dateEnvoiReclamantEnd) {
  filters.dateEnvoiReclamantEnd = this.normalizeDate(filters.dateEnvoiReclamantEnd, false);
}



  return filters;
}

searchReclamationsPage(): void {
  let rawFilters = { ...this.searchForm.value };

  Object.keys(rawFilters).forEach(key => {
    if (typeof rawFilters[key] === 'string' && rawFilters[key].trim() === '') {
      rawFilters[key] = null;
    }
  });
  if (rawFilters.identifiant) {
    rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }
  rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : 0;
  rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : 0;
  rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : 0;
  rawFilters.flagCloture = rawFilters.flagCloture ? 1 : null;

  rawFilters = this.normalizeDateFilters(rawFilters);

  const params = {
    ...rawFilters,
    page: this.currentPage,
    size: this.pageSize
  };

  this.reclamationService.searchReclamationsPage(params).subscribe(response => {
    this.reclamations = response.content;
    this.totalPages = response.totalPages;
  }, error => {
    console.error('Erreur lors de la recherche paginée', error);

  });
}
  onPageSizeChange(): void {
    this.currentPage = 0;
    this.searchReclamationsPage();
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages - 1) {
      this.currentPage++;
      this.searchReclamationsPage();
    }
  }

  previousPage(): void {
    if (this.currentPage > 0) {
      this.currentPage--;
      this.searchReclamationsPage();
    }
  }

  // Export vers Excel en appliquant les mêmes filtres (sans pagination)
  exportExcel(): void {
    const rawFilters = { ...this.searchForm.value };
    Object.keys(rawFilters).forEach(key => {
      if (typeof rawFilters[key] === 'string' && rawFilters[key].trim() === '') {
        rawFilters[key] = null;
      }
    });
  
    if (rawFilters.identifiant) {
      rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    }
  
    rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : null;
    rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : null;
    rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : null;
    rawFilters.flagCloture = rawFilters.flagCloture ? 1 : null;

    // Pour l'export, on n'envoie PAS les paramètres de pagination
    const params = { ...rawFilters };
  
    this.reclamationService.exportExcel(params).subscribe(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'reclamations.xlsx';
      a.click();
      window.URL.revokeObjectURL(url);
    }, error => {
      console.error('Erreur lors de l’export', error);
    });
  }
  private patchAssociations(rec: Reclamation): Reclamation {
    // Pour typeReclamation
    if (rec.typeReclamation && this.typeReclamations && this.typeReclamations.length > 0) {
      const id = rec.typeReclamation.id || rec.typeReclamation;
      rec.typeReclamation = this.typeReclamations.find(t => t.id === id) || rec.typeReclamation;
    }
    // Pour typeRequete
    if (rec.typeRequete && this.typeRequetes && this.typeRequetes.length > 0) {
      const id = rec.typeRequete.id || rec.typeRequete;
      rec.typeRequete = this.typeRequetes.find(t => t.id === id) || rec.typeRequete;
    }
/*     // Pour typeDestinataire
    if (rec.typeDestinataire && this.typeDestinataires && this.typeDestinataires.length > 0) {
      const id = rec.typeDestinataire.id || rec.typeDestinataire;
      rec.typeDestinataire = this.typeDestinataires.find(t => t.id === id) || rec.typeDestinataire;
    }
    // Pour userSaisie, si besoin (exemple déjà fonctionnel, car vous affichez son login)
    if (rec.userSaisie && this.userLogins && this.userLogins.length > 0) {
      const id = rec.userSaisie.id || rec.userSaisie;
      rec.userSaisie = this.userLogins.find(u => u.id === id) || rec.userSaisie;
    }
      // Pour voieEnvoiAutorite
  if (rec.voieEnvoiAutorite && this.voieReponses && this.voieReponses.length > 0) {
    const id = rec.voieEnvoiAutorite.id || rec.voieEnvoiAutorite;
    rec.voieEnvoiAutorite = this.voieReponses.find(v => v.id === id) || rec.voieEnvoiAutorite;
  }
  // Pour voieRetourAutorite
  if (rec.voieRetourAutorite && this.voieReponses && this.voieReponses.length > 0) {
    const id = rec.voieRetourAutorite.id || rec.voieRetourAutorite;
    rec.voieRetourAutorite = this.voieReponses.find(v => v.id === id) || rec.voieRetourAutorite;
  }
  // Pour voieEnvoiReclamant
  if (rec.voieEnvoiReclamant && this.voieReponses && this.voieReponses.length > 0) {
    const id = rec.voieEnvoiReclamant.id || rec.voieEnvoiReclamant;
    rec.voieEnvoiReclamant = this.voieReponses.find(v => v.id === id) || rec.voieEnvoiReclamant;
  } */

    return rec;
  }
  formatNumInscription(num: string): string {
    const regex = /^\((\d{4})\)(.+)$/;
    const match = num.match(regex);
    if (match) {
      return `${match[2]}(${match[1]})`;
    }
    return num;
  }
  removeDetail(detail: any) {
    if (this.selectedReclamation.details) {
      this.selectedReclamation.details = this.selectedReclamation.details.filter((d: { id: any; }) => d.id !== detail.id);
    }
  }
  compareById(item1: any, item2: any): boolean {
    return item1 && item2 ? item1.id === item2.id : item1 === item2;
  }
      
}
