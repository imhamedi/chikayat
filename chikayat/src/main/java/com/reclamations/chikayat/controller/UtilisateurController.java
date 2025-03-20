package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.ResetPasswordRequest;
import com.reclamations.chikayat.entity.Role;
import com.reclamations.chikayat.entity.Utilisateur;
import com.reclamations.chikayat.security.JwtUtil;
import com.reclamations.chikayat.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.reclamations.chikayat.repository.UtilisateurRepository;
import java.util.Set;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/utilisateurs")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    /**
     * 🔹 Inscription d'un nouvel utilisateur (Self-registration).
     */
    @PostMapping("/register")
    public ResponseEntity<Utilisateur> register(@RequestBody Utilisateur utilisateur) {
        try {
            Utilisateur newUser = utilisateurService.register(utilisateur);
            return ResponseEntity.ok(newUser);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    /**
     * 🔹 Recherche d'un utilisateur par login.
     */
    @GetMapping("/{login}")
    public ResponseEntity<Utilisateur> getByLogin(@PathVariable String login) {
        Optional<Utilisateur> user = utilisateurService.findByLogin(login);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @GetMapping("/me")
    public ResponseEntity<Utilisateur> getAuthenticatedUser(@RequestHeader("Authorization") String token) {
        String login = jwtUtil.extractUsername(token.substring(7)); // Suppression du préfixe "Bearer "
        Optional<Utilisateur> user = utilisateurService.findByLogin(login);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestHeader("Authorization") String token,
            @RequestBody Utilisateur utilisateurUpdate) {
        String login = jwtUtil.extractUsername(token.substring(7));

        Optional<Utilisateur> userOpt = utilisateurService.findByLogin(login);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur non trouvé.");
        }

        Utilisateur utilisateur = userOpt.get();
        utilisateur.setNom(utilisateurUpdate.getNom());
        utilisateur.setEmail(utilisateurUpdate.getEmail());
        utilisateur.setNiveau(utilisateurUpdate.getNiveau());

        utilisateurRepository.save(utilisateur);
        return ResponseEntity.ok("Profil mis à jour avec succès.");
    }

    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestHeader("Authorization") String token,
            @RequestBody Map<String, String> request) {
        String login = jwtUtil.extractUsername(token.substring(7));
        String oldPassword = request.get("oldPassword");
        String newPassword = request.get("newPassword");

        boolean success = utilisateurService.updatePassword(login, oldPassword, newPassword);

        if (!success) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ancien mot de passe incorrect.");
        }

        return ResponseEntity.ok("Mot de passe mis à jour avec succès.");
    }

    @Value("${app.uploads.profile-pictures-path}")
    private String profilePicturesPath;

    public UtilisateurController(UtilisateurRepository utilisateurRepository, JwtUtil jwtUtil) {
        this.utilisateurRepository = utilisateurRepository;
        this.jwtUtil = jwtUtil;
    }

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png", "gif");

    @PostMapping("/upload-profile-picture")
    public ResponseEntity<?> uploadProfilePicture(@RequestHeader("Authorization") String token,
            @RequestParam("file") MultipartFile file) {
        String login = jwtUtil.extractUsername(token.substring(7));

        Optional<Utilisateur> userOpt = utilisateurRepository.findByLogin(login);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Utilisateur introuvable.");
        }

        Utilisateur utilisateur = userOpt.get();

        // ✅ Vérification de l'extension du fichier
        String originalFilename = Objects.requireNonNull(file.getOriginalFilename());
        String extension = originalFilename.substring(originalFilename.lastIndexOf('.') + 1).toLowerCase();

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Format non supporté. Formats autorisés : " + ALLOWED_EXTENSIONS);
        }

        // ✅ Génération du nom du fichier
        String fileName = login + "_" + utilisateur.getId() + "." + extension;

        // ✅ Construction du chemin du fichier
        Path path = Paths.get(profilePicturesPath, fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, file.getBytes());

            // Mise à jour de l'utilisateur avec le nouveau nom de fichier
            utilisateur.setProfilePicture(fileName);
            utilisateurRepository.save(utilisateur);

            // Retourner le nom du fichier pour mise à jour instantanée sur le frontend
            return ResponseEntity.ok().body(Map.of("fileName", fileName));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'enregistrement du fichier.");
        }
    }

    /**
     * 🔹 Authentification et génération de JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Utilisateur utilisateur) {
        System.out.println("🔍 Tentative de connexion : " + utilisateur.getLogin());

        Optional<Utilisateur> user = utilisateurService.findByLogin(utilisateur.getLogin());

        if (user.isPresent() && utilisateurService.checkPassword(utilisateur.getPass(), user.get().getPass())) {
            Utilisateur utilisateurTrouve = user.get();

            // Vérifier si le rôle est null et lui attribuer un rôle par défaut
            if (utilisateurTrouve.getRole() == null) {
                utilisateurTrouve.setRole(Role.UTILISATEUR); // 🔥 Définit un rôle par défaut
            }

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    utilisateurTrouve.getLogin(),
                    utilisateurTrouve.getPass(),
                    Collections.emptyList());

            String token = jwtUtil.generateToken(userDetails, utilisateurTrouve.getRole().name());

            System.out.println("✅ Connexion réussie, token généré !");
            return ResponseEntity.ok().body(token);
        } else {
            System.out.println("❌ Identifiants incorrects !");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de connexion : Identifiants incorrects.");
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("L'email est requis.");
        }

        utilisateurService.generateResetToken(email);
        return ResponseEntity.ok("Un email de réinitialisation a été envoyé.");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        utilisateurService.resetPassword(request.getToken(), request.getNewPassword()); // Pas besoin d'affecter un
                                                                                        // boolean
        return ResponseEntity.ok("Mot de passe réinitialisé avec succès.");
    }

    @RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {
            Map<String, String> response = new HashMap<>();
            response.put("error", ex.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Value("${longueur_pws_min}")
    private int longueurMin;

    @Value("${nbr_majiscules}")
    private int nbrMajuscules;

    @Value("${chiffres_obligatoires}")
    private boolean chiffresObligatoires;

    @Value("${lettres_spec_obligatoires}")
    private boolean lettresSpecObligatoires;

    @GetMapping("/password-rules")
    public Map<String, Object> getPasswordRules() {
        Map<String, Object> rules = new HashMap<>();
        rules.put("longueurMin", longueurMin);
        rules.put("nbrMajuscules", nbrMajuscules);
        rules.put("chiffresObligatoires", chiffresObligatoires);
        rules.put("lettresSpecObligatoires", lettresSpecObligatoires);
        return rules;
    }

}
