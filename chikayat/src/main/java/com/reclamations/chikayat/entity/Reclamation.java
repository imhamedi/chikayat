package com.reclamations.chikayat.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reclamations")
@Data
@Getter
@Setter
public class Reclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateDepot; // Auto-généré mais modifiable

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateInscription; // Auto-généré mais modifiable

    private String numInscription; // Généré automatiquement
    private String typeIdentifiant;
    private String identifiant;
    private String nomComplet;
    private String commune;
    private String adresse;
    private String telephone;
    private String referenceBo;
    private String pieceJointe; // Pour le chemin du fichier

    private Integer flagEnvoiAutorite;
    private Integer flagRetourAutorite;
    private Integer flagEnvoiReclamant;
    private Integer flagCloture;

    // Getters et Setters pour pieceJointe
    public String getPieceJointe() {
        return pieceJointe;
    }

    public void setPieceJointe(String pieceJointe) {
        this.pieceJointe = pieceJointe;
    }

    // Relations avec d'autres entités
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_requete", nullable = false)
    private TypeRequete typeRequete;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_type_reclamation", nullable = false)
    private TypeReclamation typeReclamation;

    private String objetReclamation;
    private String pieceJointeReclamation;
    private Integer annee;

    // Observations & Instructions
    private String observation1;
    private String observation2;
    private String observation3;
    private String instructionsGouverneur;

    // Relations avec les utilisateurs
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_user_saisie", nullable = false)
    private Utilisateur userSaisie;

    public TypeRequete getTypeRequete() {
        return typeRequete;
    }

    public void setTypeRequete(TypeRequete typeRequete) {
        this.typeRequete = typeRequete;
    }

    public TypeReclamation getTypeReclamation() {
        return typeReclamation;
    }

    public void setTypeReclamation(TypeReclamation typeReclamation) {
        this.typeReclamation = typeReclamation;
    }

    public Utilisateur getUserSaisie() {
        return userSaisie;
    }

    public void setUserSaisie(Utilisateur userSaisie) {
        this.userSaisie = userSaisie;
    }

    @ManyToOne
    @JoinColumn(name = "id_source_reclamation")
    private SourceReclamation sourceReclamation;
    @OneToMany(mappedBy = "reclamation", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonManagedReference
    private List<ReclamationDetail> details;

}
