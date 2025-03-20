package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "voie_reponse")
@Data
public class VoieReponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nom_langue_2")
    private String nomLangue2;

    private String libelle;
}
