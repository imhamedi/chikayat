package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.VoieReponse;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VoieReponseRepository extends JpaRepository<VoieReponse, Integer> {
    List<VoieReponse> findByNomLangue2Containing(String keyword);

}
