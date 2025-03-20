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

  // Listes fixes pour les listes déroulantes (à remplacer par des appels API si besoin)
  communes: string[] = [];
  typeRequetes: TypeRequete[] = [];
  typeReclamations: TypeReclamation[] = [];
  sourceReclamations: SourceReclamation[] = [];
  typeDestinataires: TypeDestinataire[] = [];
  userLogins: UserLogin[] = [];
  voieReponses: VoieReponse[] = [];   
// Propriétés pour la pagination
public currentPage: number = 0;
public pageSize: number = 10;
public totalPages: number = 0;
public pageSizeOptions: number[] = [5, 10, 20, 50];

  constructor(private fb: FormBuilder, private reclamationService: ReclamationService) {
    // Initialisation de tous les filtres conformément aux consignes
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
      flagEnvoiAutorite: [false],
      dateEnvoiAutoriteStart: [''],
      dateEnvoiAutoriteEnd: [''],
      voieEnvoiAutorite: [''],
      referenceEnvoiAutorite: [''],
      flagRetourAutorite: [false],
      dateRetourAutoriteStart: [''],
      dateRetourAutoriteEnd: [''],
      voieRetourAutorite: [''],
      referenceRetourAutorite: [''],
      flagEnvoiReclamant: [false],
      dateEnvoiReclamantStart: [''],
      dateEnvoiReclamantEnd: [''],
      voieEnvoiReclamant: [''],
      referenceEnvoiReclamant: [''],
      userSaisie: [''],
      userEnvoiAutorite: [''],
      userRetourAutorite: [''],
      typeRequete: [''],
      sourceReclamation: [''],
      destinataire: ['']
    });
  }

  ngOnInit(): void {
    this.searchReclamationsPage();
    this.loadListes();

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
  
    // Normalisation du champ "identifiant" : suppression des espaces et caractères spéciaux, et passage en majuscules
    if (rawFilters.identifiant) {
      rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    }
  
    // Conversion explicite des cases à cocher en entiers (0 pour false, 1 pour true)
  rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : 0;
  rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : 0;
  rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : 0;

  this.reclamationService.searchReclamations(rawFilters).subscribe(
    (response: any) => { // Modifier le type de 'data' en 'response'
      this.reclamations = response.content; // Extraire le tableau 'content'
      this.totalPages = response.totalPages; // Mettre à jour si nécessaire
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
    this.selectedReclamation = { ...rec };

    // Formatez les dates pour l'input "date"
    if (this.selectedReclamation.dateDepot) {
      this.selectedReclamation.dateDepot = this.formatDateForInput(this.selectedReclamation.dateDepot);
    }
    if (this.selectedReclamation.dateEnvoiAutorite) {
      this.selectedReclamation.dateEnvoiAutorite= this.formatDateForInput(this.selectedReclamation.dateEnvoiAutorite);
    }
    if (this.selectedReclamation.dateRetourAutorite) {
      this.selectedReclamation.dateRetourAutorite= this.formatDateForInput(this.selectedReclamation.dateRetourAutorite);
    }
    if (this.selectedReclamation.dateEnvoiReclamant) {
      this.selectedReclamation.dateEnvoiReclamant= this.formatDateForInput(this.selectedReclamation.dateEnvoiReclamant);
    }

    if (this.selectedReclamation.dateInscription) {
      this.selectedReclamation.dateInscription = this.formatDateForInput(this.selectedReclamation.dateInscription);
    }
    // Faites de même pour les autres dates du modal, si nécessaire
  
    // Appliquez le patch sur les associations pour les listes si besoin (cf. la méthode patchAssociations)
    this.selectedReclamation = this.patchAssociations({ ...this.selectedReclamation });
      this.isConsultation = true;
    this.isEditing = false;
  }

  editReclamation(rec: any): void {
    this.selectedReclamation = { ...rec };

    if (this.selectedReclamation.dateDepot) {
      this.selectedReclamation.dateDepot = this.formatDateForInput(this.selectedReclamation.dateDepot);
    }
    if (this.selectedReclamation.dateInscription) {
      this.selectedReclamation.dateInscription = this.formatDateForInput(this.selectedReclamation.dateInscription);
    }
    // Répétez pour les autres dates du formulaire au besoin
  
    this.selectedReclamation = this.patchAssociations({ ...this.selectedReclamation });
      this.isEditing = true;
    this.isConsultation = false;
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
  // Sinon, on ajoute l'heure minimale (ou maximale)
  return dateStr + (isStart ? 'T00:00:00' : 'T23:59:59');
}

saveReclamation(): void {
  // Cloner la réclamation sélectionnée
  let reclamationToSave = { ...this.selectedReclamation };

  // Liste des champs de date à transformer
  const dateFields = [
    'dateDepot',
    'dateInscription',
    'dateEnvoiAutorite',
    'dateRetourAutorite',
    'dateEnvoiReclamant'
  ];
  
  // Transformation de tous les champs de date
  dateFields.forEach(field => {
    if (reclamationToSave[field]) {
      reclamationToSave[field] = this.completeDate(reclamationToSave[field], true);
    }
  });

  // On envoie l'objet transformé
  this.reclamationService.updateReclamation(reclamationToSave.id, reclamationToSave)
    .subscribe(data => {
      this.modalMessage = "✅ تم التعديل بنجاح";
      this.searchReclamationsPage();
      this.selectedReclamation = null;
      this.isEditing = false;
    }, error => {
      console.error('خطأ أثناء التعديل', error);
      this.modalMessage = "❌ خطأ أثناء التعديل";
      setTimeout(() => this.modalMessage = "", 3000);
    });
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
    // Vous pouvez aussi choisir de vider le champ ou de marquer le champ comme invalide dans le form group
  }
}
private normalizeDate(dateStr: string, isStart: boolean): string {
  // Si la chaîne est vide, renvoyer directement
  if (!dateStr) { 
    return dateStr;
  }
  // On extrait uniquement la partie date (avant le "T") si présent
  const dateOnly = dateStr.split('T')[0];
  // Retourne la date complétée avec l'heure minimale ou maximale
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
  // Faites de même pour les autres dates si nécessaire
  return filters;
}

searchReclamationsPage(): void {
  let rawFilters = { ...this.searchForm.value };


  // Remplacer les chaînes vides par null
  Object.keys(rawFilters).forEach(key => {
    if (typeof rawFilters[key] === 'string' && rawFilters[key].trim() === '') {
      rawFilters[key] = null;
    }
  });
  // Normaliser le champ "identifiant"
  if (rawFilters.identifiant) {
    rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
  }
  // Conversion des cases à cocher en entier
  rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : 0;
  rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : 0;
  rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : 0;
  rawFilters = this.normalizeDateFilters(rawFilters);

  // Ajouter les paramètres de pagination
  const params = {
    ...rawFilters,
    page: this.currentPage,
    size: this.pageSize
  };

  this.reclamationService.searchReclamationsPage(params).subscribe(response => {
    // Extraire explicitement le tableau contenu dans "content"
    this.reclamations = response.content;
    this.totalPages = response.totalPages;
  }, error => {
    console.error('Erreur lors de la recherche paginée', error);

  });
}
  // Méthodes de pagination
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
  
    // Remplacer les chaînes vides par null
    Object.keys(rawFilters).forEach(key => {
      if (typeof rawFilters[key] === 'string' && rawFilters[key].trim() === '') {
        rawFilters[key] = null;
      }
    });
  
    // Normaliser le champ "identifiant"
    if (rawFilters.identifiant) {
      rawFilters.identifiant = rawFilters.identifiant.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    }
  
    // Pour les cases à cocher :
    // Si la case est activée, on envoie 1 (filtrer sur 1) ; sinon on laisse la valeur à null pour ne pas filtrer.
    rawFilters.flagEnvoiAutorite = (rawFilters.flagEnvoiAutorite === true || rawFilters.flagEnvoiAutorite === 'true') ? 1 : null;
    rawFilters.flagRetourAutorite = (rawFilters.flagRetourAutorite === true || rawFilters.flagRetourAutorite === 'true') ? 1 : null;
    rawFilters.flagEnvoiReclamant = (rawFilters.flagEnvoiReclamant === true || rawFilters.flagEnvoiReclamant === 'true') ? 1 : null;
  
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
    // Pour typeDestinataire
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
  }

    // Ne pas patcher les autres propriétés qui n'existent pas dans l'interface actuelle.
    return rec;
  }
  formatNumInscription(num: string): string {
    // Supposons que le back renvoie le numéro au format "(2025)51/02"
    // et que vous souhaitez le transformer en "51/02(2025)"
    // On utilise une expression régulière pour extraire les deux parties.
    const regex = /^\((\d{4})\)(.+)$/;
    const match = num.match(regex);
    if (match) {
      return `${match[2]}(${match[1]})`;
    }
    // Sinon, retourne la chaîne d'origine
    return num;
  }
      
}
