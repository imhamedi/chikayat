package com.reclamations.chikayat.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "scans_pieces")
@Data
public class ScanPiece {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String language;

    @Column(length = 10000) // Augmente la taille maximale pour du texte plus long
    private String extractedText;

    private LocalDateTime dateUploaded;

    @Column(name = "num_inscription", length = 45)
    private String numInscription;
}
