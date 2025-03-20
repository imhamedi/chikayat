package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    Optional<Utilisateur> findByLogin(String login);

    Optional<Utilisateur> findByEmail(String email);

    Optional<Utilisateur> findByResetToken(String resetToken);

}
