import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  profilePicture = 'assets/default-profile.png';
  dropdownOpen = false;

  constructor(private router: Router, private authService: AuthService) {}

  ngOnInit() {
    this.authService.getUserProfile().subscribe(user => {
      if (user.profilePicture) {
        this.profilePicture = `uploads/profile_pictures/${user.profilePicture}`;
      }
    });
  }


toggleDropdown() {
  this.dropdownOpen = !this.dropdownOpen;
  setTimeout(() => {
    const dropdownElement = document.getElementById("profileDropdown");
    if (dropdownElement) {
      dropdownElement.setAttribute("aria-expanded", this.dropdownOpen ? "true" : "false");
    }
  });
}

  goToProfile() {
    this.router.navigate(['/profile']);
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
