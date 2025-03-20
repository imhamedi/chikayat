package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "type_destinataire")
@Data
public class TypeDestinataire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "nom_langue_2")
    private String nomLangue2;

    private String libelle;
}
