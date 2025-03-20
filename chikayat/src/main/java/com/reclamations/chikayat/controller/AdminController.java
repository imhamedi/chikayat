package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.Utilisateur;
import com.reclamations.chikayat.repository.UtilisateurRepository;
import com.reclamations.chikayat.service.EmailService;
import com.reclamations.chikayat.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private UtilisateurRepository utilisateurRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @GetMapping("/users")
    public List<Utilisateur> getUsers(@RequestParam(required = false) String nom,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String login,
            @RequestParam(required = false) String niveau,
            @RequestParam(required = false) String role) {
        return utilisateurService.filterUsers(nom, email, login, niveau, role);
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody Utilisateur utilisateur) {
        boolean success = utilisateurService.updateUser(id, utilisateur);
        return success ? ResponseEntity.ok("Utilisateur mis à jour avec succès.")
                : ResponseEntity.status(400).body("Erreur lors de la mise à jour de l'utilisateur.");
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        boolean success = utilisateurService.deleteUser(id);
        return success ? ResponseEntity.ok("Utilisateur supprimé avec succès.")
                : ResponseEntity.status(400).body("Erreur lors de la suppression de l'utilisateur.");
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@RequestBody Utilisateur utilisateur) {
        if (utilisateurRepository.findByLogin(utilisateur.getLogin()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Collections.singletonMap("error", "Un utilisateur avec ce login existe déjà."));
        }

        String tempPassword = utilisateurService.generateTempPassword();

        utilisateur.setPass(passwordEncoder.encode(tempPassword));
        utilisateur.setMustChangePassword(true);
        Utilisateur newUser = utilisateurRepository.save(utilisateur);

        try {
            String subject = "Bienvenue sur notre plateforme";
            String content = "<p>Bonjour " + utilisateur.getNom() + ",</p>"
                    + "<p>Votre compte a été créé avec succès.</p>"
                    + "<p>Votre identifiant : <b>" + utilisateur.getLogin() + "</b></p>"
                    + "<p>Votre mot de passe temporaire : <b>" + tempPassword + "</b></p>"
                    + "<p>Veuillez vous connecter et changer votre mot de passe dès que possible.</p>";

            emailService.sendEmail(utilisateur.getEmail(), subject, content);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "Utilisateur créé mais échec de l'envoi de l'email."));
        }

        return ResponseEntity
                .ok(Collections.singletonMap("message", "Utilisateur créé avec succès, un email a été envoyé."));
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> request) {
        boolean success = utilisateurService.updatePassword(
                request.get("login"), request.get("oldPassword"), request.get("newPassword"));

        return success ? ResponseEntity.ok("Mot de passe mis à jour avec succès")
                : ResponseEntity.status(400).body("Ancien mot de passe incorrect.");
    }
}
