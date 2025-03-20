package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.SequenceGenerator;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SequenceGeneratorRepository extends JpaRepository<SequenceGenerator, Integer> {
    Optional<SequenceGenerator> findByAnnee(String annee);
}
