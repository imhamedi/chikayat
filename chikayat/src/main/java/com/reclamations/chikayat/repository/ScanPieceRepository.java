package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.ScanPiece;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ScanPieceRepository extends JpaRepository<ScanPiece, Integer> {
    List<ScanPiece> findByNumInscription(String numInscription);
}
