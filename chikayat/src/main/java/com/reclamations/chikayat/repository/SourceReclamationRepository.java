package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.SourceReclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SourceReclamationRepository extends JpaRepository<SourceReclamation, Integer> {
    List<SourceReclamation> findByNomLangue2Containing(String keyword);
}
