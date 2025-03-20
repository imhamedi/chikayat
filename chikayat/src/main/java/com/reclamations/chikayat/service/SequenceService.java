package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.SequenceGenerator;
import com.reclamations.chikayat.repository.SequenceGeneratorRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SequenceService {

    @Autowired
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    // Lire les valeurs du fichier application.properties
    @Value("${sequence_method}")
    private String sequenceMethod;

    @Value("${num_inscription_format}")
    private String numInscriptionFormat;

    public synchronized String generateNumInscription() {
        LocalDate today = LocalDate.now();
        String annee = String.valueOf(today.getYear()); // YYYY
        String anneeShort = annee.substring(2); // YY
        String mois = String.format("%02d", today.getMonthValue()); // MM
        String jour = String.format("%02d", today.getDayOfMonth()); // DD

        // Récupérer la séquence de l'année en cours
        Optional<SequenceGenerator> existingSequence = sequenceGeneratorRepository.findByAnnee(annee);
        SequenceGenerator sequence;

        if (existingSequence.isPresent()) {
            sequence = existingSequence.get();
            sequence.setDernierNumero(sequence.getDernierNumero() + 1);
        } else {
            // Nouvelle année, réinitialisation du compteur
            sequence = new SequenceGenerator();
            sequence.setAnnee(annee);
            sequence.setDernierNumero(1);
        }

        // Sauvegarde de la nouvelle séquence
        sequenceGeneratorRepository.save(sequence);

        // Générer le numéro d'inscription selon le format défini
        return formatNumInscription(sequence.getDernierNumero(), anneeShort, mois, jour);
    }

    private String formatNumInscription(int numero, String anneeShort, String mois, String jour) {
        if ("YYMMDD.XXXX".equalsIgnoreCase(numInscriptionFormat)) {
            return anneeShort + mois + jour + "." + String.format("%04d", numero);
        } else if ("XXXX/MM(YYYY)".equalsIgnoreCase(numInscriptionFormat)) {
            return numero + "/" + mois + "(" + anneeShort + ")";
        } else if ("XXXX-YYYY".equalsIgnoreCase(numInscriptionFormat)) {
            return numero + "-" + anneeShort;
        } else if ("YYYY/MM/XXXX".equalsIgnoreCase(numInscriptionFormat)) {
            return anneeShort + "/" + mois + "/" + numero;
        } else {
            return anneeShort + mois + jour + "." + String.format("%04d", numero); // Format par défaut
        }
    }
}