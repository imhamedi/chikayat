import { Component, OnInit, ViewChild, ElementRef, HostListener } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReclamationService, SourceReclamation, TypeDestinataire, TypeReclamation, TypeRequete } from '../../services/reclamation.service';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';

interface Option {
  id: number;
  nomLangue2: string;
}





declare var bootstrap: any;  // Pour utiliser le modal Bootstrap

@Component({
  selector: 'app-reclamation-form',
  templateUrl: './reclamation-form.component.html',
  styleUrls: ['./reclamation-form.component.css']
})

export class ReclamationFormComponent implements OnInit {
  private baseUrl = environment.apiUrl;
  formSubmitted: boolean = false; 
  reclamationForm!: FormGroup;
  reclamationCount: number = 0;
  reclamations: any[] = [];

  communes: string[] = [];
  typeRequetes: TypeRequete[] = [];
  typeReclamations: TypeReclamation[] = [];
  sourceReclamations: SourceReclamation[] = [];
  typeDestinataires: TypeDestinataire[] = [];
 



  selectedFile: File | null = null;
  successMessage: string = '';
  errorMessage: string = '';
  ocrText: string = '';

  @ViewChild('telephoneInput') telephoneInput!: ElementRef;

  // Supposons que l'ID de l'utilisateur connecté est stocké dans localStorage sous "userId"
  currentUserId: number = Number(localStorage.getItem('userId'));

  constructor(
    private fb: FormBuilder,
    private reclamationService: ReclamationService,
    private http: HttpClient
  ) {
  }

  ngOnInit(): void {
      this.initForm();
      this.loadListes();
  }

  // Ajoute des secondes par défaut si nécessaire
  private appendSeconds(dateStr: string): string {
    if (dateStr.includes('T')) {
      const timePart = dateStr.split('T')[1];
      if (timePart.split(':').length === 2) {
        return dateStr + ':00';
      }
      return dateStr;
    }
    return dateStr + 'T00:00:00';
  }

  onFileSelected(event: any): void {
    if (event.target.files && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
    }
  }

  submitForm(): void {
    const telephoneControl = this.reclamationForm.get('telephone');
    if (this.reclamationForm.invalid) {
      if (telephoneControl?.invalid) {
        this.errorMessage = 'Format de téléphone erroné! Veuillez entrer un numéro à 10 chiffres.';
        // Remet le focus sur le champ téléphone si la référence existe
        setTimeout(() => {
          this.telephoneInput.nativeElement.focus();
        }, 0);
      } else {
        this.errorMessage = 'Veuillez remplir tous les champs obligatoires correctement.';
      }
      return;
    }
    let formData = this.reclamationForm.getRawValue();

    formData.dateDepot = this.appendSeconds(formData.dateDepot);
    formData.dateInscription = this.appendSeconds(formData.dateInscription);
    if (!formData.annee && formData.dateDepot) {
      formData.annee = new Date(formData.dateDepot).getFullYear();
    }
    if (!formData.referenceBo) {
      formData.referenceBo = formData.numInscription;
    }

    // Transformer les associations en objets avec leur id
    formData.typeRequete = { id: parseInt(formData.typeRequete, 10) };
    formData.typeReclamation = { id: parseInt(formData.typeReclamation, 10) };
    formData.sourceReclamation = { id: parseInt(formData.sourceReclamation, 10) };
    formData.typeDestinataire = { id: parseInt(formData.typeDestinataire, 10) };
    formData.userSaisie = { id: formData.userSaisie };

    this.reclamationService.createReclamation(formData).subscribe(
      response => {
        const reclamationId = response.id;
        if (reclamationId == null) {
          this.errorMessage = "Erreur : l'ID de la réclamation n'a pas été retourné.";
          return;
        }
        if (this.selectedFile) {
          this.reclamationService.uploadFile(reclamationId, this.selectedFile).subscribe(
            uploadRes => {
              this.successMessage = 'Réclamation créée et fichier uploadé avec succès.';
              this.resetForm();

            },
            uploadErr => {
              this.errorMessage = 'Réclamation créée, mais erreur lors de l’upload du fichier.';
            }
          );
        } else {
          this.successMessage = 'Réclamation créée avec succès.';
          this.resetForm();

        }
      },
      error => {
        this.errorMessage = 'Erreur lors de la création de la réclamation.';
      }
    );
  }

  convertImageToText(): void {
    if (!this.selectedFile) {
      alert('Veuillez sélectionner un fichier d\'image avant de lancer l\'OCR.');
      return;
    }
    const formData = new FormData();
    formData.append('file', this.selectedFile);
    // Utilisez le code de langue souhaité ; ici 'ara' pour l'arabe
    formData.append('language', 'ara');
  
    // Appel au nouvel endpoint OCR
    this.http.post<any>(`${this.baseUrl}/ocr/extract-text`, formData).subscribe(
      response => {
        this.ocrText = response.extractedText;
        // Affichage de la modale contenant le texte extrait
        const modalElem = document.getElementById('ocrModal');
        if (modalElem) {
          const modal = new bootstrap.Modal(modalElem);
          modal.show();
        }
      },
      error => {
        console.error('Erreur OCR:', error);
        alert('Erreur lors de l\'extraction du texte.');
      }
    );
  }
  

  copyOcrText(): void {
    navigator.clipboard.writeText(this.ocrText).then(() => {
      alert('Texte copié dans le presse-papiers.');
    });
  }
  
