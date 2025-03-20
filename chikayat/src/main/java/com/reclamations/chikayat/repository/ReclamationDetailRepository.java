package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.Reclamation;
import com.reclamations.chikayat.entity.ReclamationDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReclamationDetailRepository extends JpaRepository<ReclamationDetail, Integer> {

    @Query("SELECT COUNT(d) FROM ReclamationDetail d WHERE d.reclamation = :rec AND d.flagEnvoiAutorite = :flag")
    long countByReclamationAndFlagEnvoiAutorite(@Param("rec") Reclamation rec, @Param("flag") int flag);

    @Query("SELECT COUNT(d) FROM ReclamationDetail d WHERE d.reclamation = :rec AND d.flagRetourAutorite = :flag")
    long countByReclamationAndFlagRetourAutorite(@Param("rec") Reclamation rec, @Param("flag") int flag);

    @Query("SELECT COUNT(d) FROM ReclamationDetail d WHERE d.reclamation = :rec AND d.flagEnvoiReclamant = :flag")
    long countByReclamationAndFlagEnvoiReclamant(@Param("rec") Reclamation rec, @Param("flag") int flag);
}
