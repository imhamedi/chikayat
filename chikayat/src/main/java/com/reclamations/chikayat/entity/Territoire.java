package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "territoires")
@Data
public class Territoire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String codeVille;

    @Column(name = "nom_ville_langue_2")
    private String nomVilleLangue2;

    private String codeCommune;

    @Column(name = "nom_commune_langue_2")
    private String nomCommuneLangue2;

    private String codeArrondissement;

    @Column(name = "nom_arrondissement_langue_2")
    private String nomArrondissementLangue2;
}
