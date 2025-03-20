package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.Niveau;
import com.reclamations.chikayat.repository.NiveauRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NiveauService {

    @Autowired
    private NiveauRepository niveauRepository;

    public List<Niveau> findAll() {
        return niveauRepository.findAll();
    }

    public Niveau save(Niveau niveau) {
        return niveauRepository.save(niveau);
    }

    public Optional<Niveau> findById(Integer id) {
        return niveauRepository.findById(id);
    }

    public Niveau update(Niveau niveau) {
        // On suppose que l'id est présent dans l'objet
        return niveauRepository.save(niveau);
    }

    public void delete(Integer id) {
        niveauRepository.deleteById(id);
    }

    public List<Niveau> search(String keyword) {
        return niveauRepository.findByDescriptionContaining(keyword);
    }
}