  checkReclamations(): void {
  // Formater l'identifiant : supprimer espaces et caractères spéciaux et mettre en majuscules
  const identifiantControl = this.reclamationForm.get('identifiant');
  if (identifiantControl && identifiantControl.value) {
    const formatted = identifiantControl.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
    identifiantControl.setValue(formatted);
  }
  
  // Puis effectuer la recherche
  const identifiant = identifiantControl?.value;
    if (identifiant) {
      this.http.get<any>(`${this.baseUrl}/reclamations/by-identifiant/${identifiant}`)
        .subscribe(
          res => {
            this.reclamationCount = res.count;
            this.reclamations = res.reclamations;
          },
          err => {
            console.error('Erreur lors de la recherche des réclamations :', err);
            this.reclamationCount = 0;
            this.reclamations = [];
          }
        );
    } else {
      this.reclamationCount = 0;
      this.reclamations = [];
    }
  }

  openReclamationsModal(): void {
    const modalElem = document.getElementById('reclamationsModal');
    if (modalElem) {
      const modal = new bootstrap.Modal(modalElem);
      modal.show();
    }
  }
  resetForm(): void {
    this.formSubmitted = false;
    this.reclamationForm.reset();
    this.reclamationCount = 0;

    setTimeout(() => {
      this.successMessage = '';
      this.errorMessage = '';
    }, 3000);
      this.initForm(); 
      this.loadListes();
  }




  formatIdentifiant(): void {
    const identifiantControl = this.reclamationForm.get('identifiant');
    if (identifiantControl && identifiantControl.value) {
      // Supprime tous les caractères qui ne sont pas des lettres ou chiffres
      const formatted = identifiantControl.value.replace(/[^a-zA-Z0-9]/g, '').toUpperCase();
      identifiantControl.setValue(formatted);
    }
  }
  ngOnDestroy(): void {
    // Si le formulaire est quitté sans soumission, libérez le numéro
    if (!this.formSubmitted) {
      const numInscription = this.reclamationForm.get('numInscription')?.value;
      if (numInscription) {
        this.reclamationService.releaseNumInscription(numInscription).subscribe(
          () => console.log("Numéro d'inscription libéré"),
          error => console.error("Erreur lors de la libération du numéro", error)
        );
      }
    }
    window.removeEventListener('beforeunload', this.handleBeforeUnload);
  }
  private handleBeforeUnload = (event: BeforeUnloadEvent) => {
    // Utilisez sendBeacon pour garantir l'envoi de la requête même en déchargement de page
    if (!this.formSubmitted) {
      const numInscription = this.reclamationForm.get('numInscription')?.value;
      if (numInscription) {
        this.reclamationService.releaseNumInscription(numInscription).subscribe(
          () => console.log("Numéro d'inscription libéré"),
          error => console.error("Erreur lors de la libération du numéro", error)
        );
      }
    }
    event.preventDefault();
    event.returnValue = ''; // Nécessaire pour certains navigateurs

  };

  private releaseNumber(useBeacon: boolean = false): void {
    const numInscription = this.reclamationForm.get('numInscription')?.value;
    if (numInscription) {
      const payload = JSON.stringify({ numInscription });
      if (useBeacon && navigator.sendBeacon) {
        // sendBeacon ne permet pas de définir les headers ; le backend devra accepter ce type de payload
        navigator.sendBeacon(`${this.baseUrl}/reclamations/release-num-inscription`, payload);
      } else {
        // Utilisation classique via le service Angular
        this.reclamationService.releaseNumInscription(numInscription).subscribe(
          () => console.log("Numéro d'inscription libéré"),
          error => console.error("Erreur lors de la libération du numéro", error)
        );
      }
    }
  }
  @HostListener('window:beforeunload', ['$event'])
  unloadNotification($event: BeforeUnloadEvent) {
    if (!this.formSubmitted) {
      const numInscription = this.reclamationForm.get('numInscription')?.value;
      if (numInscription) {
        this.reclamationService.releaseNumInscription(numInscription).subscribe(
          () => console.log("Numéro d'inscription libéré"),
          error => console.error("Erreur lors de la libération du numéro", error)
        );
      }
    }
    this.releaseNumber(true);    
    $event.preventDefault();
    $event.returnValue = ''; // Display confirmation dialog
  }
  private initForm(): void {
    const today = new Date().toISOString().split('T')[0];
    this.reclamationForm = this.fb.group({
      numInscription: [{ value: '', disabled: true }],
      referenceBo: ['', Validators.required],
      typeIdentifiant: ['CIN', Validators.required],
      identifiant: ['', Validators.required],
      nomComplet: ['', Validators.required],
      commune: ['', Validators.required],
      adresse: [''],
      telephone: ['', [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      dateDepot: [today, Validators.required],
      dateInscription: [today],
      typeRequete: ['', Validators.required],
      typeReclamation: ['', Validators.required],
      sourceReclamation: ['', Validators.required],
      objetReclamation: ['', Validators.required],
      instructionsGouverneur: [''],
      annee: [new Date().getFullYear()],
      typeDestinataire: ['', Validators.required],
      destinataire: ['', Validators.required],
      observation1: [''],
      userSaisie: [this.currentUserId, Validators.required]
    });
    // Génération du numéro d'inscription et affectation à numInscription et referenceBo
    this.reclamationService.generateNumInscription().subscribe(
      num => {
        this.reclamationForm.get('numInscription')?.setValue(num);
        this.reclamationForm.get('referenceBo')?.setValue(num);
      },
      err => console.error('Erreur lors de la génération du numéro', err)
    );
  }

  /**
   * Charge les listes déroulantes depuis le backend.
   */
  private loadListes(): void {
    this.reclamationForm.get('typeIdentifiant')?.setValue('CIN');

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
			 
    const today = new Date().toISOString().split('T')[0];
    this.reclamationForm.patchValue({ dateDepot: today, dateInscription: today });
    this.reclamationForm.get('annee')?.setValue(new Date().getFullYear());
    this.formSubmitted = false; 
  }
}
