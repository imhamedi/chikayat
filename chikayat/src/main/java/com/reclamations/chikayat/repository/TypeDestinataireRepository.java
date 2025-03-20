package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.TypeDestinataire;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeDestinataireRepository extends JpaRepository<TypeDestinataire, Integer> {
    List<TypeDestinataire> findByNomLangue2Containing(String keyword);

}
