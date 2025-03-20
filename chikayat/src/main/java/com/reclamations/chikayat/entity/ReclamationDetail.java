package com.reclamations.chikayat.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "reclamations_detail")
@Data
public class ReclamationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Référence vers la réclamation principale
    @ManyToOne
    @JoinColumn(name = "id_reclamation", nullable = false)
    @JsonBackReference
    private Reclamation reclamation;

    // Pour lier le numéro d'inscription
    private String numInscription;

    // Envoi vers l'autorité
    private Integer flagEnvoiAutorite;
    private LocalDateTime dateEnvoiAutorite;

    // Pour connaître le type de destinataire lors de l'envoi
    private String referenceEnvoiAutorite;

    // Retour de l'autorité
    private Integer flagRetourAutorite;
    private LocalDateTime dateRetourAutorite;
    private String referenceRetourAutorite;

    // Envoi vers le réclamant
    private Integer flagEnvoiReclamant;
    private LocalDateTime dateEnvoiReclamant;
    @ManyToOne
    @JoinColumn(name = "voie_envoi_reclamant")
    private VoieReponse voieEnvoiReclamant;
    private String referenceEnvoiReclamant;

    // Utilisateurs ayant réalisé les actions
    @ManyToOne
    @JoinColumn(name = "id_user_envoi_autorite")
    private Utilisateur userEnvoiAutorite;
    @ManyToOne
    @JoinColumn(name = "id_user_retour_autorite")
    private Utilisateur userRetourAutorite;
    @ManyToOne
    @JoinColumn(name = "id_user_envoi_reclamant")
    private Utilisateur userEnvoiReclamant;
    @ManyToOne(optional = true)
    @JoinColumn(name = "id_type_destinataire", nullable = true)
    private TypeDestinataire typeDestinataire;

    public TypeDestinataire getTypeDestinataire() {
        return typeDestinataire;
    }

    public void setTypeDestinataire(TypeDestinataire typeDestinataire) {
        this.typeDestinataire = typeDestinataire;
    }

    public int getSafeFlagEnvoiAutorite() {
        return flagEnvoiAutorite != null ? flagEnvoiAutorite : 0;
    }

    public int getSafeFlagRetourAutorite() {
        return flagRetourAutorite != null ? flagRetourAutorite : 0;
    }

    public int getSafeFlagEnvoiReclamant() {
        return flagEnvoiReclamant != null ? flagEnvoiReclamant : 0;
    }

}
