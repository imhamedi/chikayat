package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.Role;
import com.reclamations.chikayat.entity.Utilisateur;
import com.reclamations.chikayat.repository.UtilisateurRepository;
import com.reclamations.chikayat.security.PasswordValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private EmailService emailService; // Ajout du service d'envoi d'email

    @Autowired
    private PasswordValidator passwordValidator;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${validite_url_pwd_oublie}")
    private long resetTokenValidity; // Durée de validité du lien

    /**
     * Génération d’un mot de passe temporaire
     */
    public String generateTempPassword() {
        SecureRandom random = new SecureRandom();
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$%&*!";

        StringBuilder password = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            password.append(chars.charAt(random.nextInt(chars.length())));
        }
        return password.toString();
    }

    /**
     * Création d'un nouvel utilisateur avec mot de passe temporaire
     */
    public Utilisateur createUser(Utilisateur utilisateur) {
        if (utilisateurRepository.findByLogin(utilisateur.getLogin()).isPresent()) {
            throw new RuntimeException("Utilisateur déjà existant avec ce login.");
        }

        String tempPassword = generateTempPassword();
        utilisateur.setPass(passwordEncoder.encode(tempPassword));
        utilisateur.setMustChangePassword(true);

        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Enregistrement d'un nouvel utilisateur avec validation du mot de passe
     */
    public Utilisateur register(Utilisateur utilisateur) {
        if (utilisateurRepository.findByLogin(utilisateur.getLogin()).isPresent()) {
            throw new RuntimeException("Utilisateur déjà existant avec ce login.");
        }

        if (!passwordValidator.isValidPassword(utilisateur.getPass())) {
            throw new RuntimeException("Mot de passe non conforme aux règles de sécurité.");
        }

        utilisateur.setPass(passwordEncoder.encode(utilisateur.getPass()));
        utilisateur.setMustChangePassword(false);

        return utilisateurRepository.save(utilisateur);
    }

    /**
     * Recherche d'un utilisateur par login
     */
    public Optional<Utilisateur> findByLogin(String login) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByLogin(login);

        if (utilisateur.isPresent() && utilisateur.get().getRole() == null) {
            utilisateur.get().setRole(Role.UTILISATEUR); // Définit un rôle par défaut si null
        }

        return utilisateur;
    }

    /**
     * Vérification du mot de passe
     */
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * Mise à jour du mot de passe (avec vérification de l'ancien)
     */
    public boolean updatePassword(String login, String oldPassword, String newPassword) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findByLogin(login);
        if (userOpt.isPresent()) {
            Utilisateur user = userOpt.get();

            if (!passwordEncoder.matches(oldPassword, user.getPass())) {
                throw new RuntimeException("L'ancien mot de passe est incorrect.");
            }

            // Vérification des règles de mot de passe
            if (!passwordValidator.isValidPassword(newPassword)) {
                throw new RuntimeException("Le nouveau mot de passe ne respecte pas les règles de sécurité.");
            }

            user.setPass(passwordEncoder.encode(newPassword));
            utilisateurRepository.save(user);

            return true;
        }
        return false;
    }

    // *********************************
    // NOUVELLES MÉTHODES : Gestion du mot de passe oublié
    // *********************************

    /**
     * Recherche d'un utilisateur par email
     */
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }

    /**
     * Génération et envoi du token de réinitialisation du mot de passe
     */
    public String generateResetToken(String email) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findByEmail(email);
        Utilisateur utilisateur = utilisateurOpt
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec cet email"));

        String token = UUID.randomUUID().toString();
        utilisateur.setResetToken(token);
        utilisateur.setResetTokenExpiry(LocalDateTime.now().plusMinutes(resetTokenValidity));

        utilisateurRepository.save(utilisateur);

        emailService.sendPasswordResetEmail(email, token);

        return token;
    }

    /**
     * Réinitialisation du mot de passe
     */
    public void resetPassword(String token, String newPassword) {
        Utilisateur utilisateur = utilisateurRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token invalide ou expiré"));

        if (utilisateur.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expiré, veuillez en demander un nouveau.");
        }

        if (!passwordValidator.isValidPassword(newPassword)) {
            throw new RuntimeException("Le nouveau mot de passe ne respecte pas les règles de sécurité.");
        }

        utilisateur.setPass(passwordEncoder.encode(newPassword));
        utilisateur.setResetToken(null);
        utilisateur.setResetTokenExpiry(null);

        utilisateurRepository.save(utilisateur);
    }

    @Value("${app.uploads.profile-pictures-path}")
    private String profilePicturesPath;

    public List<Utilisateur> filterUsers(String nom, String email, String login, String niveau, String role) {
        return utilisateurRepository.findAll().stream()
                .filter(user -> (nom == null || user.getNom().toLowerCase().contains(nom.toLowerCase())))
                .filter(user -> (email == null || user.getEmail().toLowerCase().contains(email.toLowerCase())))
                .filter(user -> (login == null || user.getLogin().toLowerCase().contains(login.toLowerCase())))
                .filter(user -> (niveau == null || user.getNiveau().equalsIgnoreCase(niveau)))
                .filter(user -> (role == null || user.getRole().toString().equalsIgnoreCase(role)))
                .collect(Collectors.toList());
    }

    public boolean updateUser(Integer id, Utilisateur utilisateurUpdate) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            Utilisateur utilisateur = userOpt.get();
            utilisateur.setNom(utilisateurUpdate.getNom());
            utilisateur.setEmail(utilisateurUpdate.getEmail());
            utilisateur.setNiveau(utilisateurUpdate.getNiveau());
            utilisateur.setRole(utilisateurUpdate.getRole());

            utilisateurRepository.save(utilisateur);
            return true;
        }
        return false;
    }

    public boolean deleteUser(Integer id) {
        Optional<Utilisateur> userOpt = utilisateurRepository.findById(id);
        if (userOpt.isPresent()) {
            utilisateurRepository.delete(userOpt.get());
            return true;
        }
        return false;
    }

}
