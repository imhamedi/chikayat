import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LoginComponent } from './components/login/login.component';
import { ReclamationFormComponent } from './components/reclamation-form/reclamation-form.component';
import { ReclamationListComponent } from './components/reclamation-list/reclamation-list.component';
import { ScanUploadComponent } from './components/scan-upload/scan-upload.component';
import { AuthGuard } from './guards/auth.guard';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { ProfileComponent } from './components/profile/profile.component';
import { AdminComponent } from './components/admin/admin.component';
import { HomeComponent } from './components/home/home.component';
import { NiveauComponent } from './components/niveau/niveau.component';

import { SourceReclamationComponent } from './admin/settings/source-reclamation/source-reclamation.component';
import { TypeDestinataireComponent } from './admin/settings/type-destinataire/type-destinataire.component';
import { TypeReclamationComponent } from './admin/settings/type-reclamation/type-reclamation.component';
import { TypeRequeteComponent } from './admin/settings/type-requete/type-requete.component';
import { VoieReponseComponent } from './admin/settings/voie-reponse/voie-reponse.component';
import { TerritoiresComponent } from './admin/settings/territoires/territoires.component';
import { ReclamationManagementComponent } from './components/reclamation-management/reclamation-management.component';
import { ReclamationEnvoiAutoriteComponent } from './components/reclamation-envoi-autorite/reclamation-envoi-autorite.component';
import { ReclamationRetourAutoriteComponent } from './components/reclamation-retour-autorite/reclamation-retour-autorite.component';
import { ReclamationEnvoiReclamantComponent } from './components/reclamation-envoi-reclamant/reclamation-envoi-reclamant.component';
import { OuvertureReclamationComponent } from './components/ouverture-reclamation/ouverture-reclamation.component';
import { ClotureReclamationComponent } from './components/cloture-reclamation/cloture-reclamation.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'reclamations', component: ReclamationListComponent, canActivate: [AuthGuard] },
  { path: 'reclamations/envoi-autorite', component: ReclamationEnvoiAutoriteComponent, canActivate: [AuthGuard] },
  { path: 'reclamations/retour-autorite', component: ReclamationRetourAutoriteComponent, canActivate: [AuthGuard] },
  { path: 'reclamations/envoi-reclamant', component: ReclamationEnvoiReclamantComponent, canActivate: [AuthGuard] },
  { path: 'ajouter-reclamation', component: ReclamationFormComponent, canActivate: [AuthGuard] },
  { path: 'ajouter-niveau', component: NiveauComponent, canActivate: [AuthGuard] },
  { path: 'admin/settings/source-reclamation', component: SourceReclamationComponent , canActivate: [AuthGuard] },
  { path: 'admin/settings/type-destinataire', component: TypeDestinataireComponent , canActivate: [AuthGuard] },
  { path: 'admin/settings/type-reclamation', component: TypeReclamationComponent , canActivate: [AuthGuard] },
  { path: 'admin/settings/type-requete', component: TypeRequeteComponent , canActivate: [AuthGuard] },
  { path: 'admin/settings/voie-reponse', component: VoieReponseComponent , canActivate: [AuthGuard] },
  { path: 'admin/settings/territoires', component: TerritoiresComponent , canActivate: [AuthGuard] },  
  { path: 'scan', component: ScanUploadComponent, canActivate: [AuthGuard] },
  { path: 'reset-password', component: ResetPasswordComponent }, 
  { path: 'reset-password/:token', component: ResetPasswordComponent }, 
  { path: 'profile', component: ProfileComponent, canActivate: [AuthGuard] },
  { path: 'reclamation-management', component: ReclamationManagementComponent, canActivate: [AuthGuard] },
  { path: 'admin/users', component: AdminComponent, canActivate: [AuthGuard] },
  { path: '', redirectTo: '/home', pathMatch: 'full' }, 
  { path: 'home', component: HomeComponent }, 
  { path: 'ouverture-reclamation', component: OuvertureReclamationComponent, canActivate: [AuthGuard] },
  { path: 'cloture-reclamation', component: ClotureReclamationComponent, canActivate: [AuthGuard] },


];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { 
  constructor() {
  }
}
