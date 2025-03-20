package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.TypeReclamation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeReclamationRepository extends JpaRepository<TypeReclamation, Integer> {
    List<TypeReclamation> findByNomLangue2Containing(String keyword);

}
