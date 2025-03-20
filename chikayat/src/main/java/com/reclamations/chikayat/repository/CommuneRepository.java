package com.reclamations.chikayat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reclamations.chikayat.entity.Commune;

@Repository
public interface CommuneRepository extends JpaRepository<Commune, Long> {
}
