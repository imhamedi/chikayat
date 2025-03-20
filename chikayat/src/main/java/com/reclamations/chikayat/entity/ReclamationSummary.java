package com.reclamations.chikayat.entity;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ReclamationSummary {
    private String numInscription;
    private LocalDateTime dateDepot;
    private String nomComplet;
    private String objetReclamation;
    private String typeReclamationLabel;
    private String typeRequeteLabel;
    private String flagEnvoiAutoriteText;
    private String flagRetourAutoriteText;
    private String flagEnvoiReclamantText;
    private String instructionsGouverneur;

    public ReclamationSummary(String numInscription, LocalDateTime dateDepot, String nomComplet,
            String objetReclamation,
            String flagEnvoiAutoriteText, String flagRetourAutoriteText, String flagEnvoiReclamantText) {
        this.numInscription = numInscription;
        this.dateDepot = dateDepot;
        this.nomComplet = nomComplet;
        this.objetReclamation = objetReclamation;
        this.flagEnvoiAutoriteText = flagEnvoiAutoriteText;
        this.flagRetourAutoriteText = flagRetourAutoriteText;
        this.flagEnvoiReclamantText = flagEnvoiReclamantText;
    }

    public ReclamationSummary() {
    }

}
