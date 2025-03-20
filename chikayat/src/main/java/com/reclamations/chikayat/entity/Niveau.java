package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "niveaux")
@Data
public class Niveau {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
}
