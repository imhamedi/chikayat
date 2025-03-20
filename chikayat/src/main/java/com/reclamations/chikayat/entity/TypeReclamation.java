package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "type_reclamation")
@Data
public class TypeReclamation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String libelle;
    @Column(name = "nom_langue_2")
    private String nomLangue2;

}
