package com.reclamations.chikayat.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class PasswordValidator {

    @Value("${longueur_pws_min}")
    private int longueurMin;

    @Value("${nbr_majiscules}")
    private int nombreMaj;

    @Value("${chiffres_obligatoires}")
    private boolean chiffresObligatoires;

    @Value("${lettres_spec_obligatoires}")
    private boolean lettresSpecObligatoires;

    private String generateRegex() {
        StringBuilder regex = new StringBuilder("^");

        if (nombreMaj > 0)
            regex.append("(?=(.*[A-Z]){" + nombreMaj + "})");
        if (chiffresObligatoires)
            regex.append("(?=.*\\d)");
        if (lettresSpecObligatoires)
            regex.append("(?=.*[@#$%^&+=])");

        regex.append(".{" + longueurMin + ",}$");

        return regex.toString();
    }

    public boolean isValidPassword(String password) {
        String regex = generateRegex();
        return Pattern.compile(regex).matcher(password).matches();
    }

    public String getPasswordRules() {
        return "Le mot de passe doit contenir au moins " + longueurMin + " caractères, "
                + (nombreMaj > 0 ? nombreMaj + " majuscule(s), " : "")
                + (chiffresObligatoires ? "un chiffre, " : "")
                + (lettresSpecObligatoires ? "un caractère spécial (@#$%^&+=), " : "")
                + "et ne doit pas contenir d'espace.";
    }
}
