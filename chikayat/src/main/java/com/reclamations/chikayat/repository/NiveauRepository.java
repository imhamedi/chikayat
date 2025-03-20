package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.Niveau;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NiveauRepository extends JpaRepository<Niveau, Integer> {
    List<Niveau> findByDescriptionContaining(String description);

}
