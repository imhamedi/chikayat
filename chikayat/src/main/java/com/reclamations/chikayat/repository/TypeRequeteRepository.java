package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.TypeRequete;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRequeteRepository extends JpaRepository<TypeRequete, Integer> {
    List<TypeRequete> findByNomLangue2Containing(String keyword);

}
