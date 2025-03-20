import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { NgSelectModule } from '@ng-select/ng-select';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ReclamationFormComponent } from './components/reclamation-form/reclamation-form.component';
import { FormsModule } from '@angular/forms';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { LoginComponent } from './components/login/login.component';
import { ReclamationListComponent } from './components/reclamation-list/reclamation-list.component';
import { ScanUploadComponent } from './components/scan-upload/scan-upload.component';
import { ReclamationSearchComponent } from './components/reclamation-search/reclamation-search.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { AuthService } from './services/auth.service';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { ReactiveFormsModule } from '@angular/forms';
import { ProfileComponent } from './components/profile/profile.component';
import { HeaderComponent } from './components/header/header.component';
import { AdminComponent } from './components/admin/admin.component';
import { HomeComponent } from './components/home/home.component';
import { NiveauComponent } from './components/niveau/niveau.component';
import { SourceReclamationComponent } from './admin/settings/source-reclamation/source-reclamation.component';
import { TerritoiresComponent } from './admin/settings/territoires/territoires.component';
import { TypeDestinataireComponent } from './admin/settings/type-destinataire/type-destinataire.component';
import { TypeReclamationComponent } from './admin/settings/type-reclamation/type-reclamation.component';
import { TypeRequeteComponent } from './admin/settings/type-requete/type-requete.component';
import { VoieReponseComponent } from './admin/settings/voie-reponse/voie-reponse.component';
import { ReclamationManagementComponent } from './components/reclamation-management/reclamation-management.component';
import { ReclamationEnvoiAutoriteComponent } from './components/reclamation-envoi-autorite/reclamation-envoi-autorite.component';
import { ReclamationRetourAutoriteComponent } from './components/reclamation-retour-autorite/reclamation-retour-autorite.component';
import { ReclamationEnvoiReclamantComponent } from './components/reclamation-envoi-reclamant/reclamation-envoi-reclamant.component';
import { OuvertureReclamationComponent } from './components/ouverture-reclamation/ouverture-reclamation.component';
import { ClotureReclamationComponent } from './components/cloture-reclamation/cloture-reclamation.component';

@NgModule({
  declarations: [
    AppComponent,
    ReclamationFormComponent,
    LoginComponent,
    ResetPasswordComponent,
    ReclamationListComponent,
    ScanUploadComponent,
    ReclamationSearchComponent,
    SidebarComponent,
    ProfileComponent,
    HeaderComponent,
    AdminComponent,
    HomeComponent,
    NiveauComponent,
    SourceReclamationComponent,
    TerritoiresComponent,
    TypeDestinataireComponent,
    TypeReclamationComponent,
    TypeRequeteComponent,
    VoieReponseComponent,
    ReclamationManagementComponent,
    ReclamationEnvoiAutoriteComponent,
    ReclamationRetourAutoriteComponent,
    ReclamationEnvoiReclamantComponent,
    OuvertureReclamationComponent,
    ClotureReclamationComponent, 
  ],
  imports: [
    BrowserModule,
    FormsModule,  
    HttpClientModule, 
    NgSelectModule,
    AppRoutingModule,
    ReactiveFormsModule, 
  ],
  providers: [AuthService],
  bootstrap: [AppComponent]
})
export class AppModule {  constructor() {
} 

}
