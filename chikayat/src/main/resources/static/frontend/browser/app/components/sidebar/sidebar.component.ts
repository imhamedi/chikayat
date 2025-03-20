import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.css']
})
export class SidebarComponent {
  constructor(private authService: AuthService, private router: Router) {}

  activeMenu: string | null = null;
  activeSubMenu: string | null = null; // ✅ Ajout d'un état pour les sous-menus

  toggleMenu(menu: string) {
    if (this.activeMenu === menu) {
      this.activeMenu = null; // Ferme uniquement le menu si déjà ouvert
    } else {
      this.activeMenu = menu; // Ouvre un menu et ferme les autres
      this.activeSubMenu = null; // Réinitialise les sous-menus pour éviter les conflits
    }
  }

  toggleSubMenu(subMenu: string) {
    this.activeSubMenu = this.activeSubMenu === subMenu ? null : subMenu;
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']); // ✅ Redirection après déconnexion
  }
}
