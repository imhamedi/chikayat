package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "sequence_generator")
public class SequenceGenerator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String mois;
    private String annee;
    private int dernierNumero;
}
