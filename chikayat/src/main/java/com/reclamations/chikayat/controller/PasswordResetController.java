package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/password-reset")
public class PasswordResetController {

    private final UtilisateurService utilisateurService;

    public PasswordResetController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        utilisateurService.generateResetToken(email);
        return ResponseEntity.ok("Un email de réinitialisation a été envoyé.");
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        utilisateurService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Votre mot de passe a été mis à jour avec succès.");
    }
}
